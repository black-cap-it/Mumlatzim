package com.mumlatzim.mumlatzimv2.DrawerToggle;

import android.app.Activity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class EndMenuDrawerToggle implements DrawerLayout.DrawerListener{
    private final DrawerLayout mdrawerLayout;
    private final Toolbar mtoolbar;
    private Activity activity;
    private final DrawerArrowDrawable arrowDrawable;
    private final int openDrawer, closeDrawer;
    private MenuItem toggleItem;

    public EndMenuDrawerToggle(Activity activity,DrawerLayout mdrawerLayout, Toolbar mtoolbar,
                               int openDrawer, int closeDrawer) {
        this.mdrawerLayout = mdrawerLayout;
        this.mtoolbar = mtoolbar;

        this.openDrawer = openDrawer;
        this.closeDrawer = closeDrawer;

        arrowDrawable = new DrawerArrowDrawable(mtoolbar.getContext());
        arrowDrawable.setDirection(DrawerArrowDrawable.ARROW_DIRECTION_END);
    }

    public void setToggleOnMenu(Menu menu) {
        toggleItem = menu.add(openDrawer);
        toggleItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        toggleItem.setIcon(arrowDrawable);
        toggleItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                                  @Override
                                                  public boolean onMenuItemClick(MenuItem item) {
                                                      toggle();
                                                      return true;
                                                  }
                                              }
        );

        setPosition(mdrawerLayout.isDrawerOpen(GravityCompat.END) ? 1f : 0f);
    }

    private void toggle() {
        final int drawerLockMode = mdrawerLayout.getDrawerLockMode(GravityCompat.END);
        if (mdrawerLayout.isDrawerVisible(GravityCompat.END)
                && (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_OPEN)) {
            mdrawerLayout.closeDrawer(GravityCompat.END);
        }
        else if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            mdrawerLayout.openDrawer(GravityCompat.END);
        }
    }

    private void setPosition(float position) {
        if (position == 1f) {
            arrowDrawable.setVerticalMirror(true);
            toggleItem.setTitle(closeDrawer);
        }
        else if (position == 0f) {
            arrowDrawable.setVerticalMirror(false);
            toggleItem.setTitle(openDrawer);
        }
        arrowDrawable.setProgress(position);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        setPosition(Math.min(1f, Math.max(0f, slideOffset)));
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        setPosition(1f);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        setPosition(0f);
    }

    @Override
    public void onDrawerStateChanged(int newState) {}
}

