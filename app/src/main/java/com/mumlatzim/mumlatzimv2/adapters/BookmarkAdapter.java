package com.mumlatzim.mumlatzimv2.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mumlatzim.mumlatzimv2.GlideApp.GlideApp;
import com.mumlatzim.mumlatzimv2.R;
import com.mumlatzim.mumlatzimv2.listeners.ListItemClickListener;
import com.mumlatzim.mumlatzimv2.models.bookmark.BookmarkModel;

import java.util.ArrayList;
import java.util.Random;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private Context mContext;

    private ArrayList<BookmarkModel> mBookmarkList;
    private ListItemClickListener mItemClickListener;

    public BookmarkAdapter(Context mContext, ArrayList<BookmarkModel> mBookmarkList) {
        this.mContext = mContext;
        this.mBookmarkList = mBookmarkList;
    }

    public void setItemClickListener(ListItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookmark_list, parent, false);
        return new ViewHolder(view, viewType, mItemClickListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgPost;
        private TextView tvPostTitle, tvCategoryName, tvPostDate;
        private ImageButton btnBookmark, btnSharePost;
        private RelativeLayout lytContainer;
        private LinearLayout lyt_secondary;
        private ListItemClickListener itemClickListener;


        public ViewHolder(View itemView, int viewType, ListItemClickListener itemClickListener) {
            super(itemView);

            this.itemClickListener = itemClickListener;
            // Find all views ids
            imgPost = (ImageView) itemView.findViewById(R.id.post_img);
            tvPostTitle = (TextView) itemView.findViewById(R.id.title_text);
            tvCategoryName = (TextView) itemView.findViewById(R.id.category_name);
            tvPostDate = (TextView) itemView.findViewById(R.id.date_text);
            btnBookmark = (ImageButton) itemView.findViewById(R.id.btn_book);
            btnBookmark.setVisibility(View.VISIBLE);
            btnSharePost = (ImageButton) itemView.findViewById(R.id.btn_share);
            lytContainer = (RelativeLayout) itemView.findViewById(R.id.lyt_container);
            lyt_secondary= (LinearLayout) itemView.findViewById(R.id.lyt_secondary);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) lyt_secondary.getLayoutParams();
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            lyt_secondary.setLayoutParams(lp);

            btnBookmark.setOnClickListener(this);
            btnSharePost.setOnClickListener(this);
            lytContainer.setOnClickListener(this);
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
        return (null != mBookmarkList ? mBookmarkList.size() : 0);

    }

    @Override
    public void onBindViewHolder(BookmarkAdapter.ViewHolder mainHolder, int position) {
        final BookmarkModel model = mBookmarkList.get(position);

        // setting data over views
        mainHolder.tvPostTitle.setText(Html.fromHtml(model.getPostTitle()));
        mainHolder.tvCategoryName.setText(Html.fromHtml(model.getPostCategory()));

        String imgUrl = model.getPostImageUrl();
        if (imgUrl != null && !imgUrl.isEmpty()) {
            GlideApp.with(mContext)
                    .load(imgUrl)
                    .fitCenter()
                    .into(mainHolder.imgPost);
        } else {
            GlideApp.with(mContext)
                    .load(R.drawable.no_images_preview)
                    .fitCenter()
                    .into(mainHolder.imgPost);
        }

        mainHolder.tvPostDate.setText(model.getFormattedDate());

        Random rand = new Random();
        int i = rand.nextInt(5) + 1;


        switch (i) {
            case 1:
                mainHolder.tvCategoryName.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rectangle_green));
                break;
            case 2:
                mainHolder.tvCategoryName.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rectangle_orange));
                break;
            case 3:
                mainHolder.tvCategoryName.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rectangle_pink));
                break;
            case 4:
                mainHolder.tvCategoryName.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rectangle_purple));
                break;
            case 5:
                mainHolder.tvCategoryName.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rectangle_red));
                break;
            default:
                break;
        }
    }
}