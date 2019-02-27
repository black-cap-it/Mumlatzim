package com.mumlatzim.mumlatzimv2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.mumlatzim.mumlatzimv2.R;
import com.mumlatzim.mumlatzimv2.adapters.OrderedAdapter;
import com.mumlatzim.mumlatzimv2.data.constant.AppConstant;
import com.mumlatzim.mumlatzimv2.data.sqlite.OrderedDbController;
import com.mumlatzim.mumlatzimv2.listeners.OrderItemClickListener;
import com.mumlatzim.mumlatzimv2.models.bookmark.OrderModel;
import com.mumlatzim.mumlatzimv2.utility.ActivityUtilities;
import com.mumlatzim.mumlatzimv2.utility.AdsUtilities;
import com.mumlatzim.mumlatzimv2.utility.DialogUtilities;
import com.mumlatzim.mumlatzimv2.utility.OrderedListUtilities;

import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.content.ContentValues.TAG;

public class OrderedListActivity extends BaseActivity {

    private Activity mActivity;
    static final String API_KEY ="a3a6bf12-86ef-408f-9f99-e3faee4dfd0f";
    private Context mContext;
    String  tracking_number;
    public static final int SD_REQUEST=1;
    private static final int PERMISSION_REQUEST_CODE = 200,PICK_IMAGE_REQUEST=3;
    public  byte[] byteArray = new byte[0];

    private TextView screenshot_textView;

    private ArrayList<OrderModel> mOrderList;
    private OrderedAdapter mOrderedAdapter = null;
    private RecyclerView mRecycler;

