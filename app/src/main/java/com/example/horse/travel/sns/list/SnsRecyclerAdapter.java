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
import com.example.horse.travel.sns.like.SnsItemLike;
import com.example.horse.travel.sns.like.SnsItemLikeDTO;
import com.example.horse.travel.sns.like.SnsItemUnLike;
import com.example.horse.travel.sns.like.SnsItemUnLikeDTO;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by qazz92 on 2017. 10. 12..
 */

public class SnsRecyclerAdapter extends RecyclerView.Adapter<SnsRecyclerAdapter.ViewHolder> {

    private final String IMG_URL = "http://168.115.8.109:5000/";

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Resources res = holder.itemView.getContext().getResources();
        final SnsListItem item = items.get(position);

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


        if (item.getLike_id()!=0){
            Log.d("ID",item.getLike_id()+" | "+position);
            holder.like.setImageResource(R.drawable.like);
        }

        holder.userIdTextView.setText(content);
        holder.contentTextView.setText(item.getPost());
        holder.contentTextView.setTag(item.getId());
        holder.like.setTag(item.getLike_id());

        RequestOptions options = new RequestOptions();
        options.fitCenter().override(Target.SIZE_ORIGINAL, holder.myImageView.getHeight());
//        options.fitCenter();

        Glide.with(holder.myImageView.getContext())
                .load(IMG_URL+imgArr[0])
//                .apply(options)
//                .apply(bitmapTransform(new BlurTransformation(25)))
                .into(holder.myImageView);


        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String isLike = holder.like.getTag().toString();
                if (Integer.parseInt(isLike)==0){
                    Log.d("LIKE","CLICK");
                    Call<SnsItemLikeDTO> call = like(holder.contentTextView.getTag().toString(),"9");
                    call.enqueue(new Callback<SnsItemLikeDTO>() {
                        @Override
                        public void onResponse(Call<SnsItemLikeDTO> call, Response<SnsItemLikeDTO> response) {
//                           Log.d("LIKE_SUC",response.body().getResult_code()+"");
                                    if (response.body().getResult_code()==200){
                                        Log.d("Result","LIKE_SUCCESS!!");
                                        holder.like.setImageResource(R.drawable.like);
                                        holder.like.setTag(response.body().getResult_body());
                                    }
                                }
                                @Override
                                public void onFailure(Call<SnsItemLikeDTO> call, Throwable t) {
                                    Log.d("LIKE_FAIL",t.getMessage());
                                }
                            });
                } else {
                    Log.d("UNLIKE","CLICK");
                    Call<SnsItemUnLikeDTO> call = unlike(holder.contentTextView.getTag().toString(),"9");
                    call.enqueue(new Callback<SnsItemUnLikeDTO>() {
                        @Override
                        public void onResponse(Call<SnsItemUnLikeDTO> call, Response<SnsItemUnLikeDTO> response) {
                            if (response.body().getResult_code()==200) {
                                Log.d("Result","UNLIKE_SUCCESS!!");
                                holder.like.setImageResource(R.drawable.normal);
                                holder.like.setTag("0");
                            }
                        }

                        @Override
                        public void onFailure(Call<SnsItemUnLikeDTO> call, Throwable t) {
                            Log.d("UNLIKE_FAIL",t.getMessage());
                        }
                    });
                }
            }
        });

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

    private Call<SnsItemLikeDTO> like(String content_id,String user_id){
        SnsItemLike like = new SnsItemLike();
        return like.like(content_id,user_id);
    }

    private Call<SnsItemUnLikeDTO> unlike(String content_id,String user_id){
        SnsItemUnLike unlike = new SnsItemUnLike();
        return unlike.unlike(content_id,user_id);
    }
}
