package com.example.horse.travel.sns.list;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.horse.travel.R;

public class SnsImageSlideAdapter extends PagerAdapter{

    private Context context;
    private String[] imgArr;
    private LayoutInflater layoutInflater;

    SnsImageSlideAdapter(Context context, String[] imgArr) {
        this.context = context;
        this.imgArr = imgArr;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imgArr.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.viewpager_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
//        URL url = null;
//        int height = 0;
//        try {
//            url = new URL(imgArr[position]);
//            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            height = bmp.getHeight();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        imageView.getLayoutParams().height = height;

        // url로부터 이미지 사이즈를 받아서 지정하면 networkOnMainThreadException 발생 스레드 계속 파면 이미지 하나당 만들어져서 에바일듯
        Glide.with(context).load(imgArr[position]).into(imageView);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
//        if (position != mCurrentPosition) {
//            Fragment fragment = (Fragment) object;
//            CustomPager pager = (CustomPager) container;
//            if (fragment != null && fragment.getView() != null) {
//                mCurrentPosition = position;
//                pager.measureCurrentView(fragment.getView());
//            }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}