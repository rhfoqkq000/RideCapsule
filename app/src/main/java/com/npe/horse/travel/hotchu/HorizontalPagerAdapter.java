package com.npe.horse.travel.hotchu;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.npe.horse.travel.R;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.ArrayList;

import static com.npe.horse.travel.hotchu.Utils.setupItem;
import static com.npe.horse.travel.hotchu.Utils.setupList;

/**
 * Created by qazz92 on 2017. 11. 1..
 */

public class HorizontalPagerAdapter extends PagerAdapter {


    private Context mContext;
    private Activity activity;
    private LayoutInflater mLayoutInflater;

    private boolean mIsTwoWay;

    private ArrayList<HotchuItem> items;

    public HorizontalPagerAdapter(final Context context, Activity activity, final boolean isTwoWay, ArrayList<HotchuItem> items) {
        mContext = context;
        this.activity = activity;
        mLayoutInflater = LayoutInflater.from(context);
        mIsTwoWay = isTwoWay;
        this.items = items;
    }

    @Override
    public int getCount() {
        return mIsTwoWay ? 6 : items.size()+1;
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view;

        if(position==0){
          // 첫번째에는 목록 나열
            view = mLayoutInflater.inflate(R.layout.hot_item_index, container, false);
            setupList(view, items);   
        }else{
            // 두번째부터 마지막까지 뉴스
            view = mLayoutInflater.inflate(R.layout.hot_item, container, false);
            setupItem(view, items.get(position-1),position);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 주소
//                    items.get(position-1).getContent();
                    new FinestWebView.Builder(activity).show(items.get(position-1).getContent());
                }
            });
        }


        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }
}
