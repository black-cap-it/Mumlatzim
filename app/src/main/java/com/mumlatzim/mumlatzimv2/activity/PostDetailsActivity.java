package com.mumlatzim.mumlatzimv2.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdView;
import com.mumlatzim.mumlatzimv2.GlideApp.GlideApp;
import com.mumlatzim.mumlatzimv2.R;
import com.mumlatzim.mumlatzimv2.adapters.RelatedAdapter;
import com.mumlatzim.mumlatzimv2.api.ApiUtilities;
import com.mumlatzim.mumlatzimv2.api.HttpParams;
import com.mumlatzim.mumlatzimv2.data.constant.AppConstant;
import com.mumlatzim.mumlatzimv2.data.sqlite.BookmarkDbController;
import com.mumlatzim.mumlatzimv2.data.sqlite.OrderedDbController;
import com.mumlatzim.mumlatzimv2.fragment.ImageZoomFragment;
import com.mumlatzim.mumlatzimv2.listeners.ListItemClickListener;
import com.mumlatzim.mumlatzimv2.listeners.WebListener;
import com.mumlatzim.mumlatzimv2.models.bookmark.BookmarkModel;
import com.mumlatzim.mumlatzimv2.models.bookmark.OrderModel;
import com.mumlatzim.mumlatzimv2.models.comment.Comments;
import com.mumlatzim.mumlatzimv2.models.post.Post;
import com.mumlatzim.mumlatzimv2.models.post.PostDetails;
import com.mumlatzim.mumlatzimv2.models.post.PostReport;
import com.mumlatzim.mumlatzimv2.utility.ActivityUtilities;
import com.mumlatzim.mumlatzimv2.utility.AdsUtilities;
import com.mumlatzim.mumlatzimv2.utility.TtsEngine;
import com.mumlatzim.mumlatzimv2.webengine.PostWebEngine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class PostDetailsActivity extends BaseActivity {
    private Activity mActivity;
    private Context mContext;
    private ImageView mPostImage;
    private FloatingActionButton mFab;
    private Button mTagFab;
    private AppBarLayout appbar;
    private TextView mTvTitle, mTvDate, mTvComment, mTvRelated,activity_post_details_reporting,order_textView;
    private int mPostId,mTagsid;
    private PostDetails mModel = null;
    private PostReport mPostReport=null;
    private RelativeLayout mLytContainer;
    private ArrayList<Comments> mCommentList;
    private String mCommentsLink,mTagsname;
    private int mItemCount = 5;
    private LinearLayout mLytSecondary, mLytThird;
    private FragmentManager fm;
    private FragmentTransaction ft;

    // Bookmarks view
    private List<BookmarkModel> mBookmarkList;
    private List<OrderModel> mOrderedList;
    private BookmarkDbController mBookmarkDbController;
    private OrderedDbController mOrderedDbController;
    private boolean mIsBookmark;
    private boolean mIsOrdered;

    private TtsEngine mTtsEngine;
    private boolean mIsTtsPlaying = false;
    private String mTtsText,link,coupon_code;
    private MenuItem menuItemTTS;
    public static String imagePost="com.mumlatzim.mumlatzimv2.activity.ImagePost";



    private WebView mWebView;
    private PostWebEngine mPostWebEngine;

    private LinearLayout activity_post_details_cartLayout;
    private TextView activity_post_details_cartTextView,activity_post_details_coupon,activity_post_details_custom_title;

    private List<Post> mRelatedList;
    private RecyclerView mRvRelated;
    private RelatedAdapter mRelatedAdapter = null;
    private int mPageNo = 1;
    private Bitmap bitmap;
    private String imgUrl = null;

    //Extract Href from PostText
    private static final String HTML_A_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";
    private static final String HTML_A_HREF_TAG_PATTERN =
            "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";

    private static final String HTML_IMG_TAG_PATTERN = "<img[^>]+src=\\\"([^\\\">]+)\\\"";
    private static final String HTML_IMG_SRC_PATTERN  ="src=\\\"(.*?)\\\"";
    private Button mBtnViewALlComments, mBtnWriteAComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar();
        initView();
        initFunctionality();
        initListener();
    }

    private void initVar() {
        mActivity = PostDetailsActivity.this;
        mContext = mActivity.getApplicationContext();

        Intent intent = getIntent();
        if (intent != null) {
            mPostId = intent.getIntExtra(AppConstant.BUNDLE_KEY_POST_ID, 0);
        }
        mBookmarkList = new ArrayList<>();
        mOrderedList= new ArrayList<>();
        mCommentList = new ArrayList<>();
        mRelatedList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_post_details);
        mPostImage = (ImageView) findViewById(R.id.post_img);
        mFab = (FloatingActionButton) findViewById(R.id.share_post);
        mTagFab=(Button) findViewById(R.id.tags_post);
        appbar=(AppBarLayout)findViewById(R.id.appbar);
        mTvTitle = (TextView) findViewById(R.id.title_text);
        mTvDate = (TextView) findViewById(R.id.date_text);
        order_textView=(TextView) findViewById(R.id.order_textView);
        mTvComment = (TextView) findViewById(R.id.comment_text);
        mLytContainer = (RelativeLayout) findViewById(R.id.lyt_container);
        mLytSecondary = (LinearLayout) findViewById(R.id.lyt_secondary);
        mTvRelated = (TextView) findViewById(R.id.tv_related);
        mLytThird = (LinearLayout) findViewById(R.id.lyt_third);
        activity_post_details_custom_title=(TextView) findViewById(R.id.activity_post_details_custom_title);

        activity_post_details_reporting=(TextView) findViewById(R.id.activity_post_details_reporting);

        mRvRelated = (RecyclerView) findViewById(R.id.rvRelated);
        mRvRelated.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRelatedAdapter = new RelatedAdapter(mContext, (ArrayList<Post>) mRelatedList);
        mRvRelated.setAdapter(mRelatedAdapter);

        mBtnViewALlComments = (Button) findViewById(R.id.btn_view_comment);
        mBtnWriteAComment = (Button) findViewById(R.id.btn_write_comment);

        activity_post_details_cartLayout=(LinearLayout)findViewById(R.id.activity_post_details_cartLayout);
        activity_post_details_cartTextView=(TextView)findViewById(R.id.activity_post_details_cartTextView);
        activity_post_details_coupon=(TextView) findViewById(R.id.activity_post_details_coupon);

        initWebEngine();
        initLoader();
        initToolbar(false);
        enableUpButton();
    }

    public void initWebEngine() {

        mWebView = (WebView) findViewById(R.id.web_view);

        mPostWebEngine = new PostWebEngine(mWebView, mActivity);
        mPostWebEngine.initWebView();

        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction()==MotionEvent.ACTION_MOVE)
                {
                    return false;
                }
                if (event.getAction()==MotionEvent.ACTION_UP)
                {
                    //startActivity(new Intent(this,Example.class));
                    WebView.HitTestResult hr = ((WebView)v).getHitTestResult();
                    //  Log.e(TAG, "getExtra = "+ hr.getExtra() + "\t\t Type=" + hr.getType());
                    if(hr.getType()==WebView.HitTestResult.IMAGE_TYPE)
                    {
                        Log.e(TAG, "getExtra = "+ hr.getExtra() + "\t\t Type=" + hr.getType());

                        String imageUrl=hr.getExtra();

                        Log.e("Constant",""+imageUrl);
                        fm=getSupportFragmentManager();
                        Bundle bundle=new Bundle();
                        bundle.putString(imagePost,imageUrl);
                        ImageZoomFragment imageZoom=new ImageZoomFragment();
                        imageZoom.setArguments(bundle);
                        imageZoom.show(fm,"dialog");
                    }
                }
                return false;
            }
        });

        mPostWebEngine.initListeners(new WebListener() {
            @Override
            public void onStart() {
                showLoader();
            }

            @Override
            public void onLoaded() {
                hideLoader();
            }

            @Override
            public void onProgress(int progress) {
            }

            @Override
            public void onNetworkError() {
                showEmptyView();
            }

            @Override
            public void onPageTitle(String title) {
            }
        });
    }

    private void initFunctionality() {

        showLoader();

        mTtsEngine = new TtsEngine(mActivity);

        loadPostDetails();
        updateUI();
        updateOrderUI();
        // show full-screen ads
       // AdsUtilities.getInstance(mContext).showFullScreenAd();
        // show banner ads
        AdsUtilities.getInstance(mContext).showBannerAd((AdView) findViewById(R.id.adsView));
    }

    private void initListener() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imgUrl=null;
                if (mModel != null) {
                    if (mIsBookmark) {
                        mBookmarkDbController.deleteEachFav(mModel.getID().intValue());
                        Toast.makeText(mActivity, getString(R.string.removed_from_book), Toast.LENGTH_SHORT).show();
                    } else {
                        int postId = mModel.getID().intValue();
                        if (mModel.getEmbedded()!=null && mModel.getEmbedded().getWpFeaturedMedias().size() > 0) {
                            if (mModel.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails() != null) {
                                if(mModel.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getThumbnailSize().getSourceUrl() != null)
                                {
                                    imgUrl=mModel.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getThumbnailSize().getSourceUrl();
                                }
                                else if (mModel.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getFullSize().getSourceUrl() != null)
                                {
                                    imgUrl = mModel.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getFullSize().getSourceUrl();
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
                       // String imgUrl = mModel.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getFullSize().getSourceUrl();
                        String postTitle = mModel.getTitle().getRendered();
                        String postUrl = mModel.getPostUrl();
                        String postCategory = mModel.getEmbedded().getWpTerms().get(0).get(0).getName();
                        String postDate = mModel.getFormattedDate();

                        mBookmarkDbController.insertData(postId, imgUrl, postTitle, postUrl, postCategory, postDate);
                        Toast.makeText(mActivity, getString(R.string.added_to_bookmark), Toast.LENGTH_SHORT).show();
                    }
                    mIsBookmark = !mIsBookmark;
                    setFabImage();
                }
            }
        });

        mTagFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mModel!=null)
                {
                    ActivityUtilities.getInstance().postTagListActivity(mActivity,SubTagListActivity.class,mTagsid,mTagsname,false);
                }
            }
        });

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (Math.abs(i) >= (appBarLayout.getTotalScrollRange())) {
                    // Make your TextView Visible here
                    mTagFab.setVisibility(View.GONE);
                }
                else {
                    if(mTagsname!=null)
                    {
                        mTagFab.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        mLytContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mModel != null) {
                    ActivityUtilities.getInstance().invokeCommentListActivity(mActivity, CommentListActivity.class, mPostId, mCommentsLink, false, false);
                }
            }
        });

        mRelatedAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Post model = mRelatedList.get(position);
                switch (view.getId()) {
                    case R.id.lyt_container:
                        ActivityUtilities.getInstance().invokePostDetailsActivity(mActivity, PostDetailsActivity.class, model.getID().intValue(), true);
                        break;
                    default:
                        break;
                }
            }
        });

        mBtnViewALlComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mModel != null) {
                    ActivityUtilities.getInstance().invokeCommentListActivity(mActivity, CommentListActivity.class, mPostId, mCommentsLink, false, false);
                }
            }
        });

        mBtnWriteAComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mModel != null) {
                    ActivityUtilities.getInstance().invokeCommentListActivity(mActivity, CommentListActivity.class, mPostId, mCommentsLink, true, false);
                }
            }
        });

        activity_post_details_cartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("clicked ","cart is clickable");
                Log.e("Constant","maternLink href value is "+link);
                ActivityUtilities.getInstance().invokeBrowser(mActivity,link);
            }
        });

        activity_post_details_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityUtilities.getInstance().invokeCouponClipBoard(mActivity,coupon_code);
                activity_post_details_coupon.setBackground(getResources().getDrawable(R.drawable.rectangle_gray_normal));
                activity_post_details_coupon.setText(getResources().getString(R.string.couponApplied));
                activity_post_details_coupon.setClickable(false);
            }
        });

        activity_post_details_reporting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtilities.getInstance().PostReport(mActivity,getString(R.string.report_message), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ApiUtilities.getApiInterface().getPostReport("wprc_add_report",mPostId,"BadLink","appreport@gmail.com","reporter").enqueue(new Callback<PostReport>() {
                            @Override
                            public void onResponse(Call<PostReport> call, Response<PostReport> response) {
                                Log.e("Response","code is "+response.code());
                                if(response.isSuccessful())
                                {
                                    mPostReport=response.body();
                                    int success=mPostReport.getSuccess();
                                   // String message=mPostReport.getMessage();
                                    Log.e("Report ","message is "+success);
                                    if(success==1)
                                    {
                                        Toast.makeText(mActivity, "הלינק דווח", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(mActivity, "not success", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<PostReport> call, Throwable t) {

                            }
                        });
                    }
                });
            }
        });

        order_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (mModel != null) {
                    if (mIsOrdered) {
                        mOrderedDbController.deleteEachFav(mModel.getID().intValue());
                        Toast.makeText(mActivity, getString(R.string.toast_removedOrder), Toast.LENGTH_SHORT).show();
                    }
                    else
                        {
                        int postId = mModel.getID().intValue();
                            if (mModel.getEmbedded()!=null && mModel.getEmbedded().getWpFeaturedMedias().size() > 0) {
                                if (mModel.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails() != null) {
                                    if(mModel.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getThumbnailSize().getSourceUrl() != null)
                                    {
                                        imgUrl=mModel.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getThumbnailSize().getSourceUrl();
                                    }
                                    else if (mModel.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getFullSize().getSourceUrl() != null)
                                    {
                                        imgUrl = mModel.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getFullSize().getSourceUrl();
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
                        String postTitle = mModel.getTitle().getRendered();
                        String postUrl = mModel.getPostUrl();
                        String postCategory = mModel.getEmbedded().getWpTerms().get(0).get(0).getName();
                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy",java.util.Locale.getDefault());
                        String postDate = df.format(c);
                        String postTrack= getResources().getString(R.string.track_no);
                       // String postScreenshot=getResources().getString(R.string.add_screenshot);
                            String  postScreenshot = "";
                        mOrderedDbController.insertData(postId, imgUrl, postTitle, postUrl, postCategory, postDate,postTrack,postScreenshot);
                        Toast.makeText(mActivity, getString(R.string.toast_addedOrdered), Toast.LENGTH_SHORT).show();
                    }
                    mIsOrdered = !mIsOrdered;
                    setFabOrder();
                }
            }
        });
    }

    private void loadPostDetails()
    {

        ApiUtilities.getApiInterface().getPostDetails(mPostId).enqueue(new Callback<PostDetails>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                long a=response.raw().receivedResponseAtMillis();
                long b=response.raw().sentRequestAtMillis();
                long c=a-b;
                Log.e("TAG","response success postDetails "+c);
                if (response.isSuccessful())
                {

                    mLytSecondary.setVisibility(View.VISIBLE);
                    mFab.setVisibility(View.VISIBLE);
                    mTagFab.setVisibility(View.VISIBLE);
                    mModel = response.body();

                    mCommentsLink = mModel.getLinks().getComments().get(0).getHref();
                    Log.e("Constant"," comment link is "+mCommentsLink);
                    loadComments();
                    loadRelatedPosts();
                    checklinkavailablity();
                    if(link!=null && !link.isEmpty())
                    {
                        loadCart();
                    }
                    mTvTitle.setText(Html.fromHtml(mModel.getTitle().getRendered()));

                    if (mModel.getEmbedded()!=null && mModel.getEmbedded().getWpFeaturedMedias().size() > 0) {
                        if (mModel.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails() != null)
                        {
                            if(mModel.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getThumbnailSize().getSourceUrl()!=null)
                            {
                                imgUrl = mModel.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getThumbnailSize().getSourceUrl();
                            }
                            else if (mModel.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getFullSize().getSourceUrl() != null)
                            {
                                imgUrl = mModel.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getFullSize().getSourceUrl();
                            }
                        }
                    }

                    if (imgUrl != null)
                    {
                        GlideApp.with(getApplicationContext())
                                .load(imgUrl)
                                .into(mPostImage);
                        getBitmap();
                    }
                    else
                    {
                        GlideApp.with(getApplicationContext())
                                .load(R.drawable.no_images_preview)
                                .fitCenter()
                                .into(mPostImage);
                    }
                    if(mModel.getMacf_PostDetails()!=null)
                    if(mModel.getMacf_PostDetails().getMcoupon()!=null && !mModel.getMacf_PostDetails().getMcoupon().isEmpty())
                    {
                        coupon_code=mModel.getMacf_PostDetails().getMcoupon();
                        Log.e("coupon","code is "+coupon_code);
                    }
                    else
                    {
                        activity_post_details_coupon.setVisibility(View.GONE);
                    }

                    mTvDate.setText(mModel.getFormattedDate());
                    if(mModel.getMtaxonomies_PostDetails()!=null)
                    if(mModel.getMtaxonomies_PostDetails().getMtags_Details().size()!=0)
                    {
                        mTagsid= mModel.getMtaxonomies_PostDetails().getMtags_Details().get(0).getmID().intValue();
                        mTagsname=mModel.getMtaxonomies_PostDetails().getMtags_Details().get(0).getmName();
                        mTagFab.setText(mTagsname.trim());
                    }
                    else
                    {
                        mTagFab.setVisibility(View.GONE);
                    }

                    //get all Images

                    String postContent = mModel.getContent().getRendered();

                    List<String> allMatches = new ArrayList<String>();
                    ArrayList<String> post_ImageUrls = new ArrayList<String>();
                    List<String> all = new ArrayList<String>();
                    Matcher matcher1 = null;
                    Pattern pattern=Pattern.compile(HTML_IMG_TAG_PATTERN);
                    Matcher matcher=pattern.matcher(postContent);
                    while (matcher.find())
                    {
                        if(!matcher.group().contains("role")) {
                            allMatches.add(matcher.group());

                            for (int i = 0; i < allMatches.size(); i++) {
                                Pattern pattern1 = Pattern.compile(HTML_IMG_SRC_PATTERN);
                                matcher1 = pattern1.matcher(allMatches.get(i));

                            }
                            while (matcher1.find()) {
                                post_ImageUrls.add(matcher1.group(1));

                            }
                        }
                    }


                    Log.e("First Img urls are "," "+post_ImageUrls.get(0));
                    String imgfromArrayUrl=post_ImageUrls.get(0);
                   postContent= postContent.replaceFirst("<img(.*?)src=\"(.*?)\""+imgfromArrayUrl+"(.*?)/>","");
                    mTtsText = new StringBuilder(Html.fromHtml(mModel.getTitle().getRendered())).append(AppConstant.DOT).append(Html.fromHtml(mModel.getContent().getRendered())).toString();
                    postContent = new StringBuilder().append(AppConstant.CSS_PROPERTIES).append(postContent).toString();
                    postContent= postContent.replace("#", "%23");
                    mPostWebEngine.loadHtml(postContent);
                    String customTitle=null;
                    if(mModel.getMacf_PostDetails()!=null)
                    { customTitle=mModel.getMacf_PostDetails().getMtitle();
                    }

                   // Log.e("Custom title","is "+customTitle);
                    if(customTitle!=null && !customTitle.isEmpty())
                    {
                        Log.e("inside","if "+customTitle);
                        activity_post_details_custom_title.setText(customTitle);
                        activity_post_details_custom_title.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        Log.e("inside","else"+customTitle);
                        activity_post_details_custom_title.setVisibility(View.GONE);
                    }
                    if(mModel.getMacf_PostDetails()!=null)
                    if(mModel.getMacf_PostDetails().getMok()!=null && mModel.getMacf_PostDetails().getMok().equals("1"))
                    {
                        activity_post_details_reporting.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        activity_post_details_reporting.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<PostDetails> call, Throwable t) {
                t.printStackTrace();
                showEmptyView();
            }
        });
    }

    private void checklinkavailablity() {

            String postContent=mModel.getContent().getRendered();
            Pattern pattern=Pattern.compile(HTML_A_TAG_PATTERN);
            Matcher matcher=pattern.matcher(postContent);
            while(matcher.find())
            {
                String abc=matcher.group();
                if(abc.contains("לרכישה"))
                {
                    String href = matcher.group(1);
                    Pattern patternLink = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
                    Matcher matcherLink = patternLink.matcher(href);

                    while (matcherLink.find()) {

                        link = matcherLink.group(1); // link
                        link = link.replaceAll("^\"|\"$", "");
                    }
                }
                else
                {
                    if(mModel.getMacf_PostDetails()!=null)
                    if(mModel.getMacf_PostDetails().getMlink()!=null && !mModel.getMacf_PostDetails().getMlink().isEmpty())
                    {
                        link=mModel.getMacf_PostDetails().getMlink();
                    }
                }
            }
    }

    private void loadCart() {
        String ok=null;
        if(mModel.getMtaxonomies_PostDetails()!=null)
        {
            ok=mModel.getMacf_PostDetails().getMok();
        }

        if(ok!=null && ok.equals("1"))
        {
            int drawable=R.drawable.ic_shopping_cart;
            activity_post_details_cartLayout.setVisibility(View.VISIBLE);
            activity_post_details_cartTextView.setBackground(getResources().getDrawable(R.drawable.rectangle_cart));
            activity_post_details_cartTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,drawable);
            activity_post_details_cartTextView.setText(getString(R.string.click_to_purchase));

        }
        else
        {
            int warning=R.drawable.ic_warning;
            activity_post_details_cartLayout.setVisibility(View.VISIBLE);
            activity_post_details_cartTextView.setBackground(getResources().getDrawable(R.drawable.rectangle_gray));
            activity_post_details_cartTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,warning);
            activity_post_details_cartTextView.setText(getString(R.string.not_available));
            activity_post_details_cartTextView.setClickable(false);
        }
    }

    private void loadComments()
    {

        ApiUtilities.getApiInterface().getComments(mCommentsLink, mItemCount).enqueue(new Callback<List<Comments>>() {
            @Override
            public void onResponse(Call<List<Comments>> call, Response<List<Comments>> response) {
                if (response.isSuccessful()) {

                    int totalPages = Integer.parseInt(response.headers().get(HttpParams.TOTAL_PAGE));

                    if (totalPages > 1) {
                        mItemCount = mItemCount * totalPages;
                        loadComments();
                    } else {
                        mCommentList.clear();
                        mCommentList.addAll(response.body());

                        int commentCount = 0;
                        for (int i = 0; i < mCommentList.size(); i++) {
                            if (mCommentList.get(i).getParent() == 0) {
                                commentCount++;
                            }
                        }

                        mTvComment.setText(String.valueOf(commentCount));
                        mBtnViewALlComments.setText(String.format(getString(R.string.view_comments), commentCount));
                        hideLoader();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Comments>> call, Throwable t)
            {
                showEmptyView();
                t.printStackTrace();
            }
        });
    }

    public void loadRelatedPosts() {
        ApiUtilities.getApiInterface().getPostsByCategory(mPageNo, mModel.getCategories().get(AppConstant.ZERO_INDEX)).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {

                    List<Post> relatedList = new ArrayList<>();
                    relatedList.addAll(response.body());
                    for (int i = 0; i < relatedList.size(); i++) {
                        if (mRelatedList.size() == AppConstant.THIRD_INDEX) {
                            break;
                        }
                        if (relatedList.get(i).getID().intValue() != mPostId) {
                            mRelatedList.add(relatedList.get(i));
                        }
                    }
                    if (mRelatedList.size() > AppConstant.ZERO_INDEX) {
                        mTvRelated.setVisibility(View.VISIBLE);
                    }
                    mLytThird.setVisibility(View.VISIBLE);

                    mRelatedAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                showEmptyView();
            }
        });
    }

    public void updateUI() {

        if (mBookmarkDbController == null) {
            mBookmarkDbController = new BookmarkDbController(mContext);
        }
        mBookmarkList.clear();
        mBookmarkList.addAll(mBookmarkDbController.getAllData());

        for (int i = 0; i < mBookmarkList.size(); i++) {
            if (mPostId == mBookmarkList.get(i).getPostId()) {
                mIsBookmark = true;
                break;
            }
        }
        setFabImage();
    }

    private void updateOrderUI()
    {
        if (mOrderedDbController == null) {
            mOrderedDbController = new OrderedDbController(mContext);
        }
        mOrderedList.clear();
        mOrderedList.addAll(mOrderedDbController.getAllData());


        for (int i = 0; i < mOrderedList.size(); i++) {
            if (mPostId == mOrderedList.get(i).getPostId()) {
                mIsOrdered = true;
                break;
            }
        }
        setFabOrder();
    }

    private void setFabImage() {
        if (mIsBookmark) {
            mFab.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_book));
        }
        else {
            mFab.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_un_book));
        }
      //  mTagFab.setImageDrawable(ContextCompat.getDrawable(mActivity,R.drawable.ic_floating_tag));
    }

    private void setFabOrder()
    {
        if (mIsOrdered) {
           // mFab.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_book));
            Log.e("Constant","inside if setFavorder");
            order_textView.setText(R.string.remove_orderedList);
        } else {
           // mFab.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_un_book));
            Log.e("Constant","inside else setFavorder");
            order_textView.setText(R.string.add_orderedlist);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
           /* case R.id.menus_read_article:
                if (mModel != null) {
                    toggleTtsPlay();
                }
                return true;
                */
            case R.id.menus_share_post:
                if (mModel != null) {
                    final String appPackageName = mActivity.getPackageName();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(mModel.getPostUrl())
                            + AppConstant.EMPTY_STRING
                            );
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                }
                break;
        /***    case R.id.menus_copy_text:
                if (mModel != null) {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Text Label", Html.fromHtml(mModel.getTitle().getRendered() + mModel.getContent().getRendered()));
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), getString(R.string.copy_to_clipboard), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menus_download_image:
                if (mModel != null) {
                    AppUtilities.downloadFile(mContext, mActivity, bitmap);
                }
                break;
            case R.id.menus_set_image:
                if (mModel != null) {
                    try {
                        WallpaperManager wm = WallpaperManager.getInstance(mContext);
                        wm.setBitmap(bitmap);
                        Toast.makeText(mActivity, getString(R.string.wallpaper_set), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(mActivity, getString(R.string.wallpaper_set_failed), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                */
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleTtsPlay() {
        if (mIsTtsPlaying) {
            mTtsEngine.releaseEngine();
            mIsTtsPlaying = false;
        } else {
            mTtsEngine.startEngine(mTtsText);
            mIsTtsPlaying = true;
        }
        toggleTtsView();
    }

    private void toggleTtsView() {
        if (mIsTtsPlaying) {
            menuItemTTS.setTitle(R.string.site_menu_stop_reading);
        } else {
            menuItemTTS.setTitle(R.string.read_post);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTtsEngine.releaseEngine();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTtsEngine.releaseEngine();
        mModel = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mIsTtsPlaying) {
            mIsTtsPlaying = false;
            menuItemTTS.setTitle(R.string.read_post);
        }

        if (mCommentsLink != null) {
            loadComments();
        }
        // load full screen ad
        AdsUtilities.getInstance(mContext).loadFullScreenAd(mActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
       // menuItemTTS = menu.findItem(R.id.menus_read_article);
        return true;
    }

    public void getBitmap() {
        GlideApp.with(mContext)
                .asBitmap()
                .load(imgUrl)
                .into(new Target<Bitmap>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {

                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        bitmap = resource;
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void getSize(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void removeCallback(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void setRequest(@Nullable Request request) {

                    }

                    @Nullable
                    @Override
                    public Request getRequest() {
                        return null;
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }
                });
    }

}
