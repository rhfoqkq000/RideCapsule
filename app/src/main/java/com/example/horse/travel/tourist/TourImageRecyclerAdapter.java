package com.example.horse.travel.tourist;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.horse.travel.R;
import com.example.horse.travel.UrlSingleton;

import java.util.List;

/**
 * Created by ekekd on 2017-10-29.
 */

public class TourImageRecyclerAdapter extends RecyclerView.Adapter<TourImageRecyclerAdapter.ViewHolder> {

    private String[] img_urls;
    private Resources res;
    private final String url = UrlSingleton.getInstance().getSERVER_URL();

    TourImageRecyclerAdapter(String[] img_urls, Resources res) {
        this.img_urls = img_urls;
        this.res = res;
    }

    private List<TourListItem> tourListItems;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tour_image_recycler,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        TourItem item = itemList.get(position);
//        Log.d("TEXT",item.getTitle());
//        holder.title.setText(item.getTitle());
//        holder.content.setText(item.getContent());
//        holder.tour_item_img.setBackground(holder.itemView.getResources().getDrawable(R.drawable.a));
    }

    @Override
    public int getItemCount() {
        if (tourListItems!=null){
            return tourListItems.size();
        } else{
            return 0;
        }
    }

    public void addNew(List<TourListItem> items){
        this.tourListItems = items;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tour_item_txt;
        ImageView tour_item_img;
//        ImageView imgView;

        ViewHolder(View itemView) {
            super(itemView);
            tour_item_txt = itemView.findViewById(R.id.tour_item_text);
            tour_item_img = itemView.findViewById(R.id.tour_item_img);
        }
    }



}
