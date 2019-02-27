package com.mumlatzim.mumlatzimv2.utility;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mumlatzim.mumlatzimv2.R;
import com.mumlatzim.mumlatzimv2.data.sqlite.OrderedDbController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class OrderedListUtilities {

    public static byte[] getBytes(Bitmap inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        inputStream.compress(Bitmap.CompressFormat.JPEG,100,byteBuffer);
      /*  int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        */
        return byteBuffer.toByteArray();
    }

    private static Bitmap getImage(byte[] postScreenshot) {
        return BitmapFactory.decodeByteArray(postScreenshot, 0, postScreenshot.length);
    }

    public static void alertShow(Activity mActivity, String postScreenshot) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity);
// ...Irrelevant code for customizing the buttons and title
        dialogBuilder.setTitle(R.string.app_name);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.gallery_image, null);
        dialogBuilder.setView(dialogView);
        final ImageView gallery_imageView = (ImageView) dialogView.findViewById(R.id.gallery_imageView);
        final TextView image_notFound_textView= (TextView) dialogView.findViewById(R.id.image_notFound_textView);
        if(postScreenshot!=null && !postScreenshot.isEmpty())
        {

            Uri uri=Uri.parse(postScreenshot);
            File file=new File(uri.getPath());
            if(file.exists())
            {
                gallery_imageView.setImageURI(uri);
            }
            else
            {
                gallery_imageView.setBackgroundColor(mActivity.getResources().getColor(R.color.default_background));
                image_notFound_textView.setVisibility(View.VISIBLE);
            }
        }
        dialogBuilder.setPositiveButton(R.string.newImage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        // Get the alert dialog buttons reference
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setBackground(mActivity.getResources().getDrawable(R.drawable.rectangle_ordered));
        positiveButton.setTextColor(mActivity.getResources().getColor(R.color.white));
    }

    public static void edit_alertShow(final Activity mActivity, final TextView trackView, final ImageButton btn_order_edit, final OrderedDbController mOrderedDbController, final int postId)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity);
// ...Irrelevant code for customizing the buttons and title
        dialogBuilder.setTitle(R.string.tracking_title);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.ordered_dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText editText = (EditText) dialogView.findViewById(R.id.track_editText);
        if(!trackView.getText().equals(mActivity.getResources().getString(R.string.track_no)))
        {
            editText.setText(trackView.getText().toString());
        }
        dialogBuilder.setPositiveButton(R.string.add_to_trackButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

              String  tracking_number=editText.getText().toString();
                if(! tracking_number.isEmpty())
                {
                    mOrderedDbController.updateTrack(postId,tracking_number);
                    // mOrderedAdapter.notifyDataSetChanged();
                    trackView.setText(tracking_number);
                    if(btn_order_edit.getVisibility()==View.GONE)
                    {
                        btn_order_edit.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    trackView.setText(mActivity.getResources().getString(R.string.track_no));
                    btn_order_edit.setVisibility(View.GONE);
                }
                dialog.dismiss();
            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel_button,null);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        // Get the alert dialog buttons reference
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        positiveButton.setBackground(mActivity.getResources().getDrawable(R.drawable.rectangle_ordered));
        positiveButton.setTextColor(mActivity.getResources().getColor(R.color.white));
        negativeButton.setTextColor(mActivity.getResources().getColor(R.color.white));
        negativeButton.setBackground(mActivity.getResources().getDrawable(R.drawable.rectangle_ordered));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(20,0,0,0);
        positiveButton.setLayoutParams(params);
    }
}
