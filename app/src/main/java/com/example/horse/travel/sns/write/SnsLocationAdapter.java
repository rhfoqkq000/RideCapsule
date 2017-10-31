package com.example.horse.travel.sns.write;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.horse.travel.R;

import java.util.List;

/**
 * Created by pmkjkr on 2017. 10. 29..
 */

public class SnsLocationAdapter extends RecyclerView.Adapter<SnsLocationAdapter.ViewHolder>{
    private List<SnsLocationInfo> items;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_location,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.location_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("SnsLocationAdapter", "눌렀음ㅎㅎ");
                SnsLocationTextViewSingleton.getInstance().getLoaction_et().setText(items.get(holder.getAdapterPosition()).getLocation());
                SnsLocationTextViewSingleton.getInstance().getLocation_alias_et().setText(items.get(holder.getAdapterPosition()).getLcoation_alias());
            }
        });
        holder.location.setText(items.get(position).getLocation());
        holder.location_alias.setText(items.get(position).getLcoation_alias());
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView location;
        TextView location_alias;
        LinearLayout location_layout;
        ViewHolder(View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.location_tv);
            location_alias = itemView.findViewById(R.id.location_alias_tv);
            location_layout = itemView.findViewById(R.id.location_layout);
        }
    }

    void addNew(List<SnsLocationInfo> items)
    {
        this.items = items;
        notifyDataSetChanged();
    }
}