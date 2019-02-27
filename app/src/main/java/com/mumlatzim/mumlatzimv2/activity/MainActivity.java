package com.mumlatzim.mumlatzimv2.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.mumlatzim.mumlatzimv2.R;
import com.mumlatzim.mumlatzimv2.adapters.FeaturedPagerAdapter;
import com.mumlatzim.mumlatzimv2.adapters.HomeRecentPostAdapter;
import com.mumlatzim.mumlatzimv2.adapters.PostAdapter;
import com.mumlatzim.mumlatzimv2.api.ApiUtilities;
import com.mumlatzim.mumlatzimv2.data.constant.AppConstant;
import com.mumlatzim.mumlatzimv2.data.sqlite.BookmarkDbController;
import com.mumlatzim.mumlatzimv2.listeners.ListItemClickListener;
import com.mumlatzim.mumlatzimv2.models.bookmark.BookmarkModel;
import com.mumlatzim.mumlatzimv2.models.post.Post;
import com.mumlatzim.mumlatzimv2.utility.ActivityUtilities;
import com.mumlatzim.mumlatzimv2.utility.AdsUtilities;
import com.mumlatzim.mumlatzimv2.utility.AppUtilities;
import com.mumlatzim.mumlatzimv2.utility.RateItDialogFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private Activity mActivity;
    private Context mContext;

  //  private RelativeLayout mNotificationView;
    private ImageButton mImgBtnSearch,hamberger,toolbar_gridButton;


    private int mItemCount = 5, mPageNo = 1;
    private List<Post> mFeaturedList;
    private ViewPager mFeaturedPager;
    private FeaturedPagerAdapter mFeaturedPagerAdapter = null;
    private String imgUrl = null;
    private List<Post> mRecentPostList;
    private RecyclerView mRvPosts;
    private HomeRecentPostAdapter mRecentAdapter = null;
    private PostAdapter mPostAdapter=null;

    private TextView mViewAllFeatured, mViewAllRecent;


    // Bookmarks view
    private List<BookmarkModel> mBookmarkList;
    private BookmarkDbController mBookmarkDbController;

    //SharedPreference
    private boolean changeView;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mLytFeatured, mLytCategories, mLytRecent, mBottomLayout;

    private boolean mUserScrolled = true;
    private int mRecentPageNo = 1, mPastVisibleItems, mVisibleItemCount, mTotalItemCount;
    private LinearLayoutManager mLayoutManager;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RateItDialogFragment.show(this, getSupportFragmentManager());

        initVar();
        initView();
        loadData();
        initListener();
        implementScrollListener();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //unregister broadcast receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(newNotificationReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //register broadcast receiver
        IntentFilter intentFilter = new IntentFilter(AppConstant.NEW_NOTI);
        LocalBroadcastManager.getInstance(this).registerReceiver(newNotificationReceiver, intentFilter);

      //  initNotification();

        if (mRecentPostList.size() != 0) {
            updateUI();
        }
        // load full screen ad
        AdsUtilities.getInstance(mContext).loadFullScreenAd(mActivity);
    }

    // received new broadcast
    private BroadcastReceiver newNotificationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
          //  initNotification();
        }
    };


    @Override
    public void onBackPressed()
    {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END))
        {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        }
        else
        {
            AppUtilities.tapPromptToExit(mActivity);
        }
    }

    private void initVar() {
        mActivity = MainActivity.this;
        mContext = getApplicationContext();

        mFeaturedList = new ArrayList<>();
        mRecentPostList = new ArrayList<>();
        mBookmarkList = new ArrayList<>();

    }

    private void initView() {
        setContentView(R.layout.activity_main);

        try
        {
            pref = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            editor = pref.edit();
        }
        catch (NullPointerException e)
        {
            Log.e("no data to read ",e.toString());
        }

       // mNotificationView = (RelativeLayout) findViewById(R.id.notificationView);
        mImgBtnSearch = (ImageButton) findViewById(R.id.imgBtnSearch);
        toolbar_gridButton=(ImageButton) findViewById(R.id.toolbar_gridButton);
        hamberger=(ImageButton)findViewById(R.id.hamberger);
        mFeaturedPager = (ViewPager) findViewById(R.id.pager_featured_posts);
        mViewAllFeatured = (TextView) findViewById(R.id.view_all_featured);
        mRvPosts = (RecyclerView) findViewById(R.id.rvRecent);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        boolean checkStatus=pref.getBoolean("status",false);
        if(checkStatus)
        {
            Log.e("TAG","inside checkStatus if");
            changeView=false;
            toolbar_gridButton.setBackground(getResources().getDrawable(R.drawable.ic_grid_on));
            mGridLayoutManager=new GridLayoutManager(mActivity,2,GridLayoutManager.VERTICAL,false);
            mRvPosts.setLayoutManager(mGridLayoutManager);

        }
        else
        {
            Log.e("TAG","inside checkStatus else");
            changeView=true;
            toolbar_gridButton.setBackground(getResources().getDrawable(R.drawable.ic_grid_off));
            mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRvPosts.setLayoutManager(mLayoutManager);
        }

        mRecentAdapter = new HomeRecentPostAdapter(mActivity, (ArrayList<Post>) mRecentPostList);
        mRvPosts.setAdapter(mRecentAdapter);

        mViewAllRecent = (TextView) findViewById(R.id.view_all_recent);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        mLytFeatured = (RelativeLayout) findViewById(R.id.lyt_featured);

        mLytRecent = (RelativeLayout) findViewById(R.id.lyt_recent);

        mBottomLayout = (RelativeLayout) findViewById(R.id.rv_itemload);

        initToolbar(false);
        initDrawer();
        initLoader();
    }

    private void initListener() {
        //notification view click listener
    /*    mNotificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtilities.getInstance().invokeNewActivity(mActivity, NotificationListActivity.class, false);
            }
        });
*/
        // Search button click listener
        mImgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtilities.getInstance().invokeNewActivity(mActivity, SearchActivity.class, false);
            }
        });

        hamberger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        //changes Layout View
        toolbar_gridButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayoutView();
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLytFeatured.setVisibility(View.GONE);
                mLytRecent.setVisibility(View.GONE);

                mRecentPageNo = 1;

                mFeaturedList.clear();
                mRecentPostList.clear();

                loadData();
            }
        });
        mFeaturedPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                swipeRefreshController(state == ViewPager.SCROLL_STATE_IDLE);
            }
        });

        mViewAllFeatured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtilities.getInstance().invokeNewActivity(mActivity, FeaturedListActivity.class, false);
            }
        });

        mRecentAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Post model = mRecentPostList.get(position);
                switch (view.getId()) {
                    case R.id.btn_book:
                        if (model.isBookmark()) {
                            mBookmarkDbController.deleteEachFav(model.getID().intValue());
                           // dbInstance.orderDeo().deleteBookmarks(model.getID().intValue());
                            model.setBookmark(false);
                            mRecentAdapter.notifyDataSetChanged();
                            Toast.makeText(mActivity, getString(R.string.removed_from_book), Toast.LENGTH_SHORT).show();

                        } else {
                            int postId = model.getID().intValue();
                            if (model.getEmbedded().getWpFeaturedMedias().size() > 0) {
                                if (model.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails() != null) {
                                    if(model.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getThumbnailSize().getSourceUrl() != null)
                                    {
                                        imgUrl=model.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getThumbnailSize().getSourceUrl();
                                    }
                                    else if (model.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getFullSize().getSourceUrl() != null)
                                    {
                                        imgUrl = model.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getFullSize().getSourceUrl();
                                    }
                                    else
                                    {
                                        imgUrl="";
                                    }
                                }
                                else
                                {
                                    imgUrl="";
                                }
                            }
                            else
                            {
                                imgUrl="";
                            }
                           // String imgUrl = model.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getThumbnailSize().getSourceUrl();

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

        mViewAllRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtilities.getInstance().invokeRecentActivity(mActivity, RecentListActivity.class, (ArrayList<Post>) mFeaturedList, false);
            }
        });
    }


    private void changeLayoutView() {
        if(changeView)
        {
            editor.putBoolean("status",true);
            editor.commit();
            toolbar_gridButton.setBackground(getResources().getDrawable(R.drawable.ic_grid_on));
            mGridLayoutManager=new GridLayoutManager(mActivity,2,GridLayoutManager.VERTICAL,false);
            mRvPosts.setLayoutManager(mGridLayoutManager);
            mRecentAdapter.notifyDataSetChanged();
            Toast.makeText(this, R.string.gridView, Toast.LENGTH_SHORT).show();
            changeView=false;
        }
        else
        {
            changeView=true;
            editor.clear();
            editor.commit();
            toolbar_gridButton.setBackground(getResources().getDrawable(R.drawable.ic_grid_off));
            mLayoutManager=new LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false);
            mRvPosts.setLayoutManager(mLayoutManager);
            mRecentAdapter.notifyDataSetChanged();
            Toast.makeText(this, R.string.linearView, Toast.LENGTH_SHORT).show();
        }
    }

   private void swipeRefreshController(boolean enable) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(enable);
        }
    }

    private void loadData() {
        showLoader();

        loadFeaturedPosts();
        loadRecentPosts();

        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        // show banner ads
        AdsUtilities.getInstance(mContext).showBannerAd((AdView) findViewById(R.id.adsView));
    }

    public void loadFeaturedPosts() {
        ApiUtilities.getApiInterface().getFeaturedPosts(mPageNo).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    mFeaturedList.addAll(response.body());
                    mFeaturedPagerAdapter = new FeaturedPagerAdapter(mActivity, (ArrayList<Post>) mFeaturedList);
                    mFeaturedPager.setAdapter(mFeaturedPagerAdapter);
                    mFeaturedPagerAdapter.setItemClickListener(new ListItemClickListener() {
                        @Override
                        public void onItemClick(int position, View view) {
                            int clickedPostId = mFeaturedList.get(position).getID().intValue();
                            ActivityUtilities.getInstance().invokePostDetailsActivity(mActivity, PostDetailsActivity.class, clickedPostId, false);
                        }
                    });
                    if (mFeaturedList.size() > 0) {
                        mLytFeatured.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    Log.e("Constant ","response of feature post are "+response.code()+" \n message is "+response.headers());
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

    public void loadRecentPosts() {
        Log.e("TAG","inside loadRecentPosts");
        ApiUtilities.getApiInterface().getLatestPosts(mRecentPageNo).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                long a=response.raw().receivedResponseAtMillis();
                long b=response.raw().sentRequestAtMillis();
                long c=a-b;
                Log.e("TAG","response success"+c);

                if (response.isSuccessful())
                {
                  //  Log.e("TAG","response success");
                    if(mFeaturedList!=null)
                    {
                        mRecentPostList.addAll(response.body());
                       // mRecentPostList.removeAll(mFeaturedList);
                      //  mFeaturedList.removeAll(mRecentPostList);
                        for(int i=0;i<mFeaturedList.size();i++)
                        {
                            for(int j=0;j<mRecentPostList.size();j++)
                            {
                               if(mRecentPostList.get(j).getID().equals(mFeaturedList.get(i).getID()))
                                {
                                    mRecentPostList.remove(j);
                                }
                            }
                        }
                    }
                    else
                    {
                        mRecentPostList.addAll(response.body());
                    }
                    updateUI();
                    if(mLytRecent.getVisibility()==View.GONE) {
                        mLytRecent.setVisibility(View.VISIBLE);
                    }
                  //  hideLoader();
                    hideMoreItemLoader();
                }
                else {
                    Log.e("Constant ","response of recent  post are "+response.code()+" \n message is "+response.headers());
                    hideMoreItemLoader();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                Log.e("Constant"," failure of loadRecentPosts "+t);
                showEmptyView();
                mBottomLayout.setVisibility(View.GONE);
            }
        });
    }

    private void hideMoreItemLoader() {
        mBottomLayout.setVisibility(View.GONE);
        mUserScrolled = true;
    }

    private void implementScrollListener() {
        mRvPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
                if(changeView)
                {
                    mVisibleItemCount = mLayoutManager.getChildCount();
                    mTotalItemCount = mLayoutManager.getItemCount();
                    mPastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                    mSwipeRefreshLayout.setEnabled(mLayoutManager.findFirstCompletelyVisibleItemPosition()==0);
                }
                else
                {
                    mVisibleItemCount = mGridLayoutManager.getChildCount();
                    mTotalItemCount = mGridLayoutManager.getItemCount();
                    mPastVisibleItems = mGridLayoutManager.findFirstVisibleItemPosition();
                    mSwipeRefreshLayout.setEnabled(mGridLayoutManager.findFirstCompletelyVisibleItemPosition()==0);
                }

    if(dy>0)
            {
           if (mUserScrolled && (mVisibleItemCount + mPastVisibleItems) == mTotalItemCount) {
                 mUserScrolled = false;
                    //  updateRecyclerView();
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
        Log.e("inside"," the updateRecyclerView");
        mBottomLayout.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecentPageNo++;
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

        for (int i = 0; i < mRecentPostList.size(); i++) {
            boolean isBookmarkSet = false;
            for (int j = 0; j < mBookmarkList.size(); j++)
            {
                if (mRecentPostList.get(i).getID() == mBookmarkList.get(j).getPostId())
                {
                    mRecentPostList.get(i).setBookmark(true);
                    isBookmarkSet = true;
                    break;
                }
            }
            if (!isBookmarkSet)
            {
                mRecentPostList.get(i).setBookmark(false);
            }
        }

        if (mRecentPostList.size() == 0)
        {
            showEmptyView();
        }
        else {
            mRecentAdapter.notifyDataSetChanged();
            hideLoader();
        }
    }
}
