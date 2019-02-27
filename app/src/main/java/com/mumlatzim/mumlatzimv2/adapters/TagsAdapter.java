package com.mumlatzim.mumlatzimv2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mumlatzim.mumlatzimv2.R;
import com.mumlatzim.mumlatzimv2.listeners.ListItemClickListener;
import com.mumlatzim.mumlatzimv2.models.category.Tags;

import java.util.ArrayList;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder>{
    private Context mContext;

    private ArrayList<Tags> mCategoryList;
    private ListItemClickListener mItemClickListener;

    public TagsAdapter(Context mContext, ArrayList<Tags> mCategoryList) {
        this.mContext = mContext;
        this.mCategoryList = mCategoryList;
    }

    public void setItemClickListener(ListItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    @NonNull
    @Override
    public TagsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_list, parent, false);
        return new TagsAdapter.ViewHolder(view, viewType, mItemClickListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tag_TextView, tag_count;
        private ConstraintLayout lytContainer;
        private ListItemClickListener itemClickListener;


        public ViewHolder(View itemView, int viewType, ListItemClickListener itemClickListener)
        {
            super(itemView);
            this.itemClickListener = itemClickListener;
            // Find all views ids
            tag_TextView = (TextView) itemView.findViewById(R.id.tag_TextView);
            tag_count = (TextView) itemView.findViewById(R.id.tag_count);
            lytContainer = (ConstraintLayout) itemView.findViewById(R.id.lyt_container);
            lytContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
            {
                itemClickListener.onItemClick(getLayoutPosition(), view);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != mCategoryList ? mCategoryList.size() : 0);
    }

    @Override
    public void onBindViewHolder(TagsAdapter.ViewHolder mainHolder, int position) {
        final Tags model = mCategoryList.get(position);
        // setting data over views
        mainHolder.tag_TextView.setText(Html.fromHtml(model.getmName()));
        mainHolder.tag_count.setText(String.valueOf(model.getmCount()));
        Log.e("inside recyclerView","");
    }
}
