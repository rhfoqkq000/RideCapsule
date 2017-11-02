package com.npe.horse.travel.tourist;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.npe.horse.travel.R;
import com.npe.horse.travel.UrlSingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ekekd on 2017-10-30.
 */

public class TourItemVerticalAdapter extends RecyclerView.Adapter<TourItemVerticalAdapter.ViewHolder> {
    private final String url = UrlSingleton.getInstance().getSERVER_URL();

    private List<TourListItem> tourListItems;
    private Context context;
    Activity activity;
    ArrayList<String> arrTitle;
    TourItemVerticalAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public TourItemVerticalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //  recyclerview의 아이템 하나 레이아웃 지정 후 리턴
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tour_image_recycler,parent,false);
        return new TourItemVerticalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TourItemVerticalAdapter.ViewHolder holder, int position) {
        // item들을 viewHolder로 묶어서 관리함
        // onBindViewHolder는 당장 눈에 보이는 item들에 대해서 호출됨

//        TourItem item = itemList.get(position);
//        Log.d("TEXT",item.getTitle());
//        holder.title.setText(item.getTitle());
//        holder.content.setText(item.getContent());
        TourImageRecyclerAdapter adapter = new TourImageRecyclerAdapter(context, Glide.with(context));
        adapter.addNew(tourListItems);
        holder.textView.setText(arrTitle.get(position));
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayout.HORIZONTAL, false));
        Log.e("TourImageRecyclerA1111", "set:::"+adapter.getItemCount());

    }

    @Override
    public int getItemCount() {
        // recyclerview의 줄 수
//        if (tourListItems!=null){
//            return tourListItems.size();
//        } else{
//            return 0;
//        }
        return 3;
    }

    public void addNew(List<TourListItem> items, ArrayList<String> arrTitle){
        this.tourListItems = items;
        this.arrTitle = arrTitle;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView textView;
        ViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.tour_horizontal_recyclerview);
            textView = itemView.findViewById(R.id.tour_horizontal_tv);
        }
    }
}
