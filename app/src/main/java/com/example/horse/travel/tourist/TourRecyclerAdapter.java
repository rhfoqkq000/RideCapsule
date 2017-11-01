package com.example.horse.travel.tourist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.horse.travel.R;
import com.example.horse.travel.sns.list.SnsListItem;
import com.example.horse.travel.sns.list.SnsRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ekekd on 2017-11-01.
 */

public class TourRecyclerAdapter extends RecyclerView.Adapter<TourRecyclerAdapter.ViewHolder> {
    private ArrayList<TourListRepo.Item> items;
    //private ArrayList<TourOverviewRepo.Item> overviewitems;


    @Override
    public TourRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tour_re_item,viewGroup,false);
        return new TourRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TourRecyclerAdapter.ViewHolder holder, int position) {
        TourListRepo.Item item = items.get(position);

        holder.family_title.setText(item.getTitle());
        holder.family_content.setText(item.getAddr1());
        //holder.family_content.setText(overviewitems.get(position).getOverview());
        holder.family_readcount.setText(item.getReadcount());
        Picasso.with(holder.itemView.getContext()).load(item.getFirstimage()).into(holder.family_img);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void addNew(ArrayList<TourListRepo.Item> items)
    {
        this.items = items;
        //this.overviewitems = overviewitems;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView family_title ;
        TextView family_content;
        TextView family_readcount;
        ImageView family_img;

        public ViewHolder(View itemView) {
            super(itemView);
            family_title = itemView.findViewById(R.id.family_title);
            family_content = itemView.findViewById(R.id.family_content);
            family_readcount = itemView.findViewById(R.id.family_readcount);
            family_img = itemView.findViewById(R.id.family_img);

        }
    }
}
