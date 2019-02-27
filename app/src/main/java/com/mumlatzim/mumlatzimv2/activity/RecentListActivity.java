package com.mumlatzim.mumlatzimv2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.mumlatzim.mumlatzimv2.R;
import com.mumlatzim.mumlatzimv2.adapters.PostAdapter;
import com.mumlatzim.mumlatzimv2.api.ApiUtilities;
import com.mumlatzim.mumlatzimv2.data.constant.AppConstant;
import com.mumlatzim.mumlatzimv2.data.sqlite.BookmarkDbController;
import com.mumlatzim.mumlatzimv2.listeners.ListItemClickListener;
import com.mumlatzim.mumlatzimv2.models.bookmark.BookmarkModel;
import com.mumlatzim.mumlatzimv2.models.post.Post;
import com.mumlatzim.mumlatzimv2.utility.ActivityUtilities;
import com.mumlatzim.mumlatzimv2.utility.AdsUtilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentListActivity extends BaseActivity {

    private Activity mActivity;
    private Context mContext;

    private List<Post> mRecentList;
    private List<Post> mFeatureList;
    private RecyclerView mRvRecentPosts;
    private PostAdapter mRecentAdapter = null;

    private RelativeLayout mBottomLayout;
    private StaggeredGridLayoutManager mLayoutManager;
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
        mActivity = RecentListActivity.this;
        mContext = mActivity.getApplicationContext();

        mRecentList = new ArrayList<>();
        mBookmarkList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_recent_post_list);

        mRvRecentPosts = (RecyclerView) findViewById(R.id.rvPosts);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRvRecentPosts.setLayoutManager(mLayoutManager);

        mRecentAdapter = new PostAdapter(mActivity, (ArrayList<Post>) mRecentList);
        mRvRecentPosts.setAdapter(mRecentAdapter);

        mBottomLayout = (RelativeLayout) findViewById(R.id.rv_itemload);

        mFeatureList= getIntent().getParcelableArrayListExtra(AppConstant.BUNDLE_KEY_FEATURE_LIST);

        initToolbar(true);
        setToolbarTitle(getString(R.string.recent));
        enableUpButton();
        initLoader();
    }

    private void initFunctionality() {

        showLoader();

        loadRecentPosts();

        // show full-screen ads
        AdsUtilities.getInstance(mContext).showFullScreenAd();
        // show banner ads
        AdsUtilities.getInstance(mContext).showBannerAd((AdView) findViewById(R.id.adsView));
    }

    public void initListener() {

        mRecentAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Post model = mRecentList.get(position);
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

    public void loadRecentPosts() {
        ApiUtilities.getApiInterface().getLatestPosts(mPageNo).enqueue(new Callback<List<Post>>() {
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
        if(mFeatureList!=null)
        {
            mRecentList.addAll(response.body());
            for(int i=0;i<mFeatureList.size();i++)
            {
                for(int j=0;j<mRecentList.size();j++)
                {
                    if(mRecentList.get(j).getID().equals(mFeatureList.get(i).getID()))
                    {
                        mRecentList.remove(j);
                    }
                }
            }

        }
        else
        {
            mRecentList.addAll(response.body());
        }

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
                mPastVisibleItems = mLayoutManager.findFirstVisibleItemPositions(null)[0];

                if (mUserScrolled && (mVisibleItemCount + mPastVisibleItems) == mTotalItemCount) {
                    mUserScrolled = false;

                    if(mBottomLayout.getVisibility()!=View.VISIBLE)
                    {
                        updateRecyclerView();
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
                loadRecentPosts();

            }
        }, 0);

    }

    private void updateUI() {

        if (mBookmarkDbController == null) {
            mBookmarkDbController = new BookmarkDbController(mActivity);
        }

        mBookmarkList.clear();
        mBookmarkList.addAll(mBookmarkDbController.getAllData());

        for (int i = 0; i < mRecentList.size(); i++) {
            boolean isBookmarkSet = false;
            for (int j = 0; j < mBookmarkList.size(); j++) {
                if (mRecentList.get(i).getID() == mBookmarkList.get(j).getPostId()) {
                    mRecentList.get(i).setBookmark(true);
                    isBookmarkSet = true;
                    break;
                }
            }
            if (!isBookmarkSet) {
                mRecentList.get(i).setBookmark(false);
            }
        }

        if (mRecentList.size() == 0) {
            showEmptyView();
        } else {
            mRecentAdapter.notifyDataSetChanged();
            hideLoader();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRecentList.size() != 0) {
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