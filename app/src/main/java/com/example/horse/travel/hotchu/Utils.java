package com.example.horse.travel.hotchu;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.horse.travel.R;
import com.example.horse.travel.UrlSingleton;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by GIGAMOLE on 8/18/16.
 */
public class Utils {

    public static void setupItem(final View view, final HotchuItem item, int position) {
        final TextView txt_title = view.findViewById(R.id.txt_title);
        final TextView txt_summary = view.findViewById(R.id.txt_summary);
        final TextView index_hot = view.findViewById(R.id.index_hot);

        txt_title.setText(item.getTitle());
        txt_summary.setText(item.getSummary());
        index_hot.setText(String.valueOf(position));

        final ImageView img = view.findViewById(R.id.img_item);
        Picasso.with(img.getContext()).load(UrlSingleton.getInstance().getSERVER_URL()+item.getImg_path()).into(img);
//        img.setImageResource(libraryObject.getRes());
    }
    public static void setupList(final View view, final ArrayList<HotchuItem> item) {
        final ListView listView = view.findViewById(R.id.index_list_hot);
        listView.setAdapter(new ListAdapter(item,view.getContext()));
//        img.setImageResource(libraryObject.getRes());
    }
}
