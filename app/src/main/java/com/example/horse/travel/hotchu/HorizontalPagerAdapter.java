package com.example.horse.travel.hotchu;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.horse.travel.R;
import com.gigamole.infinitecycleviewpager.VerticalInfiniteCycleViewPager;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;
import static com.example.horse.travel.hotchu.Utils.setupItem;

/**
 * Created by qazz92 on 2017. 11. 1..
 */

public class HorizontalPagerAdapter extends PagerAdapter {

    private final Utils.LibraryObject[] LIBRARIES = new Utils.LibraryObject[]{
            new Utils.LibraryObject(
                    R.drawable.active_dot,
                    "Strategy"
            ),
            new Utils.LibraryObject(
                    R.drawable.chat,
                    "Design"
            ),
            new Utils.LibraryObject(
                    R.drawable.go,
                    "Development"
            ),
            new Utils.LibraryObject(
                    R.drawable.ic_launcher_background,
                    "Quality Assurance"
            )
    };

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private boolean mIsTwoWay;

    public HorizontalPagerAdapter(final Context context, final boolean isTwoWay) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mIsTwoWay = isTwoWay;
    }

    @Override
    public int getCount() {
        return mIsTwoWay ? 6 : LIBRARIES.length;
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view;
        view = mLayoutInflater.inflate(R.layout.hot_item, container, false);
        setupItem(view, LIBRARIES[position]);

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
