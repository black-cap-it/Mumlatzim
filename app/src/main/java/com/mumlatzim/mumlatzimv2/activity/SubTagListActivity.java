package com.mumlatzim.mumlatzimv2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.mumlatzim.mumlatzimv2.R;
import com.mumlatzim.mumlatzimv2.adapters.HomeRecentPostAdapter;
import com.mumlatzim.mumlatzimv2.adapters.TagsAdapter;
import com.mumlatzim.mumlatzimv2.api.ApiUtilities;
import com.mumlatzim.mumlatzimv2.data.constant.AppConstant;
import com.mumlatzim.mumlatzimv2.data.sqlite.BookmarkDbController;
import com.mumlatzim.mumlatzimv2.listeners.ListItemClickListener;
import com.mumlatzim.mumlatzimv2.models.bookmark.BookmarkModel;
import com.mumlatzim.mumlatzimv2.models.category.Tags;
import com.mumlatzim.mumlatzimv2.models.post.Post;
import com.mumlatzim.mumlatzimv2.utility.ActivityUtilities;
import com.mumlatzim.mumlatzimv2.utility.AdsUtilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubTagListActivity extends BaseActivity {
    private static final String TAG = "constants";
    private Activity mActivity;
    private Context mContext;

    private ArrayList<Tags> mTagsList;
    private ArrayList<Tags> mChildTagsList;
    private TagsAdapter mTagsAdapter = null;
    private RecyclerView mTagsRecycler;
    private String mTagsName;
    private int mTagsId;


    private List<Post> mCategoryWisePostList;
    private RecyclerView mRvRecentPosts;
    private HomeRecentPostAdapter mRecentAdapter = null;

    private RelativeLayout mBottomLayout;
    private LinearLayoutManager mLayoutManager;
    private boolean mUserScrolled = true;
    private int mPageNo = 1, mPastVisibleItems, mVisibleItemCount, mTotalItemCount;

    // Bookmarks view
    private List<BookmarkModel> mBookmarkList;
    private BookmarkDbController mBookmarkDbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        initFunctionality();
        initListener();
        implementScrollListener();
    }

    private void initVar() {
        mActivity = SubTagListActivity.this;
        mContext = mActivity.getApplicationContext();

        Intent intent = getIntent();

        if (intent != null) {
            mTagsId= intent.getIntExtra(AppConstant.BUNDLE_KEY_TAGS_ID, 0);
            Log.e("mtage id is ",""+mTagsId);
            mTagsName = intent.getStringExtra(AppConstant.BUNDLE_KEY_TAGS_NAME);
           // mTagsList = intent.getParcelableArrayListExtra(AppConstant.BUNDLE_KEY_TAGS_LIST);
        }

        mChildTagsList = new ArrayList<>();
        mCategoryWisePostList = new ArrayList<>();
        mBookmarkList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_category_list);

        mTagsRecycler = (RecyclerView) findViewById(R.id.rvCategories);
      /*  mTagsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mTagsAdapter = new TagsAdapter(mContext, mChildTagsList);
        mTagsRecycler.setAdapter(mTagsAdapter);
*/
        mBottomLayout = (RelativeLayout) findViewById(R.id.rv_itemload);

        mRvRecentPosts = (RecyclerView) findViewById(R.id.rvPosts);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvRecentPosts.setLayoutManager(mLayoutManager);

        mRecentAdapter = new HomeRecentPostAdapter(mActivity, (ArrayList<Post>) mCategoryWisePostList);
        mRvRecentPosts.setAdapter(mRecentAdapter);

        mBottomLayout = (RelativeLayout) findViewById(R.id.rv_itemload);

        initToolbar(true);
        setToolbarTitle(Html.fromHtml(mTagsName).toString());
        enableUpButton();
        initLoader();
    }

    private void initFunctionality() {

        showLoader();

       // loadSubCategories();

        loadCategoryWisePosts();

        // show banner ads
        AdsUtilities.getInstance(mContext).showBannerAd((AdView) findViewById(R.id.adsView));
    }

    private void initListener() {
  /*      mTagsAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Tags model = mChildTagsList.get(position);
                switch (view.getId()) {
                    case R.id.lyt_container:
                        ActivityUtilities.getInstance().subTagsListActivity(mActivity, SubCategoryListActivity.class, model.getmID().intValue(), model.getmName(), mTagsList, false);
                        break;
                    default:
                        break;
                }
            }
        });
*/
        mRecentAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Post model = mCategoryWisePostList.get(position);
                switch (view.getId()) {
                    case R.id.btn_book:
                        if (model.isBookmark()) {
                            mBookmarkDbController.deleteEachFav(model.getID().intValue());
                            model.setBookmark(false);
                            mRecentAdapter.notifyDataSetChanged();
                            Toast.makeText(mActivity, getString(R.string.removed_from_book), Toast.LENGTH_SHORT).show();

                        } else {
                            int postId = model.getID().intValue();
                            String imgUrl = model.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getFullSize().getSourceUrl();
                            String postTitle = model.getTitle().getRendered();
                            String postUrl = model.getPostUrl();
                            String postCategory = model.getEmbedded().getWpTerms().get(0).get(0).getName();
                            String postDate = model.getFormattedDate();

                            mBookmarkDbController.insertData(postId, imgUrl, postTitle, postUrl, postCategory, postDate);
                            model.setBookmark(true);
                            mRecentAdapter.notifyDataSetChanged();
                            Toast.makeText(mActivity, getString(R.string.added_to_bookmark), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.btn_share:
                        final String appPackageName = getPackageName();
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(model.getPostUrl())
                                + AppConstant.EMPTY_STRING
                                );
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                        break;
                    case R.id.card_view_top:
                        ActivityUtilities.getInstance().invokePostDetailsActivity(mActivity, PostDetailsActivity.class, model.getID().intValue(), false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void loadSubCategories() {
        mChildTagsList.addAll(mTagsList);
        mTagsAdapter.notifyDataSetChanged();
    }

    public void loadCategoryWisePosts() {
        ApiUtilities.getApiInterface().getPostsByTags(mPageNo, mTagsId).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    loadPosts(response);
                } else {
                    hideMoreItemLoader();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                showEmptyView();
                mBottomLayout.setVisibility(View.GONE);
            }
        });
    }

    public void loadPosts(Response<List<Post>> response) {
        mCategoryWisePostList.addAll(response.body());

        updateUI();

        hideMoreItemLoader();
    }

    private void hideMoreItemLoader() {
        mBottomLayout.setVisibility(View.GONE);
        mUserScrolled = true;
    }

    private void implementScrollListener() {
        mRvRecentPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mUserScrolled = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,
                                   int dy) {

                super.onScrolled(recyclerView, dx, dy);

                mVisibleItemCount = mLayoutManager.getChildCount();
                mTotalItemCount = mLayoutManager.getItemCount();
                mPastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if(dy>0)
                {
                    if (mUserScrolled && (mVisibleItemCount + mPastVisibleItems) == mTotalItemCount) {
                        mUserScrolled = false;

                        if(mBottomLayout.getVisibility()!=View.VISIBLE)
                        {
                            updateRecyclerView();
                        }
                    }
                }
            }
        });

    }

    private void updateRecyclerView() {
        mBottomLayout.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mPageNo++;
                loadCategoryWisePosts();

            }
        }, 0);

    }

    private void updateUI() {

        if (mBookmarkDbController == null) {
            mBookmarkDbController = new BookmarkDbController(mActivity);
        }

        mBookmarkList.clear();
        mBookmarkList.addAll(mBookmarkDbController.getAllData());

        for (int i = 0; i < mCategoryWisePostList.size(); i++) {
            boolean isBookmarkSet = false;
            for (int j = 0; j < mBookmarkList.size(); j++) {
                if (mCategoryWisePostList.get(i).getID() == mBookmarkList.get(j).getPostId()) {
                    mCategoryWisePostList.get(i).setBookmark(true);
                    isBookmarkSet = true;
                    break;
                }
            }
            if (!isBookmarkSet) {
                mCategoryWisePostList.get(i).setBookmark(false);
            }
        }

        mRecentAdapter.notifyDataSetChanged();
        hideLoader();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCategoryWisePostList.size() != 0) {
            updateUI();
        }

        // load full screen ad
        AdsUtilities.getInstance(mContext).loadFullScreenAd(mActivity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}