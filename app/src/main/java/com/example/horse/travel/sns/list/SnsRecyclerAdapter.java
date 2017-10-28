package com.example.horse.travel.sns.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.media.ImageReader;
import android.support.annotation.Dimension;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.horse.travel.R;
import com.example.horse.travel.sns.like.SnsItemLike;
import com.example.horse.travel.sns.like.SnsItemLikeDTO;
import com.example.horse.travel.sns.like.SnsItemUnLike;
import com.example.horse.travel.sns.like.SnsItemUnLikeDTO;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by qazz92 on 2017. 10. 12..
 */

public class SnsRecyclerAdapter extends RecyclerView.Adapter<SnsRecyclerAdapter.ViewHolder> {

    private List<SnsListItem> items;
    private Context context;
    private RequestManager glide;
    private RequestOptions options;
    private SnsImageSlideAdapter pagerAdapter;

    public SnsRecyclerAdapter(RequestManager glide, RequestOptions options) {
        this.glide=glide;
        this.options=options;
    }

    public void setContext(Context context) {
        this.context = context;
    }
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


//        SpannableString content = new SpannableString(viewpager_item.getNickname());
//        content.setSpan(new UnderlineSpan(), 0, viewpager_item.getNickname().length(), 0);
        setHashTagTextView(holder, res, item);
        setHeaderTextView(holder, item);
        setLike(holder,item, position);
        setImage(holder, item);

//        holder.like.setTag(viewpager_item.getLike_id());

//        RequestOptions options = new RequestOptions();
//        options.fitCenter().override(Target.SIZE_ORIGINAL, holder.myImageView.getHeight());
//        options.fitCenter();

    }

    private void setImage(final ViewHolder holder, SnsListItem item) {
//        RequestOptions options = new RequestOptions();
//        options.fitCenter().override(Target.SIZE_ORIGINAL, holder.myImageView.getHeight());
//        options.fitCenter();
        final String[] imgArr = item.getImgs().split(",");

        pagerAdapter = new SnsImageSlideAdapter(holder.viewPager.getContext(), imgArr,glide,options);
        holder.viewPager.setAdapter(pagerAdapter);
        holder.indicator.setViewPager(holder.viewPager);
//        Glide.with(holder.main_img.getContext())
//                .load("http://220.84.195.101:5000/"+imgArr[0])
////                .apply(options)
////                .apply(bitmapTransform(new BlurTransformation(25)))
//                .into(holder.main_img);
    }

    private void setLike(final ViewHolder holder, final SnsListItem item, int position) {
        if (item.getLike_id()!=0){
            Log.d("ID",item.getLike_id()+" | "+position+" | "+item.getPost());
            glide.load(R.drawable.go).into(holder.like);
//            holder.like.setImageResource(R.drawable.like);
        } else {
            Log.d("ID",item.getLike_id()+" | "+position+" | "+item.getPost());
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
//                                holder.like.setImageResource(R.drawable.like);
                                glide.load(R.drawable.go).into(holder.like);
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
//                                holder.like.setImageResource(R.drawable.normal);
                                glide.load(R.drawable.normal).into(holder.like);
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

    private void setHeaderTextView(final ViewHolder holder, SnsListItem item) {
        String location = item.getNickname()+" | location";
        SpannableString content = new SpannableString(location);
        content.setSpan(new UnderlineSpan(), 0, location.length(), 0);
        holder.userIdTextView.setText(item.getNickname());
        holder.userIdTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UserID",holder.userIdTextView.getText().toString());
            }
        });
        holder.locationTextView.setText(content);
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
        TextView sns_good;
        TextView like_users;
//        ImageView main_img;
        CustomPager viewPager;
        DotsIndicator indicator;
        TextView locationTextView;
//        ViewPager sns_viewPager;
//        LinearLayout sliderDotsPanel;
        ViewHolder(View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.sns_con);
            userIdTextView = itemView.findViewById(R.id.user_id);
            like = itemView.findViewById(R.id.love);
            sns_good = itemView.findViewById(R.id.sns_good);
            like_users = itemView.findViewById(R.id.like_users);
//            main_img = itemView.findViewById(R.id.imageView);
            viewPager = itemView.findViewById(R.id.viewPager);
            indicator = itemView.findViewById(R.id.dots_indicator);
            locationTextView = itemView.findViewById(R.id.sns_location);
        }
    }

    public void addNew(List<SnsListItem> items)
    {
        this.items = items;
        notifyDataSetChanged();
    }
    public void removeAll(){
        this.items.clear();
        this.items = null;
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
