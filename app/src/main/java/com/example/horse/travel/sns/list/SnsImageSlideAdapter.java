package com.example.horse.travel.sns.list;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.horse.travel.R;
import com.example.horse.travel.UrlSingleton;
import com.squareup.picasso.Picasso;

public class SnsImageSlideAdapter extends PagerAdapter{
    private final String IMG_URL = UrlSingleton.getInstance().getSERVER_URL();
    private Context context;
    private String[] imgArr;


    SnsImageSlideAdapter(Context context, String[] imgArr) {
        this.context = context;
        this.imgArr = imgArr;
    }

    @Override
    public int getCount() {
        return imgArr.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.viewpager_item, container, false);
        container.addView(layout);
//        View itemView = layoutInflater.inflate(R.layout.viewpager_item, container, false);

        ImageView imageView = layout.findViewById(R.id.imageView);

        imageView.layout(0, 0, 0, 0);
        Picasso.with(imageView.getContext())
                .load(IMG_URL +imgArr[position])
                .into(imageView);

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
//        glide.load(IMG_URL +imgArr[position]).apply(options).into(imageView);

//        container.addView(itemView);

        return layout;
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