package com.example.horse.travel.tourist;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.horse.travel.R;
import com.example.horse.travel.UrlSingleton;

import java.util.List;

/**
 * Created by ekekd on 2017-10-29.
 */

public class TourImageRecyclerAdapter extends RecyclerView.Adapter<TourImageRecyclerAdapter.ViewHolder> {

    private final String url = UrlSingleton.getInstance().getSERVER_URL();

    private List<TourListItem> tourListItems;
    Context context;

    TourImageRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //  recyclerview의 아이템 하나 레이아웃 지정 후 리턴
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tour_image_horizontal_recycler,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // item들을 viewHolder로 묶어서 관리함
        // onBindViewHolder는 당장 눈에 보이는 item들에 대해서 호출됨

//        TourItem item = itemList.get(position);
//        Log.d("TEXT",item.getTitle());
//        holder.title.setText(item.getTitle());
//        holder.content.setText(item.getContent());
      
        holder.tour_item_txt.setText(tourListItems.get(position).getTour_title());
//        holder.tour_item_img.setBackground(holder.itemView.getResources().getDrawable(R.drawable.weather01));
        Glide.with(context).load(tourListItems.get(position).getTour_image()).into(holder.tour_item_img);
        Log.e("TourImageRecyclerA", tourListItems.get(position).getTour_title());
    }

    @Override
    public int getItemCount() {
        // recyclerview의 줄 수
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
