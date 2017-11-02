package com.npe.horse.travel.sns.list;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.npe.horse.travel.R;
import com.npe.horse.travel.TimeCal;
import com.npe.horse.travel.sns.comment.SnsCommentActivity;
import com.npe.horse.travel.sns.comment.SnsCommentSingleton;
import com.npe.horse.travel.sns.hashtag.HashTagSingleton;
import com.npe.horse.travel.sns.hashtag.SnsHashTagActivity;
import com.npe.horse.travel.sns.like.SnsItemLike;
import com.npe.horse.travel.sns.like.SnsItemLikeDTO;
import com.npe.horse.travel.sns.like.SnsItemUnLike;
import com.npe.horse.travel.sns.like.SnsItemUnLikeDTO;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by qazz92 on 2017. 10. 12..
 */

public class SnsRecyclerAdapter extends RecyclerView.Adapter<SnsRecyclerAdapter.ViewHolder> {

    private List<SnsListItem> items;
    private RequestManager glide;
    private Resources res;
    private Context context;
    private ContentResolver contentResolver;

    public SnsRecyclerAdapter(RequestManager glide, ContentResolver contentResolver) {
        this.glide=glide;
        this.contentResolver = contentResolver;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_sns,viewGroup,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.imgRe.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int i, int i1) {
                viewHolder.imgs_count.setText(viewHolder.itemView.getResources().getString(R.string.imgs_count_text,i1+1,viewHolder.imgRe.getAdapter().getItemCount()));

                if (i<i1){
                    ImageView img_next = viewHolder.itemView.findViewById(concatenateDigits(viewHolder.getAdapterPosition(),i1));
                    img_next.setImageDrawable(viewHolder.itemView.getResources().getDrawable(R.drawable.tab_indicator_selected));
                    ImageView img_pre = viewHolder.itemView.findViewById(concatenateDigits(viewHolder.getAdapterPosition(),i));
                    img_pre.setImageDrawable(viewHolder.itemView.getResources().getDrawable(R.drawable.tab_indicator_default));
                } else {
                    ImageView img_pre = viewHolder.itemView.findViewById(concatenateDigits(viewHolder.getAdapterPosition(),i1));
                    img_pre.setImageDrawable(viewHolder.itemView.getResources().getDrawable(R.drawable.tab_indicator_selected));
                    ImageView img_next = viewHolder.itemView.findViewById(concatenateDigits(viewHolder.getAdapterPosition(),i));
                    img_next.setImageDrawable(viewHolder.itemView.getResources().getDrawable(R.drawable.tab_indicator_default));
                }

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        setUI(holder, position);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.ims_layout.removeAllViewsInLayout();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, holder.itemView.getResources().getDisplayMetrics());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(dp,dp);
        int length = holder.imgRe.getAdapter().getItemCount();
        for (int i=0;i<length;i++){
            ImageView img = new ImageView(holder.itemView.getContext());
            img.setId(concatenateDigits(holder.getAdapterPosition(),i));
            img.setLayoutParams(lp);
            if (i==0){
                img.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.tab_indicator_selected));
            } else {
                img.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.tab_indicator_default));
            }
            holder.ims_layout.addView(img);
        }
    }

    private void setUI(final ViewHolder holder, int position) {
        res = holder.itemView.getContext().getResources();
        final SnsListItem item = items.get(position);

        setHashTagTextView(holder, res, item);
        setHeaderTextView(holder, item);
        setLike(holder,item, position);
        setImage(holder, item, res);
        setComment(holder, item);
        setUpdatedAt(holder,item);
        setCount(holder,item,res);

    }

    private void setCount(final ViewHolder holder, final SnsListItem item, Resources res) {
        holder.sns_comment_count.setText(res.getString(R.string.comment_count_text,item.getComment_count()));
        holder.sns_comment_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(),SnsCommentActivity.class);
                SnsCommentSingleton singleton = SnsCommentSingleton.getInstance();
                singleton.setItem(item);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    private void setUpdatedAt(ViewHolder holder, SnsListItem item) {
        holder.sns_updated_at.setText(TimeCal.formatTimeString(item.getUpdated_at().getTime()));
    }

    private void setComment(final ViewHolder holder, final SnsListItem item) {
        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(),SnsCommentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                SnsCommentSingleton singleton = SnsCommentSingleton.getInstance();
                singleton.setItem(item);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    private void setImage(final ViewHolder holder, SnsListItem item, final Resources res) {
//        RequestOptions options = new RequestOptions();
//        options.fitCenter().override(Target.SIZE_ORIGINAL, holder.myImageView.getHeight());
//        options.fitCenter();
        final String[] imgArr = item.getImgs().split(",");

        int img_length = imgArr.length;

        holder.imgRe.setFocusable(false);
        holder.imgRe.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        RequestOptions options = new RequestOptions().placeholder(R.drawable.image_loding);
        holder.imgRe.setAdapter(new SnsImageRecyclerAdapter(glide, context, contentResolver, imgArr));
        holder.imgRe.getRecycledViewPool().setMaxRecycledViews(0,img_length);

        if (imgArr.length>1){
            holder.imgs_count.setText(res.getString(R.string.imgs_count_text,1,img_length));
            holder.imgs_count.setVisibility(View.VISIBLE);
        }
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
                    Call<SnsItemLikeDTO> call = like(holder.contentTextView.getTag().toString(),"1");
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
                    Call<SnsItemUnLikeDTO> call = unlike(holder.contentTextView.getTag().toString(),"1");
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

    private void setHeaderTextView(final ViewHolder holder, final SnsListItem item) {
        String location = item.getNickname()+" | location";
        SpannableString content = new SpannableString(location);
        content.setSpan(new UnderlineSpan(), 0, location.length(), 0);
        holder.userIdTextView.setText(item.getNickname());
        holder.userIdTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UserID", String.valueOf(item.getUser_id()));
                Intent intent = new Intent(holder.itemView.getContext(), SnsHashTagActivity.class);
                HashTagSingleton.getInstance().setHash("@"+item.getNickname());
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.locationTextView.setText(item.getLocation_alias());
        holder.sns_location_full.setText(item.getLocation());
        holder.locationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = holder.locationTextView.getText().toString();
                Log.d("location",location);
                Intent intent = new Intent(holder.itemView.getContext(), SnsHashTagActivity.class);
                HashTagSingleton.getInstance().setHash("~"+location);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                holder.itemView.getContext().startActivity(intent);
            }
        });
        Log.e("SnsRecyclerAdapter", item.getUser_profile());

        glide.load(item.getUser_profile()).into(holder.profile_image);
