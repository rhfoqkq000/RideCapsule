package com.example.horse.travel.hotchu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.horse.travel.R;

import java.util.ArrayList;

/**
 * Created by qazz92 on 2017. 11. 1..
 */

public class ListAdapter extends BaseAdapter {

    private ArrayList<HotchuItem> items;
    private Context context;

    public ListAdapter(ArrayList<HotchuItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.hot_item_index_detail, viewGroup, false);
        }

        TextView num = view.findViewById(R.id.num_hot);
        TextView title = view.findViewById(R.id.title_hot);

        HotchuItem item = (HotchuItem) getItem(position);

        num.setText(String.valueOf(position+1));
        title.setText(item.getTitle());

        return view;
    }
}
