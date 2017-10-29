package com.example.horse.travel.sns.list;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.horse.travel.R;
import com.example.horse.travel.sns.like.SnsItemLike;
import com.example.horse.travel.sns.like.SnsItemLikeDTO;
import com.example.horse.travel.sns.like.SnsItemUnLike;
import com.example.horse.travel.sns.like.SnsItemUnLikeDTO;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.squareup.picasso.Picasso;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by qazz92 on 2017. 10. 12..
 */

public class SnsRecyclerAdapter extends RecyclerView.Adapter<SnsRecyclerAdapter.ViewHolder> {

    private List<SnsListItem> items;
    private RequestManager glide;
    private PagerAdapter pagerAdapter;
    private int childP = 0;
    private int iml =  0;

    public SnsRecyclerAdapter(RequestManager glide) {
        this.glide=glide;
        if (!EventBus.getDefault().isRegistered(this)) { EventBus.getDefault().register(this); }
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

    @Subscribe
    public void onEvent(PositionEvent event){
        childP = event.getPosition();
        Log.d("Child",""+childP);
    }

//    @Override
//    public void onViewAttachedToWindow(ViewHolder holder) {
//        super.onViewAttachedToWindow(holder);
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onViewDetachedFromWindow(ViewHolder holder) {
//        super.onViewDetachedFromWindow(holder);
//        EventBus.getDefault().unregister(this);
//    }

    private void setUI(final ViewHolder holder, int position) {
        Resources res = holder.itemView.getContext().getResources();
        final SnsListItem item = items.get(position);


//        SpannableString content = new SpannableString(viewpager_item.getNickname());
//        content.setSpan(new UnderlineSpan(), 0, viewpager_item.getNickname().length(), 0);
        setHashTagTextView(holder, res, item);
        setHeaderTextView(holder, item);
        setLike(holder,item, position);
        setImage(holder, item, res);

//        holder.like.setTag(viewpager_item.getLike_id());

//        RequestOptions options = new RequestOptions();
//        options.fitCenter().override(Target.SIZE_ORIGINAL, holder.myImageView.getHeight());
//        options.fitCenter();

    }

    private void setImage(final ViewHolder holder, SnsListItem item, Resources res) {
//        RequestOptions options = new RequestOptions();
//        options.fitCenter().override(Target.SIZE_ORIGINAL, holder.myImageView.getHeight());
//        options.fitCenter();
        final String[] imgArr = item.getImgs().split(",");
        iml = imgArr.length;
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.imgRe.getContext(), LinearLayoutManager.HORIZONTAL, false);
        holder.imgRe.setFocusable(false);
        holder.imgRe.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        holder.imgRe.setAdapter(new SnsImageRecyclerAdapter(imgArr,res));
        holder.imgRe.getRecycledViewPool().setMaxRecycledViews(0,imgArr.length);


//        PagerSnapHelper snapHelper = new PagerSnapHelper();
////        snapHelper.attachToRecyclerView(holder.imgRe);
//        holder.imgRe.setOnFlingListener(null);
//        snapHelper.attachToRecyclerView(holder.imgRe);
//
//
//        holder.imgRe.setOnFlingListener(new RecyclerView.OnFlingListener() {
//            @Override
//            public boolean onFling(int velocityX, int velocityY) {
//                Log.d("ONFLING",velocityX+" | "+velocityY);
//                return false;
//            }
//        });
        if (imgArr.length>1){
            holder.imgs_count.setText(res.getString(R.string.imgs_count_text,childP,imgArr.length));
            holder.imgs_count.setVisibility(View.VISIBLE);
        }

//        pagerAdapter = new SnsImageSlideAdapter(holder.viewPager.getContext(), imgArr);
//        holder.viewPager.setAdapter(pagerAdapter);
//        holder.indicator.setViewPager(holder.viewPager);
//        Glide.with(holder.main_img.getContext())
//                .load("http://220.84.195.101:5000/"+imgArr[0])
////                .apply(options)
////                .apply(bitmapTransform(new BlurTransformation(25)))
//                .into(holder.main_img);
    }

    private void setLike(final ViewHolder holder, final SnsListItem item, int position) {
        if (item.getLike_id()!=0){
            glide.load(R.drawable.go).into(holder.like);
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
        RecyclerViewPager imgRe;
        TextView imgs_count;
//        ImageView main_img;
//        CustomPager viewPager;
//        DotsIndicator indicator;
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
            imgRe = itemView.findViewById(R.id.img_re);
            imgs_count = itemView.findViewById(R.id.imgs_count);
//            main_img = itemView.findViewById(R.id.imageView);
//            viewPager = itemView.findViewById(R.id.viewPager);
//            indicator = itemView.findViewById(R.id.dots_indicator);
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
