package com.example.horse.travel.sns.list;

import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.horse.travel.R;
import com.example.horse.travel.UrlSingleton;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by qazz92 on 2017. 10. 28..
 */

public class SnsImageRecyclerAdapter extends RecyclerView.Adapter<SnsImageRecyclerAdapter.ViewHolder> {

    private String[] img_urls;
    private Resources res;
    private final String url = UrlSingleton.getInstance().getSERVER_URL();

    SnsImageRecyclerAdapter(String[] img_urls, Resources res) {
        this.img_urls = img_urls;
        this.res = res;
    }

    @Override
    public SnsImageRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sns_image_recycler,parent,false);

        return new SnsImageRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SnsImageRecyclerAdapter.ViewHolder holder, int position) {
//
//        if (getItemCount()>1){
//            int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, res.getDisplayMetrics());
//            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(dp,dp);
//
//            for (int i=1;i<position;i++){
//                ImageView img = new ImageView(holder.itemView.getContext());
//                img.setLayoutParams(lp);
//                img.setImageDrawable(res.getDrawable(R.drawable.tab_indicator_default));
//                holder.ims_layout.addView(img);
//            }
//
//        }

        holder.itemView.layout(0, 0, 0, 0);
        Picasso.with(holder.itemView.getContext())
                .load(url+img_urls[position])
                .into(holder.sns_image);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return img_urls == null ? 0 : img_urls.length;
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sns_image;
//        TextView imgs_count;
        LinearLayout ims_layout;

        ViewHolder(View itemView) {
            super(itemView);
            sns_image = itemView.findViewById(R.id.sns_image);
//            imgs_count = itemView.findViewById(R.id.imgs_count);
//            ims_layout = itemView.findViewById(R.id.ims_layout);


        }
    }
//    private void flipImgCount(final ViewHolder holder){
//        holder.imgs_count.setVisibility(View.VISIBLE);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                holder.imgs_count.setVisibility(View.GONE);
//            }
//        },5000);
//    }
}
