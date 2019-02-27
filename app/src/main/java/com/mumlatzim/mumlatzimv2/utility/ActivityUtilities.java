package com.mumlatzim.mumlatzimv2.utility;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mumlatzim.mumlatzimv2.R;
import com.mumlatzim.mumlatzimv2.data.constant.AppConstant;
import com.mumlatzim.mumlatzimv2.models.category.Category;
import com.mumlatzim.mumlatzimv2.models.category.Tags;
import com.mumlatzim.mumlatzimv2.models.post.Post;

import java.util.ArrayList;

public class ActivityUtilities {

    private static ActivityUtilities sActivityUtilities = null;

    public static ActivityUtilities getInstance() {
        if (sActivityUtilities == null) {
            sActivityUtilities = new ActivityUtilities();
        }
        return sActivityUtilities;
    }

    public void invokeNewActivity(Activity activity, Class<?> tClass, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokeRecentActivity(Activity activity, Class<?> tClass, ArrayList<Post> mFeaturedList, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(AppConstant.BUNDLE_KEY_FEATURE_LIST, mFeaturedList);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }


    public void subCategoryListActivity(Activity activity, Class<?> tClass, int clickedCategoryId, String categoryName, ArrayList<Category> categoryList, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(AppConstant.BUNDLE_KEY_CATEGORY_ID, clickedCategoryId);
        intent.putExtra(AppConstant.BUNDLE_KEY_CATEGORY_NAME, categoryName);
        intent.putExtra(AppConstant.BUNDLE_KEY_CATEGORY_LIST, categoryList);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void subTagsListActivity(Activity activity, Class<?> tClass, int clickedTagsId, String tagsName, ArrayList<Tags> mTagsList, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(AppConstant.BUNDLE_KEY_TAGS_ID, clickedTagsId);
        intent.putExtra(AppConstant.BUNDLE_KEY_TAGS_NAME, tagsName);
        intent.putExtra(AppConstant.BUNDLE_KEY_TAGS_LIST, mTagsList);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void postTagListActivity(Activity activity, Class<?> tClass, int clickedTagsId, String tagsName, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(AppConstant.BUNDLE_KEY_TAGS_ID, clickedTagsId);
        intent.putExtra(AppConstant.BUNDLE_KEY_TAGS_NAME, tagsName);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokeCustomUrlActivity(Activity activity, Class<?> tClass, String pageTitle, String pageUrl, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(AppConstant.BUNDLE_KEY_TITLE, pageTitle);
        intent.putExtra(AppConstant.BUNDLE_KEY_URL, pageUrl);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokePostDetailsActivity(Activity activity, Class<?> tClass, int clickedPostId, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(AppConstant.BUNDLE_KEY_POST_ID, clickedPostId);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokeCommentListActivity(Activity activity, Class<?> tClass, int clickedPostId, String commentsLink, boolean shouldDialogOpen, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(AppConstant.BUNDLE_KEY_POST_ID, clickedPostId);
        intent.putExtra(AppConstant.BUNDLE_KEY_COMMENTS_LINK, commentsLink);
        intent.putExtra(AppConstant.BUNDLE_KEY_DIALOG_OPTION, shouldDialogOpen);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokeBrowser(Activity activity,String BrowserUrl)
    {
        try {
            Uri webpage = Uri.parse(BrowserUrl.trim());
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            activity.startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, R.string.noBrowser,  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void invokeCouponClipBoard(Activity activity,String Text)
    {
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Text", Text);
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
        Toast.makeText(activity, R.string.coupon_copied, Toast.LENGTH_SHORT).show();
    }

    public void PostReport(Activity activity,String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder=  new AlertDialog.Builder(activity,R.style.AlertDialogStyle);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.report_yes, onClickListener);
        builder.setNegativeButton(R.string.report_no, null);
        AlertDialog dialog=  builder.create();
        dialog.show();
        // Get the alert dialog buttons reference
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        positiveButton.setBackground(activity.getResources().getDrawable(R.drawable.rectangle_red));
        negativeButton.setBackground(activity.getResources().getDrawable(R.drawable.rectangle_red));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(20,0,0,0);
        positiveButton.setLayoutParams(params);
    }
}
