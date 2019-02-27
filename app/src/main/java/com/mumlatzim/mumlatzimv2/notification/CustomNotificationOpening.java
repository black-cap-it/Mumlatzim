package com.mumlatzim.mumlatzimv2.notification;

import android.content.Intent;
import android.util.Log;

import com.mumlatzim.mumlatzimv2.activity.MainActivity;
import com.mumlatzim.mumlatzimv2.app.MyApplication;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class CustomNotificationOpening implements OneSignal.NotificationOpenedHandler {

  /*  @Override
    public void notificationOpened(OSNotificationOpenResult notification) {
       // notification.notification.payload.additionalData.names();
      //  JSONObject data = notification.notification.payload.additionalData;

        Intent intent = new Intent(MyApplication.getInstance(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra("paymentModel", data);
        MyApplication.getInstance().startActivity(intent);


    }
    */
  @Override
  public void notificationOpened(OSNotificationOpenResult result) {
      OSNotificationAction.ActionType actionType = result.action.type;
      JSONObject data = result.notification.payload.additionalData;
      String customKey;

      if (data != null) {
          customKey = data.optString("customkey", null);
          if (customKey != null)
              Log.i("OneSignalExample", "customkey set with value: " + customKey);
      }

      if (actionType == OSNotificationAction.ActionType.ActionTaken)
          Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

      // The following can be used to open an Activity of your choice.
      // Replace - getApplicationContext() - with any Android Context.
       Intent intent = new Intent(MyApplication.getInstance(), MainActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      MyApplication.getInstance().startActivity(intent);

      // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
      //   if you are calling startActivity above.
     /*
        <application ...>
          <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
        </application>
     */
  }
}
