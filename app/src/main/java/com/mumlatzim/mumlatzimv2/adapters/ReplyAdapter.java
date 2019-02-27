package com.mumlatzim.mumlatzimv2.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mumlatzim.mumlatzimv2.GlideApp.GlideApp;
import com.mumlatzim.mumlatzimv2.R;
import com.mumlatzim.mumlatzimv2.api.ApiUtilities;
import com.mumlatzim.mumlatzimv2.api.HttpParams;
import com.mumlatzim.mumlatzimv2.listeners.ListItemClickListener;
import com.mumlatzim.mumlatzimv2.models.comment.Comments;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder> {
    private static Context mContext;
    private ArrayList<Comments> mCommentList;
    private static ArrayList<Comments> mReplybackList=new ArrayList<>();
    private ListItemClickListener mItemClickListener;
    private int mItemCount = 5;
    String reply;

    public ReplyAdapter(Context mContext, ArrayList<Comments> mCommentList) {
        this.mContext = mContext;
        this.mCommentList = mCommentList;
    }

    public void setItemClickListener(ListItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    @Override
    public ReplyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_list, parent, false);
        return new ReplyAdapter.ViewHolder(view, viewType, mItemClickListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgAuthor;
        private TextView tvAuthorName, tvCommentDate, tvComment;
        private RelativeLayout lytComment;
        private RecyclerView recyclerView;
        private View view;
        private ReplybackAdapter mCommentAdapter=null;
        private ListItemClickListener itemClickListener;


        public ViewHolder(View itemView, int viewType, ListItemClickListener itemClickListener) {
            super(itemView);

            this.itemClickListener = itemClickListener;
            // Find all views ids
            imgAuthor = (ImageView) itemView.findViewById(R.id.author_img);
            tvAuthorName = (TextView) itemView.findViewById(R.id.author_name);
            tvAuthorName.setTextColor(mContext.getResources().getColor(R.color.green));
            tvCommentDate = (TextView) itemView.findViewById(R.id.comment_date);
            tvComment = (TextView) itemView.findViewById(R.id.comment_text);
            lytComment = (RelativeLayout) itemView.findViewById(R.id.lyt_comment);
            recyclerView=(RecyclerView) itemView.findViewById(R.id.reply_recyclerView);

            lytComment.setOnClickListener(this);
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
        return (null != mCommentList ? mCommentList.size() : 0);

    }

    @Override
    public void onBindViewHolder(final ReplyAdapter.ViewHolder mainHolder, int position) {

        final Comments model = mCommentList.get(position);

        // setting data over views

        String imgUrl = null;
        if (model.getAuthorAvatarUrl().getUrlLink() != null) {
            imgUrl = model.getAuthorAvatarUrl().getUrlLink();
        }

        if (imgUrl != null) {
            GlideApp.with(mContext)
                    .load(imgUrl)
                    .into(mainHolder.imgAuthor);
        } else {
            GlideApp.with(mContext)
                    .load(R.drawable.ic_author)
                    .into(mainHolder.imgAuthor);
        }

        mainHolder.tvAuthorName.setText(Html.fromHtml(model.getAuthorName()));
        mainHolder.tvCommentDate.setText(model.getFormattedDate());
        mainHolder.tvComment.setText(Html.fromHtml(model.getContent().getRendered()));

        if(model.getmLink()!=null && !model.getmLink().getmComments().isEmpty())
        {
            Log.e("Constant"," inside if");
            mainHolder.recyclerView.setVisibility(View.VISIBLE);
            mainHolder.mCommentAdapter=null;
            mainHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            reply=model.getmLink().getmComments().get(0).getHref();
            Log.e("Constamnt","reply string is "+reply);
            ApiUtilities.getApiInterface().getComments(reply, mItemCount).enqueue(new Callback<List<Comments>>() {
                @Override
                public void onResponse(Call<List<Comments>> call, Response<List<Comments>> response) {
                    Log.e("Constant"," response is "+response.code());
                    if (response.isSuccessful()) {

                        int totalPages = Integer.parseInt(response.headers().get(HttpParams.TOTAL_PAGE));

                        if (totalPages > 1) {
                            mItemCount = mItemCount * totalPages;
                        } else {
                            mReplybackList.clear();
                            mReplybackList.addAll(response.body());
                            mainHolder.mCommentAdapter=new ReplybackAdapter(mContext,mReplybackList);
                            mainHolder.recyclerView.setAdapter(mainHolder.mCommentAdapter);
                            if (mReplybackList.size() == 0) {
                                // showEmptyView();
                            } else {
                                // hideLoader();
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<List<Comments>> call, Throwable t) {
                    // showEmptyView();
                    t.printStackTrace();
                }
            });
        }

    }
}
