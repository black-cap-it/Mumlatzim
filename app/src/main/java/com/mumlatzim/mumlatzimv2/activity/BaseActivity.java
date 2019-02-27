package com.mumlatzim.mumlatzimv2.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.mumlatzim.mumlatzimv2.R;
import com.mumlatzim.mumlatzimv2.data.constant.AppConstant;
import com.mumlatzim.mumlatzimv2.models.category.Category;
import com.mumlatzim.mumlatzimv2.utility.ActivityUtilities;
import com.mumlatzim.mumlatzimv2.utility.AdsUtilities;
import com.mumlatzim.mumlatzimv2.utility.AppUtilities;
import com.mumlatzim.mumlatzimv2.utility.DialogUtilities;

import java.util.ArrayList;


public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DialogUtilities.OnCompleteListener {

    private Context mContext;
    private Activity mActivity;
    private ActionBarDrawerToggle toggle;
    private Toolbar mToolbar;
    private boolean fb;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private LinearLayout mLoadingView, mNoDataView;
    private ArrayList<Category> mCategoryList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = BaseActivity.this;
        mContext = mActivity.getApplicationContext();
        // uncomment this line to disable ads from entire application
        //disableAds();
    }

    public NavigationView getNavigationView() {
        return mNavigationView;
    }

    public void initDrawer() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

         toggle = new ActionBarDrawerToggle
                ( this,mDrawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);

        mNavigationView = (NavigationView) findViewById(R.id.navigationView);
        mNavigationView.setItemIconTintList(null);
        getNavigationView().setNavigationItemSelectedListener(this);
    }



    public void initToolbar(boolean isTitleEnabled) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(isTitleEnabled);
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void enableUpButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }


    public void initLoader() {
        mLoadingView = (LinearLayout) findViewById(R.id.loadingView);
        mNoDataView = (LinearLayout) findViewById(R.id.noDataView);
    }

    public void openDrawer()
    {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        }
        else {
            mDrawerLayout.openDrawer(GravityCompat.END);
        }
    }

    public void showLoader() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
        }

        if (mNoDataView != null) {
            mNoDataView.setVisibility(View.GONE);
        }
    }

    public void hideLoader() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mNoDataView != null) {
            mNoDataView.setVisibility(View.GONE);
        }
    }

    public void showEmptyView() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mNoDataView != null) {
            mNoDataView.setVisibility(View.VISIBLE);
        }
    }



    private void disableAds() {
        AdsUtilities.getInstance(mContext).disableBannerAd();
        AdsUtilities.getInstance(mContext).disableInterstitialAd();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Menu m=mNavigationView.getMenu();
        // main items
        if (id == R.id.action_home) {
            ActivityUtilities.getInstance().invokeNewActivity(mActivity, MainActivity.class, true);
        }
        else if (id == R.id.action_categories)
        {
           // ActivityUtilities.getInstance().invokeNewActivity(mActivity, CategoryListActivity.class, false);
            boolean b=!m.findItem(R.id.action_electronics).isVisible();

            if(b)
            {
                item.setIcon(getResources().getDrawable(R.drawable.ic_hidecategory_icon));
            }
            else
            {
                item.setIcon(getResources().getDrawable(R.drawable.ic_addcategory_icon));
            }

            m.findItem(R.id.action_electronics).setVisible(b);
            m.findItem(R.id.action_shoes).setVisible(b);
            m.findItem(R.id.action_wallets).setVisible(b);
            m.findItem(R.id.action_football).setVisible(b);
            m.findItem(R.id.action_animals).setVisible(b);
            m.findItem(R.id.action_clothes).setVisible(b);
            m.findItem(R.id.action_hats).setVisible(b);
            m.findItem(R.id.action_tools).setVisible(b);
            m.findItem(R.id.action_general).setVisible(b);
            m.findItem(R.id.action_homeproducts).setVisible(b);
            m.findItem(R.id.action_coats).setVisible(b);
            m.findItem(R.id.action_family).setVisible(b);
            m.findItem(R.id.action_sunglasses).setVisible(b);
            m.findItem(R.id.action_watch).setVisible(b);
            m.findItem(R.id.action_bags).setVisible(b);
            m.findItem(R.id.action_jewellery).setVisible(b);
            m.findItem(R.id.action_general).setVisible(b);
            m.findItem(R.id.action_homeproducts).setVisible(b);
            m.findItem(R.id.action_coats).setVisible(b);
            m.findItem(R.id.action_family).setVisible(b);
            m.findItem(R.id.action_sunglasses).setVisible(b);
            m.findItem(R.id.action_shoes).setVisible(b);
            m.findItem(R.id.action_watch).setVisible(b);
            m.findItem(R.id.action_bags).setVisible(b);
            m.findItem(R.id.action_jewellery).setVisible(b);

             if(!m.findItem(R.id.action_clothes).isVisible())
            {
                m.findItem(R.id.action_menclothes).setVisible(false);
                m.findItem(R.id.action_womenclothes).setVisible(false);
                m.findItem(R.id.action_childrenclothes).setVisible(false);
            }
        }

        else if(id==R.id.action_electronics)
        {
            ActivityUtilities.getInstance().subCategoryListActivity(mActivity, SubCategoryListActivity.class, Integer.parseInt(getString(R.string.electronicsID)), getString(R.string.electronics), mCategoryList, false);
        }
        else if(id==R.id.action_shoes)
        {
            Log.e("TAG","categories lis are "+mCategoryList);
            ActivityUtilities.getInstance().subCategoryListActivity(mActivity, SubCategoryListActivity.class, Integer.parseInt(getString(R.string.shoesID)), getString(R.string.shoes), mCategoryList, false);
        }

        else if(id==R.id.action_clothes)
        {
            boolean c=!m.findItem(R.id.action_menclothes).isVisible();
            m.findItem(R.id.action_menclothes).setVisible(c);
            m.findItem(R.id.action_womenclothes).setVisible(c);
            m.findItem(R.id.action_childrenclothes).setVisible(c);
        }

        else if (id==R.id.action_menclothes)
        {
            ActivityUtilities.getInstance().subCategoryListActivity(mActivity, SubCategoryListActivity.class, Integer.parseInt(getString(R.string.men_clothesID)), getString(R.string.men_clothes), mCategoryList, false);
        }

        else if (id==R.id.action_womenclothes)
        {
            ActivityUtilities.getInstance().subCategoryListActivity(mActivity, SubCategoryListActivity.class, Integer.parseInt(getString(R.string.women_clothesID)), getString(R.string.women_clothes), mCategoryList, false);
        }

        else if (id==R.id.action_childrenclothes)
        {
            ActivityUtilities.getInstance().subCategoryListActivity(mActivity, SubCategoryListActivity.class, Integer.parseInt(getString(R.string.children_clothesID)), getString(R.string.children_clothes), mCategoryList, false);
        }

        else if(id==R.id.action_wallets)
        {
            ActivityUtilities.getInstance().subCategoryListActivity(mActivity, SubCategoryListActivity.class, Integer.parseInt(getString(R.string.walletsID)), getString(R.string.wallets), mCategoryList, false);
        }

        else if(id==R.id.action_coats)
        {
            ActivityUtilities.getInstance().subCategoryListActivity(mActivity, SubCategoryListActivity.class, Integer.parseInt(getString(R.string.coatsID)), getString(R.string.coats), mCategoryList, false);
        }

        else if (id==R.id.action_football)
        {
            ActivityUtilities.getInstance().subCategoryListActivity(mActivity, SubCategoryListActivity.class, Integer.parseInt(getString(R.string.footballID)), getString(R.string.football), mCategoryList, false);
        }

        else if(id==R.id.action_hats)
        {
          ActivityUtilities.getInstance().subCategoryListActivity(mActivity, SubCategoryListActivity.class, Integer.parseInt(getString(R.string.hatsID)), getString(R.string.hats), mCategoryList, false);
        }

        else if(id==R.id.action_tools)
        {
            ActivityUtilities.getInstance().subCategoryListActivity(mActivity, SubCategoryListActivity.class, Integer.parseInt(getString(R.string.toolsID)), getString(R.string.tools), mCategoryList, false);
        }

        else if(id==R.id.action_animals)
        {
            ActivityUtilities.getInstance().subCategoryListActivity(mActivity, SubCategoryListActivity.class, Integer.parseInt(getString(R.string.animalsID)), getString(R.string.animals), mCategoryList, false);
        }

        else if (id==R.id.action_general)
        {
            ActivityUtilities.getInstance().subCategoryListActivity(mActivity, SubCategoryListActivity.class, Integer.parseInt(getString(R.string.generalID)), getString(R.string.general), mCategoryList, false);
        }

        else if (id==R.id.action_homeproducts)
        {
            ActivityUtilities.getInstance().subCategoryListActivity(mActivity, SubCategoryListActivity.class, Integer.parseInt(getString(R.string.home_productsID)), getString(R.string.home_products), mCategoryList, false);
        }

        else if(id==R.id.action_family)
        {
            ActivityUtilities.getInstance().subCategoryListActivity(mActivity, SubCategoryListActivity.class, Integer.parseInt(getString(R.string.familyID)), getString(R.string.family), mCategoryList, false);
        }

        else if(id==R.id.action_sunglasses)
        {
            ActivityUtilities.getInstance().subCategoryListActivity(mActivity, SubCategoryListActivity.class, Integer.parseInt(getString(R.string.sunglassesID)), getString(R.string.sunglasses), mCategoryList, false);
        }

        else if(id==R.id.action_watch)
        {
            ActivityUtilities.getInstance().subCategoryListActivity(mActivity, SubCategoryListActivity.class, Integer.parseInt(getString(R.string.watchID)), getString(R.string.watch), mCategoryList, false);
        }

        else if(id==R.id.action_bags)
        {
            ActivityUtilities.getInstance().subCategoryListActivity(mActivity, SubCategoryListActivity.class, Integer.parseInt(getString(R.string.bagsID)), getString(R.string.bags), mCategoryList, false);
        }

        else if(id==R.id.action_jewellery)
        {
            ActivityUtilities.getInstance().subCategoryListActivity(mActivity, SubCategoryListActivity.class, Integer.parseInt(getString(R.string.jewelleryID)), getString(R.string.jewellery), mCategoryList, false);
        }

        else if(id==R.id.action_tag)
        {
            ActivityUtilities.getInstance().invokeNewActivity(mActivity, TagListActivity.class, false);
        }

        else if (id == R.id.action_book)
        {
            ActivityUtilities.getInstance().invokeNewActivity(mActivity, BookmarkListActivity.class, false);
        }

        else if(id== R.id.action_order)
        {
            ActivityUtilities.getInstance().invokeNewActivity(mActivity, OrderedListActivity.class, false);
        }

        else if (id == R.id.action_settings)
        {
            ActivityUtilities.getInstance().invokeNewActivity(mActivity, SettingsActivity.class, false);
        }
        // social

        else if (id == R.id.action_facebook)
        {
            fb=!m.findItem(R.id.action_israel).isVisible();
            m.findItem(R.id.action_israel).setVisible(fb);
            m.findItem(R.id.action_digital).setVisible(fb);
            m.findItem(R.id.action_fbfamily).setVisible(fb);
            m.findItem(R.id.action_israelgroup).setVisible(fb);
        }


        else if(id==R.id.action_israel)
        {
            AppUtilities.faceBookLink(mActivity);
        }

        else if(id==R.id.action_digital)
        {
            AppUtilities.faceBookDigitalLink(mActivity);
        }

        else if(id==R.id.action_fbfamily)
        {
            AppUtilities.faceBookfamilyLink(mActivity);
        }

        else if(id==R.id.action_israelgroup)
        {
            AppUtilities.faceBookgroupLink(mActivity);
        }

        else if(id==R.id.action_telegram)
        {
            AppUtilities.telegramLink(mActivity);
        }

        else if(id==R.id.action_chat)
        {
            AppUtilities.MessangerLink(mActivity);
        }
        // others
        else if (id == R.id.action_share)
        {
            AppUtilities.shareApp(mActivity);
        }

        else if (id == R.id.action_rate_app)
        {
            AppUtilities.rateThisApp(mActivity); // this feature will only work after publish the app
        }

        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }

        return true;

    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    public void onComplete(Boolean isOkPressed, String viewIdText)
    {
        if (isOkPressed)
        {
            if (viewIdText.equals(AppConstant.BUNDLE_KEY_EXIT_OPTION))
            {
                mActivity.finishAffinity();
            }
        }
    }

}