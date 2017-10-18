package com.example.horse.travel.sns.list;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.horse.travel.R;
import com.example.horse.travel.capsule.ViewPagerAdapter;
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

//    private final String IMG_URL = "http://168.115.8.109:5000/";
    private final String IMG_URL = "http://220.84.195.101:5000/";
    private List<SnsListItem> items;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_sns,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        setUI(holder, position);
    }

    private void setUI(final ViewHolder holder, int position) {
        Resources res = holder.itemView.getContext().getResources();
        final SnsListItem item = items.get(position);

        setHashTagTextView(holder, res, item);

        setUserIdTextView(holder, item);

        setLike(holder,item, position);

        setImage(holder, item);

    }

    private void setImage(final ViewHolder holder, SnsListItem item) {
//        RequestOptions options = new RequestOptions();
//        options.fitCenter().override(Target.SIZE_ORIGINAL, holder.myImageView.getHeight());
//        options.fitCenter();
        String[] imgArr = item.getImgs().split(",");
        Glide.with(holder.myImageView.getContext())
                .load(IMG_URL+imgArr[0])
//                .apply(options)
//                .apply(bitmapTransform(new BlurTransformation(25)))
                .into(holder.myImageView);

    }

    private void setLike(final ViewHolder holder, final SnsListItem item, int position) {
        if (item.getLike_id()!=0){
            Log.d("ID",item.getLike_id()+" | "+position+" | "+item.getLike_id());
            holder.like.setImageResource(R.drawable.like);
        } else {
            Log.d("ID",item.getLike_id()+" | "+position+" | "+item.getLike_id());
        }

        holder.sns_good.setText(String.valueOf(item.getLike_count()));

        if (item.getLike_user().equals("none")){
            holder.like_users.setVisibility(View.GONE);
        } else if (item.getLike_user().equals(item.getNickname())){
            holder.like_users.setText("본인(?!)");
        } else {
            holder.like_users.setText(item.getLike_user());
        }

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getLike_id()==0){
                    Log.d("LIKE","CLICK");
                    Call<SnsItemLikeDTO> call = like(holder.contentTextView.getTag().toString(),"9");
                    call.enqueue(new Callback<SnsItemLikeDTO>() {
                        @Override
                        public void onResponse(Call<SnsItemLikeDTO> call, Response<SnsItemLikeDTO> response) {
//                           Log.d("LIKE_SUC",response.body().getResult_code()+"");
                            if (response.body().getResult_code()==200){
                                Log.d("Result","LIKE_SUCCESS!!");
                                holder.like.setImageResource(R.drawable.like);
                                holder.sns_good.setText(String.valueOf(item.getLike_count()+1));
                                item.setLike_id(response.body().getResult_body());
                                item.setLike_count(item.getLike_count()+1);
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
                                holder.sns_good.setText(String.valueOf(item.getLike_count()-1));
                                item.setLike_id(0);
                                item.setLike_count(item.getLike_count()-1);
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

    private void setUserIdTextView(final ViewHolder holder, SnsListItem item) {
        //        SpannableString content = new SpannableString(item.getNickname());
//        content.setSpan(new UnderlineSpan(), 0, item.getNickname().length(), 0);
        holder.userIdTextView.setText(item.getNickname());
        holder.userIdTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UserID",holder.userIdTextView.getText().toString());
            }
        });
    }

    private void setHashTagTextView(ViewHolder holder, Resources res, SnsListItem item) {
        HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(res.getColor(R.color.blue), new HashTagHelper.OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {
                Log.d("TAG",hashTag);
            }
        });

        mTextHashTagHelper.handle(holder.contentTextView);

        holder.contentTextView.setText(item.getPost());
        holder.contentTextView.setTag(item.getId());
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

//        int size = 0;
//
//        if (items != null){
//            size =  items.size();
//        }
//        return size;
        return items == null ? 0 : items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contentTextView;
        TextView userIdTextView;
        ImageView like;
        ImageView myImageView;
        TextView sns_good;
        TextView like_users;
//        ViewPager sns_viewPager;
//        LinearLayout sliderDotsPanel;
        ViewHolder(View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.sns_con);
            userIdTextView = itemView.findViewById(R.id.user_id);
            like = itemView.findViewById(R.id.love);
//            sns_viewPager = itemView.findViewById(R.id.sns_viewPager);
//            sliderDotsPanel = itemView.findViewById(R.id.sns_sliderDots);
            myImageView = itemView.findViewById(R.id.main_img);
            sns_good = itemView.findViewById(R.id.sns_good);
            like_users = itemView.findViewById(R.id.like_users);
        }
    }
    public void addNew(List<SnsListItem> items)
    {
        this.items = items;
        notifyDataSetChanged();
    }
    public void removeAll(){
        this.items = null;
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
