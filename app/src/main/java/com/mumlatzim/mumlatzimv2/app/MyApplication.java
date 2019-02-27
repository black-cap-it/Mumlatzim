package com.mumlatzim.mumlatzimv2.app;

import android.app.Application;
import android.util.Log;

import com.mumlatzim.mumlatzimv2.notification.CustomNotificationOpening;
import com.mumlatzim.mumlatzimv2.notification.CustomNotificationReceived;
import com.onesignal.OneSignal;

public class MyApplication extends Application {

    private static MyApplication instance;
    public static MyApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();

       // FirebaseMessaging.getInstance().subscribeToTopic("wpnewzappnotification");
        Log.e("Constant","inside oneSignal");
        // OneSignal Initialization
        OneSignal.startInit(this)
                .setNotificationReceivedHandler(new CustomNotificationReceived())
                .setNotificationOpenedHandler(new CustomNotificationOpening())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        instance = this;
    }
}
