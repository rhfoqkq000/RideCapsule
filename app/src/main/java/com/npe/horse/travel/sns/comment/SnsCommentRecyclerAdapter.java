package com.npe.horse.travel.sns.comment;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.npe.horse.travel.R;
import com.npe.horse.travel.TimeCal;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.util.List;

/**
 * Created by qazz92 on 2017. 10. 29..
 */

public class SnsCommentRecyclerAdapter extends RecyclerView.Adapter<SnsCommentRecyclerAdapter.ViewHolder> {
    private List<SnsCommentItem> items;

    Context context;


    public SnsCommentRecyclerAdapter(Context context) {
        this.context = context;
    }

    void addNew(List<SnsCommentItem> items)
    {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public SnsCommentRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_sns_comment,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SnsCommentRecyclerAdapter.ViewHolder holder, int position) {
        SnsCommentItem item = items.get(position);

        HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(holder.itemView.getContext().getResources().getColor(R.color.blue), new HashTagHelper.OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {
                Log.d("TAG",hashTag);
            }
        });

        mTextHashTagHelper.handle(holder.sns_article_main_re);
        holder.sns_article_main_re.setText(item.getArticle());

        Glide.with(context).load(item.getProfile()).into(holder.sns_comment_profile_re);

        holder.sns_comment_updated_at_main_re.setText(TimeCal.formatTimeString(item.getUpdated_at().getTime()));

        holder.sns_author_main_re.setText(item.getNickname());
        holder.sns_author_main_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("name",holder.sns_author_main_re.getText().toString());
            }
        });
    }


    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sns_author_main_re;
        TextView sns_article_main_re;
        TextView sns_comment_updated_at_main_re;
        ImageView sns_comment_profile_re;
        ViewHolder(View itemView) {
            super(itemView);
            sns_author_main_re = itemView.findViewById(R.id.sns_author_main_re);
            sns_article_main_re = itemView.findViewById(R.id.sns_article_main_re);
            sns_comment_updated_at_main_re = itemView.findViewById(R.id.sns_comment_updated_at_main_re);
            sns_comment_profile_re = itemView.findViewById(R.id.sns_comment_profile_re);

        }
    }
}
