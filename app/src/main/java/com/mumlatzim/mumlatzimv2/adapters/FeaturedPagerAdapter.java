package com.mumlatzim.mumlatzimv2.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mumlatzim.mumlatzimv2.GlideApp.GlideApp;
import com.mumlatzim.mumlatzimv2.R;
import com.mumlatzim.mumlatzimv2.listeners.ListItemClickListener;
import com.mumlatzim.mumlatzimv2.models.post.Post;

import java.util.ArrayList;

public class FeaturedPagerAdapter extends PagerAdapter {

    private Context mContext;

    private ArrayList<Post> mItemList;
    private ListItemClickListener mItemClickListener;

    private LayoutInflater inflater;

    public FeaturedPagerAdapter(Context mContext, ArrayList<Post> mItemList) {
        this.mContext = mContext;
        this.mItemList = mItemList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }


    @Override
    public Object instantiateItem(final ViewGroup view, final int position) {

        View rootView = inflater.inflate(R.layout.item_featured_pager_list, view, false);

        final TextView titleTextView = (TextView) rootView.findViewById(R.id.title_text);
        final ImageView imgPost = (ImageView) rootView.findViewById(R.id.post_img);
        final CardView cardView = (CardView) rootView.findViewById(R.id.card_view_top);

        final Post model = mItemList.get(position);
        String imgUrl = null;
        if (model.getEmbedded().getWpFeaturedMedias().size() > 0) {
            if (model.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails() != null) {
                if (model.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getFullSize().getSourceUrl() != null) {
                    imgUrl = model.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getFullSize().getSourceUrl();
                }
            }
        }

        if (imgUrl != null) {
            GlideApp.with(mContext)
                    .load(imgUrl)
                    .fitCenter()
                    .placeholder(R.color.imgPlaceholder)
                    .into(imgPost);
        }

        else
        {
            GlideApp.with(mContext)
                    .load(R.drawable.no_images_preview)
                    .fitCenter()
                    .into(imgPost);
        }

        String titleText = model.getTitle().getRendered();
        titleTextView.setText(Html.fromHtml(titleText));

        view.addView(rootView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(position, view);
                }
            }
        });

        return rootView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    public void setItemClickListener(ListItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
