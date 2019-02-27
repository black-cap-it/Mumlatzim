package com.mumlatzim.mumlatzimv2.adapters;

import android.app.Activity;
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
import com.mumlatzim.mumlatzimv2.listeners.OrderItemClickListener;
import com.mumlatzim.mumlatzimv2.models.bookmark.OrderModel;

import java.util.ArrayList;
import java.util.Random;

public class OrderedAdapter extends RecyclerView.Adapter<OrderedAdapter.ViewHolder> {

    private static Context mContext;
    private ArrayList<OrderModel> mOrderList;
    private OrderItemClickListener mItemClickListener;


    public OrderedAdapter(Context mContext, Activity mActivity, ArrayList<OrderModel> mOrderList) {
        this.mContext = mContext;
        this.mOrderList = mOrderList;
    }

    public void setItemClickListener(OrderItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    @Override
    public OrderedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookmark_list, parent, false);
        return new OrderedAdapter.ViewHolder(view, viewType, mItemClickListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgPost;
        private TextView tvPostTitle, tvCategoryName, tvPostDate,track_number,screenshot;
        private ImageButton btnBookmark, btnSharePost,btn_order_edit;
        private RelativeLayout lytContainer;
        private OrderItemClickListener itemClickListener;
        private LinearLayout ordered_layout;

        public ViewHolder(View itemView, int viewType, OrderItemClickListener itemClickListener) {
            super(itemView);

            this.itemClickListener = itemClickListener;

            // Find all views ids
            imgPost = (ImageView) itemView.findViewById(R.id.post_img);
            tvPostTitle = (TextView) itemView.findViewById(R.id.title_text);
            tvCategoryName = (TextView) itemView.findViewById(R.id.category_name);
            track_number= (TextView) itemView.findViewById(R.id.track_number);
            screenshot= (TextView) itemView.findViewById(R.id.screenshot);
            tvPostDate = (TextView) itemView.findViewById(R.id.date_text);
            btnBookmark = (ImageButton) itemView.findViewById(R.id.btn_book);
            btnBookmark.setImageResource(R.drawable.ic_cancel);
            btnBookmark.setVisibility(View.VISIBLE);
            btn_order_edit=(ImageButton) itemView.findViewById(R.id.btn_order_edit);
            btnSharePost = (ImageButton) itemView.findViewById(R.id.btn_share);
            lytContainer = (RelativeLayout) itemView.findViewById(R.id.lyt_container);
            ordered_layout=(LinearLayout) itemView.findViewById(R.id.ordered_layout);
            ordered_layout.setVisibility(View.VISIBLE);
            btnBookmark.setOnClickListener(this);
            btnSharePost.setOnClickListener(this);
            btn_order_edit.setOnClickListener(this);
            lytContainer.setOnClickListener(this);
            track_number.setOnClickListener(this);
            screenshot.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onOrderClick(getLayoutPosition(), view,track_number,btn_order_edit);

            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != mOrderList ? mOrderList.size() : 0);
    }

    @Override
    public void onBindViewHolder(OrderedAdapter.ViewHolder mainHolder, int position) {
        final OrderModel model = mOrderList.get(position);

        // setting data over views
        mainHolder.tvPostTitle.setText(Html.fromHtml(model.getPostTitle()));
        mainHolder.tvCategoryName.setText(Html.fromHtml(model.getPostCategory()));

        String imgUrl = model.getPostImageUrl();
        if (imgUrl != null && !imgUrl.isEmpty()) {
            GlideApp.with(mContext)
                    .load(imgUrl)
                    .fitCenter()
                    .into(mainHolder.imgPost);
        }
        else
            {
            GlideApp.with(mContext)
                    .load(R.drawable.no_images_preview)
                    .fitCenter()
                    .into(mainHolder.imgPost);
        }

        mainHolder.tvPostDate.setText(model.getFormattedDate());
        mainHolder.track_number.setText(model.getPostTrack());
       // mainHolder.screenshot.setText(model.getPostScreenshot());
        if(model.getPostScreenshot()!=null)
        {
            mainHolder.screenshot.setText(R.string.view_screenshot);
        }
        else
           {
            mainHolder.screenshot.setText(R.string.add_screenshot);
        }

        if(!mainHolder.track_number.getText().toString().equals(mContext.getString(R.string.track_no)))
        {
            mainHolder.btn_order_edit.setVisibility(View.VISIBLE);
        }
        else
            {
                mainHolder.btn_order_edit.setVisibility(View.GONE);
            }

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
