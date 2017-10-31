package com.example.horse.travel.sns.write;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.horse.travel.R;
import com.example.horse.travel.sns.list.SnsRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pmkjkr on 2017. 10. 31..
 */

public class SnsImageSlideAdapter extends RecyclerView.Adapter<SnsImageSlideAdapter.ViewHolder> {

    // SNS 말고 ActivityImageSelect에서 사용하는 어댑터임

    private ArrayList<Uri> imgArr;
    private Context context;

    public SnsImageSlideAdapter(Context context, ArrayList<Uri> imgArr) {
        this.imgArr = imgArr;
        this.context = context;
    }

    @Override
    public SnsImageSlideAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewpager_item,viewGroup,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SnsImageSlideAdapter.ViewHolder holder, int position) {
        Picasso.with(context).load(imgArr.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imgArr == null ? 0 : imgArr.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_select_imageView);
        }
    }
}