//        glide.load("https://s3.amazonaws.com/xyz.png")
//                .into(new SimpleTarget<Bitmap>(35, 35) {
//                    @Override
//                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//
//                    }
//                });
    }

    private void setHashTagTextView(final ViewHolder holder, Resources res, SnsListItem item) {
        HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(res.getColor(R.color.blue), new HashTagHelper.OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {
                Log.d("TAG",hashTag);
                Intent intent = new Intent(holder.itemView.getContext(), SnsHashTagActivity.class);
                HashTagSingleton.getInstance().setHash("#"+hashTag);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                holder.itemView.getContext().startActivity(intent);
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
        LinearLayout ims_layout;
        ImageView reply;
        TextView sns_updated_at;
        CircleImageView profile_image;
        //        ImageView main_img;
//        CustomPager viewPager;
//        DotsIndicator indicator;
        TextView locationTextView;
//        ViewPager sns_viewPager;
//        LinearLayout sliderDotsPanel;
        TextView sns_comment_count;
        TextView sns_location_full;
        ViewHolder(final View itemView) {
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
            sns_location_full = itemView.findViewById(R.id.sns_location_full);
            ims_layout = itemView.findViewById(R.id.ims_layout);
            reply = itemView.findViewById(R.id.reply);
            sns_updated_at = itemView.findViewById(R.id.sns_updated_at);
            sns_comment_count = itemView.findViewById(R.id.comment_count);
            profile_image = itemView.findViewById(R.id.profile_image);
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
    private static int concatenateDigits(int... digits) {
        StringBuilder sb = new StringBuilder(digits.length);
        for (int digit : digits) {
            sb.append(digit);
        }
        return Integer.parseInt(sb.toString());
    }
}
