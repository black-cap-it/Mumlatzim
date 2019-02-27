package com.mumlatzim.mumlatzimv2.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdView;
import com.mumlatzim.mumlatzimv2.R;
import com.mumlatzim.mumlatzimv2.adapters.TagsAdapter;
import com.mumlatzim.mumlatzimv2.api.ApiUtilities;
import com.mumlatzim.mumlatzimv2.api.HttpParams;
import com.mumlatzim.mumlatzimv2.listeners.ListItemClickListener;
import com.mumlatzim.mumlatzimv2.models.category.Tags;
import com.mumlatzim.mumlatzimv2.utility.ActivityUtilities;
import com.mumlatzim.mumlatzimv2.utility.AdsUtilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagListActivity extends BaseActivity {
    private static final String TAG = "constants";
    private Activity mActivity;
    private Context mContext;

    private ArrayList<Tags> mTagsList;
    private ArrayList<Tags> mTagsChildList;
    private TagsAdapter mTagsAdapter = null;
    private RecyclerView mRvPosts,rvCategories;
    private int mItemCount = 5;
    private boolean mUserScrolled = true;
    private int mPageNo=1,mVisibleItemCount,mTotalItemCount,mPastVisibleItems;
    private LinearLayoutManager mLayoutManager;
    private RelativeLayout  mBottomLayout;

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
        mActivity = TagListActivity.this;
        mContext = mActivity.getApplicationContext();
        mTagsList = new ArrayList<>();
        mTagsChildList= new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_category_list);
       // mRvPosts = (RecyclerView) findViewById(R.id.rvPosts);
        rvCategories=(RecyclerView)findViewById(R.id.rvPosts);
        //rvCategories.setVisibility(View.GONE);
        mLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCategories.setLayoutManager(mLayoutManager);
        mTagsAdapter = new TagsAdapter(mContext,(ArrayList<Tags>) mTagsList);
        rvCategories.setAdapter(mTagsAdapter);
        mBottomLayout = (RelativeLayout) findViewById(R.id.rv_itemload);
        initToolbar(true);
        setToolbarTitle(getString(R.string.tag));
        enableUpButton();
        initLoader();
    }

    private void initFunctionality() {

        showLoader();

        loadCategories();

        // show full-screen ads
       // AdsUtilities.getInstance(mContext).showFullScreenAd();
        // show banner ads
        AdsUtilities.getInstance(mContext).showBannerAd((AdView) findViewById(R.id.adsView));
    }

    private void initListener() {
        mTagsAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Tags model = mTagsList.get(position);
                switch (view.getId()) {
                    case R.id.lyt_container:
                        ActivityUtilities.getInstance().subTagsListActivity(mActivity, SubTagListActivity.class, model.getmID().intValue(), model.getmName(), (ArrayList<Tags>) mTagsList, false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void loadCategories() {
        ApiUtilities.getApiInterface().getTags(mPageNo).enqueue(new Callback<List<Tags>>() {
            @Override
            public void onResponse(Call<List<Tags>> call, Response<List<Tags>> response) {
                if (response.isSuccessful()) {

                    int totalPages = Integer.parseInt(response.headers().get(HttpParams.TOTAL_PAGE));
                   // Log.e("total pages are "," "+totalPages+" \n"+response.headers()+" mItemCount"+mItemCount);
                    mTagsList.addAll(response.body());
                    updateUI();
                   // hideLoader();
                    hideMoreItemLoader();
                }
                else
                {
                    hideMoreItemLoader();
                }
            }

            @Override
            public void onFailure(Call<List<Tags>> call, Throwable t) {
                t.printStackTrace();
                showEmptyView();
                mBottomLayout.setVisibility(View.GONE);
            }
        });
    }

    private void updateUI() {
        if (mTagsList.size() == 0) {
            showEmptyView();
        } else {
            mTagsAdapter.notifyDataSetChanged();
            hideLoader();
        }
    }

    private void hideMoreItemLoader() {
        mBottomLayout.setVisibility(View.GONE);
        mUserScrolled = true;
    }

    private void implementScrollListener() {
        rvCategories.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
        Log.e("inside"," recyclerView");
        mBottomLayout.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mPageNo++;
                loadCategories();

            }
        }, 0);

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
