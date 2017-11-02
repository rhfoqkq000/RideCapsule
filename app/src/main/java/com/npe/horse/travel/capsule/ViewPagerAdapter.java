package com.npe.horse.travel.capsule;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.npe.horse.travel.R;

import java.util.ArrayList;

/**
 * Created by horse on 2017. 10. 16..
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Uri> items;

    public ViewPagerAdapter(Context context, ArrayList<Uri> items){
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (items!=null){
           count =  items.size();
        }
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_imageview, null);
        ImageView imageView = (ImageView)view.findViewById(R.id.custom_image);
        Glide.with(context).load(items.get(position)).into(imageView);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
