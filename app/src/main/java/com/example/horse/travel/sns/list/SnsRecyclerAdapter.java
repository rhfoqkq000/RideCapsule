package com.example.horse.travel.sns.list;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.horse.travel.R;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.util.List;


/**
 * Created by qazz92 on 2017. 10. 12..
 */

public class SnsRecyclerAdapter extends RecyclerView.Adapter<SnsRecyclerAdapter.ViewHolder> {

    private List<SnsListItem> items;

//    public SnsRecyclerAdapter(List<SnsListItem> items) {
//        this.items = items;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_sns,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Resources res = holder.itemView.getContext().getResources();
        SnsListItem item = items.get(position);

        SpannableString content = new SpannableString(item.getEmail());
        content.setSpan(new UnderlineSpan(), 0, item.getEmail().length(), 0);

        String[] imgArr = item.getImgs().split(",");

        HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(res.getColor(R.color.blue), new HashTagHelper.OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {
                Log.d("TAG",hashTag);
            }
        });

        mTextHashTagHelper.handle(holder.contentTextView);

        holder.userIdTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UserID",holder.userIdTextView.getText().toString());
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LIKE","CLICK");
                holder.like.setImageResource(R.drawable.like);
            }
        });

        holder.userIdTextView.setText(content);
        holder.contentTextView.setText(item.getPost());

        RequestOptions options = new RequestOptions();
        options.fitCenter().override(Target.SIZE_ORIGINAL, holder.myImageView.getHeight());
//        options.fitCenter();

        Glide.with(holder.myImageView.getContext())
                .load("http://168.115.9.99:5000/"+imgArr[0])
//                .apply(options)
//                .apply(bitmapTransform(new BlurTransformation(25)))
                .into(holder.myImageView);
    }

    @Override
    public int getItemCount() {

        int size = 0;

        if (items != null){
            size =  items.size();
        }
        return size;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contentTextView;
        TextView userIdTextView;
        ImageView like;
        ImageView myImageView;

        ViewHolder(View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.sns_con);
            userIdTextView = itemView.findViewById(R.id.user_id);
            like = itemView.findViewById(R.id.love);
            myImageView = itemView.findViewById(R.id.main_img);
        }
    }
    public void addNew(List<SnsListItem> items)
    {
        this.items = items;
        notifyDataSetChanged();
    }

    private void like(){
        
    }
}
