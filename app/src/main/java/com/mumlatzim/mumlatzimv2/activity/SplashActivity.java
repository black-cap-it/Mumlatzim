package com.mumlatzim.mumlatzimv2.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mumlatzim.mumlatzimv2.R;
import com.mumlatzim.mumlatzimv2.utility.ActivityUtilities;


public class SplashActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;
    private ImageView mImageView;
    private Animation mAnimation_1;
    private ProgressBar mProgressBar;
    private RelativeLayout mRootLayout;

    // Constants
    private static final int SPLASH_DURATION = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initVar();
        initView();
    }

    private void initVar() {
        mContext = getApplicationContext();
       // mActivity = SplashActivity.this;
    }

    private void initView() {
        setContentView(R.layout.activity_splash);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRootLayout = (RelativeLayout) findViewById(R.id.splashBody);
        mImageView = (ImageView) findViewById(R.id.splashIcon);
       // mAnimation_1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUtilities.getInstance().invokeNewActivity(mActivity, MainActivity.class, true);
            }
        },SPLASH_DURATION);

    }

  /*  private void initFunctionality() {
        if (AppUtilities.isNetworkAvailable(mContext)) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(View.GONE);
                    mImageView.startAnimation(mAnimation_1);
                    mAnimation_1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            ActivityUtilities.getInstance().invokeNewActivity(mActivity, MainActivity.class, true);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }, SPLASH_DURATION);
        }
        else {
            AppUtilities.noInternetWarning(mRootLayout, mContext);
        }
    }


      @Override
    protected void onResume() {
        super.onResume();
        initFunctionality();
    }
    */
}

