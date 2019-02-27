package com.mumlatzim.mumlatzimv2.adapters;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mumlatzim.mumlatzimv2.GlideApp.GlideApp;
import com.mumlatzim.mumlatzimv2.R;
import com.mumlatzim.mumlatzimv2.listeners.ListItemClickListener;
import com.mumlatzim.mumlatzimv2.models.post.Post;

import java.util.ArrayList;
import java.util.Random;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context mContext;

    private ArrayList<Post> mFeaturedPostList;
    private ListItemClickListener mItemClickListener;

    public PostAdapter(Context mContext, ArrayList<Post> mFeaturedPostList) {
        this.mContext = mContext;
        this.mFeaturedPostList = mFeaturedPostList;
    }

    public void setItemClickListener(ListItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_list, parent, false);
        return new ViewHolder(view, viewType, mItemClickListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgPost;
        private TextView tvCategoryName, tvPostTitle, tvPostDate,category_custom_title;
        private ImageButton btnBook, btnShare;
        private CardView cardView;
        private Button category_nicename,not_available_Button;
        private ListItemClickListener itemClickListener;


        public ViewHolder(View itemView, int viewType, ListItemClickListener itemClickListener) {
            super(itemView);

            this.itemClickListener = itemClickListener;
            // Find all views ids
            imgPost = (ImageView) itemView.findViewById(R.id.post_img);
            tvCategoryName = (TextView) itemView.findViewById(R.id.category_name);
            tvPostTitle = (TextView) itemView.findViewById(R.id.title_text);
            tvPostDate = (TextView) itemView.findViewById(R.id.date_text);
            btnBook = (ImageButton) itemView.findViewById(R.id.btn_book);
            btnShare = (ImageButton) itemView.findViewById(R.id.btn_share);
            cardView = (CardView) itemView.findViewById(R.id.card_view_top);
            category_nicename=(Button) itemView.findViewById(R.id.category_nickname);
            not_available_Button=(Button) itemView.findViewById(R.id.not_available_Button);
            category_custom_title=(TextView) itemView.findViewById(R.id.category_custom_title);

            btnBook.setOnClickListener(this);
            btnShare.setOnClickListener(this);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getLayoutPosition(), view);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != mFeaturedPostList ? mFeaturedPostList.size() : 0);
    }

    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder mainHolder, int position) {
        String imgUrl = null;
        String customTitle=null;
        String category_name=null;
        String OK=null;

        final Post model = mFeaturedPostList.get(position);


        customTitle=model.getMacf_PostDetails().getMtitle();
        if(customTitle!=null)
        {
            if(!customTitle.isEmpty())
            {
                mainHolder.category_custom_title.setText(customTitle);
                mainHolder.category_custom_title.setVisibility(View.VISIBLE);
            }
            else
            {
                mainHolder.category_custom_title.setVisibility(View.GONE);
            }
        }

        category_name=model.getMtaxonomies_PostDetails().getMcategories_Details().get(0).getmCatName();
        if(category_name!=null)
        {
            if(category_name.equals("digital"))
            {
                mainHolder.category_nicename.setVisibility(View.VISIBLE);
            }
            else
            {
                mainHolder.category_nicename.setVisibility(View.GONE);
            }
        }

        OK=model.getMacf_PostDetails().getMok();

        if(OK!=null)
        {
            if(OK.equals("0"))
            {
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);  //0 means grayscale
                ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
                mainHolder.imgPost.setColorFilter(cf);
                mainHolder.not_available_Button.setVisibility(View.VISIBLE);
                mainHolder.not_available_Button.getBackground().setColorFilter(cf);
                mainHolder.category_nicename.getBackground().setColorFilter(cf);
            }
            else
            {
                mainHolder.imgPost.clearColorFilter();
                mainHolder.category_nicename.getBackground().clearColorFilter();
                mainHolder.not_available_Button.getBackground().clearColorFilter();
                mainHolder.not_available_Button.setVisibility(View.GONE);
            }
        }
        // setting data over views

        if (model.getEmbedded().getWpFeaturedMedias().size() > 0) {
            if (model.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails() != null) {
                if (model.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getThumbnailSize().getSourceUrl() != null) {
                    imgUrl = model.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getThumbnailSize().getSourceUrl();
                }
               else if (model.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getFullSize().getSourceUrl() != null) {
                    imgUrl = model.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getFullSize().getSourceUrl();
                }
            }
        }

        if (imgUrl != null) {
            GlideApp.with(mContext)
                    .load(imgUrl)
                    .into(mainHolder.imgPost);

        } else {
            GlideApp.with(mContext)
                    .load(R.drawable.no_images_preview)
                    .fitCenter()
                    .into(mainHolder.imgPost);
        }

        mainHolder.tvCategoryName.setText(Html.fromHtml(model.getEmbedded().getWpTerms().get(0).get(0).getName()));
        mainHolder.tvPostTitle.setText(Html.fromHtml(model.getTitle().getRendered()));
        mainHolder.tvPostDate.setText(model.getFormattedDate());


        if (model.isBookmark()) {
            mainHolder.btnBook.setImageResource(R.drawable.ic_book);
        } else {
            mainHolder.btnBook.setImageResource(R.drawable.ic_un_book);
        }

        Random rand = new Random();
        int i = rand.nextInt(5) + 1;


        if(OK!=null)
        {
            switch (i) {
                case 1:
                    if(model.getMacf_PostDetails().getMok().equals("0"))
                    {
                        mainHolder.tvCategoryName.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rectangle_gray));
                    }
                    else
                    {
                        mainHolder.tvCategoryName.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rectangle_green));
                    }
                    break;
                case 2:
                    if(model.getMacf_PostDetails().getMok().equals("0"))
                    {
                        mainHolder.tvCategoryName.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rectangle_gray));
                    }
                    else
                    {
                        mainHolder.tvCategoryName.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rectangle_orange));
                    }
                    break;
                case 3:
                    if(model.getMacf_PostDetails().getMok().equals("0"))
                    {
                        mainHolder.tvCategoryName.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rectangle_gray));
                    }
                    else
                    {
                        mainHolder.tvCategoryName.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rectangle_pink));
                    }
                    break;
                case 4:
                    if(model.getMacf_PostDetails().getMok().equals("0"))
                    {
                        mainHolder.tvCategoryName.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rectangle_gray));
                    }
                    else
                    {
                        mainHolder.tvCategoryName.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rectangle_purple));
                    }
                    break;
                case 5:
                    if(model.getMacf_PostDetails().getMok().equals("0"))
                    {
                        mainHolder.tvCategoryName.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rectangle_gray));
                    }
                    else
                    {
                        mainHolder.tvCategoryName.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rectangle_red));
                    }
                    break;
                default:
                    break;
            }
        }
    }
}