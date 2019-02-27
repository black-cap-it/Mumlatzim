package com.mumlatzim.mumlatzimv2.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

public class CustomerAdapter extends ArrayAdapter<Comments> {

    private ArrayList<Comments> mCommentList;
    private static ArrayList<Comments> mReplyList=new ArrayList<>();
    Context mContext;
    String reply;
    private int mItemCount = 5;

    // View lookup cache
    private static class ViewHolder {
        private ImageView imgAuthor;
        private TextView tvAuthorName, tvCommentDate, tvComment;
        private RelativeLayout lytComment;
        private ListView listView;
        private View view;
        private CustomAdapter mCustomAdapter=null;
        private ListItemClickListener itemClickListener;

    }

    public CustomerAdapter(Context mContext, ArrayList<Comments> mCommentList) {
        super(mContext, R.layout.item_comment_list);
        this.mContext=mContext;
        this.mCommentList = mCommentList;

    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        // Get the data item for this position
        Comments model = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final CustomerAdapter.ViewHolder mainHolder; // view lookup cache stored in tag

        final View result;

        if (itemView == null) {

            mainHolder = new CustomerAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            itemView = inflater.inflate(R.layout.item_comment_list, parent, false);
            mainHolder.imgAuthor = (ImageView) itemView.findViewById(R.id.author_img);
            mainHolder.tvAuthorName = (TextView) itemView.findViewById(R.id.author_name);
            mainHolder.tvAuthorName.setTextColor(mContext.getResources().getColor(R.color.green));
            mainHolder.tvCommentDate = (TextView) itemView.findViewById(R.id.comment_date);
            mainHolder.tvComment = (TextView) itemView.findViewById(R.id.comment_text);
            mainHolder.lytComment = (RelativeLayout) itemView.findViewById(R.id.lyt_comment);
           // mainHolder.listView=(ListView) itemView.findViewById(R.id.reply_recyclerView);

            result=itemView;

            itemView.setTag(mainHolder);
        }
        else
        {
            mainHolder = (CustomerAdapter.ViewHolder) itemView.getTag();
            result=itemView;
        }


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
            mainHolder.listView.setVisibility(View.VISIBLE);
            mainHolder.mCustomAdapter=null;
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
                            mReplyList.clear();
                            mReplyList.addAll(response.body());
                            mainHolder.mCustomAdapter=new CustomAdapter(mContext,mReplyList);
                            mainHolder.listView.setAdapter(mainHolder.mCustomAdapter);
                            if (mReplyList.size() == 0) {
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
        // Return the completed view to render on screen
        return itemView;
    }
}