    private OrderedDbController mOrderedDbController;
    private MenuItem mMenuItemDeleteAll;
    private int mAdapterPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar();
        initView();
        initFunctionality();
        initListener();
    }

    private void initVar() {
        mActivity = OrderedListActivity.this;
        mContext = mActivity.getApplicationContext();
        mOrderList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_bookmark);

        mRecycler = (RecyclerView) findViewById(R.id.rvBookmark);
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mOrderedAdapter = new OrderedAdapter(mContext,mActivity, mOrderList);
        mRecycler.setAdapter(mOrderedAdapter);

        initToolbar(true);
        setToolbarTitle(getString(R.string.site_menu_ordered));
        enableUpButton();
        initLoader();
    }

    private void initFunctionality() {

        // show full-screen ads
        AdsUtilities.getInstance(mContext).showFullScreenAd();
        // show banner ads
        AdsUtilities.getInstance(mContext).showBannerAd((AdView) findViewById(R.id.adsView));
    }

    public void updateUI() {
        showLoader();

        if (mOrderedDbController == null) {
            mOrderedDbController = new OrderedDbController(mContext);
        }
        mOrderList.clear();
        mOrderList.addAll(mOrderedDbController.getAllData());

        mOrderedAdapter.notifyDataSetChanged();

        hideLoader();

        if (mOrderList.size() == 0) {
            showEmptyView();
            if (mMenuItemDeleteAll != null) {
                mMenuItemDeleteAll.setVisible(false);
            }
        }
        else {
            if (mMenuItemDeleteAll != null) {
                mMenuItemDeleteAll.setVisible(true);
            }
        }
    }

    public void initListener() {
        // recycler list item click listener
        mOrderedAdapter.setItemClickListener(new OrderItemClickListener() {
            @Override
            public void onOrderClick(int position, View view, TextView trackButton, ImageButton btn_order_edit) {
                mAdapterPosition = position;
                OrderModel model = mOrderList.get(position);
                switch (view.getId()) {
                    case R.id.btn_book:
                        FragmentManager manager = getSupportFragmentManager();
                        DialogUtilities dialog = DialogUtilities.newInstance(getString(R.string.site_menu_ordered), getString(R.string.delete_fav_item), getString(R.string.yes), getString(R.string.no), AppConstant.BUNDLE_KEY_DELETE_EACH_BOOKMARK);
                        dialog.show(manager, AppConstant.BUNDLE_KEY_DIALOG_FRAGMENT);
                        break;
                    case R.id.btn_share:
                        final String appPackageName = mActivity.getPackageName();
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(model.getPostUrl())
                                + AppConstant.EMPTY_STRING
                                );
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                        break;
                    case R.id.lyt_container:
                        ActivityUtilities.getInstance().invokePostDetailsActivity(mActivity, PostDetailsActivity.class, model.getPostId(), false);
                        break;

                    case R.id.btn_order_edit:
                        OrderedListUtilities.edit_alertShow(mActivity,trackButton,btn_order_edit,mOrderedDbController,mOrderList.get(mAdapterPosition).getPostId());
                        break;
                    case R.id.track_number:

                        if(!trackButton.getText().equals(getResources().getString(R.string.track_no)))
                        {
                            String order_trackNumber=trackButton.getText().toString();
                            ActivityUtilities.getInstance().invokeBrowser(mActivity,AppConstant.Track17_url+order_trackNumber);
                            //new ConnectionAPI(API_KEY, ConnectionAPIMethods.getTrackingByNumber, OrderedListActivity.this,tracking1).execute();
                        }
                        else
                        {
                            OrderedListUtilities.edit_alertShow(mActivity,trackButton,btn_order_edit,mOrderedDbController,mOrderList.get(mAdapterPosition).getPostId());
                        }

                        break;
                    case R.id.screenshot:
                        Log.e("Constant","screenshot inside");
                        screenshot_textView=(TextView) view.findViewById(R.id.screenshot);
                        if(mOrderList.get(mAdapterPosition).getPostScreenshot()!=null && !mOrderList.get(mAdapterPosition).getPostScreenshot().isEmpty())
                        {
                            OrderedListUtilities.alertShow(mActivity,mOrderList.get(mAdapterPosition).getPostScreenshot());
                        }
                        else
                        {
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            {
                                if(checkPermission())
                                {
                                    showFileChooser();
                                }
                                else
                                {
                                    requestPermission();
                                }
                            }
                            else
                            {
                                showFileChooser();
                            }
                        }

                        break;
                    default:
                        break;
                }
            }
        });
    }

    private boolean checkPermission() //checkDangerous permission is granted or not on Choose image button
    {
        int result = ContextCompat.checkSelfPermission(mActivity.getApplicationContext(), READ_EXTERNAL_STORAGE);
        return  result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission()//if checkPermission not granted  then  request for permssion
    {
        Log.e(TAG,"requestPermission");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                Log.e(TAG,"inside onRequest");
                if (grantResults.length > 0) {
                    boolean readExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if ( readExternalStorage) {
                        showFileChooser();
                    }
                    else {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                                showDialogOKCancel(getString(R.string.message_permission),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;

                            }
                        }
                    }
                }
                break;
        }
    }

    private void showDialogOKCancel(String message, DialogInterface.OnClickListener okListener) //AlertDilaog called if no permssion is granted showing MESSAGE
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity,R.style.AlertDialogStyle);
                dialogBuilder.setIcon(R.mipmap.ic_launcher);
        dialogBuilder.setTitle(R.string.app_name);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton(R.string.alert_ok, okListener);
        dialogBuilder.setNegativeButton(R.string.cancel_button, null);
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

    private void showFileChooser() {

        startActivityForResult(getPickImageChooserIntent(),PICK_IMAGE_REQUEST);
    }

    private Intent getPickImageChooserIntent() {
        Intent sdintent = new Intent(Intent.ACTION_PICK);
        sdintent.setType("image/*");
        return sdintent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null) {
            Uri selectedImage = data.getData();



            if (null != selectedImage) {

                // Saving to Database...
                if (saveImageInDB(selectedImage)) {
                    //showMessage("Image Saved in Database...");
                    Toast.makeText(mActivity, "Image selected saved", Toast.LENGTH_SHORT).show();
                   // imgView.setImageURI(selectedImageUri);
                }
            }
            // alertShow(yourSelectedImage,byteArray);
        }
    }

    Boolean saveImageInDB(Uri selectedImageUri) {

        try {
            String realPath= getRealPathFromURI(selectedImageUri);
            Log.e("realPath ogf image is ",""+realPath);
           /* Bitmap bitmapImage = BitmapFactory.decodeFile(realPath);
            int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
            byte[] byteArray = OrderedListUtilities.getBytes(scaled);
            */
            mOrderedDbController.updateScreenshot(mOrderList.get(mAdapterPosition).getPostId(),realPath);
            return true;
        }
        catch (Exception e) {
            Log.e(TAG, "<saveImageInDB> Error : " + e.getLocalizedMessage());
            return false;
        }

      /*  try {
            InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
            byte[] byteArray = OrderedListUtilities.getBytes(iStream);
            mOrderedDbController.updateScreenshot(mBookmarkList.get(mAdapterPosition).getPostId(),byteArray);
            return true;
        }
        catch (IOException ioe)
        {
            Log.e(TAG, "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
            return false;
        }
        */
    }

    private String getRealPathFromURI(Uri contentURI) {
        String filePath;
        Cursor cursor = this.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            filePath = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            filePath = cursor.getString(idx);
            cursor.close();
        }
        return filePath;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menus_delete_all:
                FragmentManager manager = getSupportFragmentManager();
                DialogUtilities dialog = DialogUtilities.newInstance(getString(R.string.site_menu_book), getString(R.string.delete_all_fav_item), getString(R.string.yes), getString(R.string.no), AppConstant.BUNDLE_KEY_DELETE_ALL_BOOKMARK);
                dialog.show(manager, AppConstant.BUNDLE_KEY_DIALOG_FRAGMENT);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete_all, menu);
        mMenuItemDeleteAll = menu.findItem(R.id.menus_delete_all);

        updateUI();

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mOrderList.size() > 0) {
            updateUI();
        }
        // load full screen ad
        AdsUtilities.getInstance(mContext).loadFullScreenAd(mActivity);
    }

    @Override
    public void onComplete(Boolean isOkPressed, String viewIdText) {
        if (isOkPressed) {
            if (viewIdText.equals(AppConstant.BUNDLE_KEY_DELETE_ALL_BOOKMARK)) {
                mOrderedDbController.deleteAllFav();
                updateUI();
            }
            else if (viewIdText.equals(AppConstant.BUNDLE_KEY_DELETE_EACH_BOOKMARK)) {
                mOrderedDbController.deleteEachFav(mOrderList.get(mAdapterPosition).getPostId());
                updateUI();
            }
        }
    }
}
