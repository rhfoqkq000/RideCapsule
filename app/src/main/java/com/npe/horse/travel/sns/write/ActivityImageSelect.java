package com.npe.horse.travel.sns.write;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.request.RequestOptions;
import com.npe.horse.travel.MainActivity;
import com.npe.horse.travel.R;
import com.npe.horse.travel.UrlSingleton;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qazz92 on 2017. 10. 10..
 */

public class ActivityImageSelect extends AppCompatActivity {

    @BindView(R.id.toolbar_home)
    Toolbar toolbar;
    @BindView(R.id.select_imgs_recyclerview)
    RecyclerView select_imgs_recyclerview;

    SnsImageSlideAdapter adapter;

    @OnClick(R.id.image_select_nextBtn)
    public void imageSelectNextBtn(){
        Intent intent = new Intent(this, ActivityLocationSelect.class);
        startActivity(intent);
    }

    @OnClick(R.id.image_select_closeBtn)
    public void imageSelectCloseBtn(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 시작과 동시에 휴대폰 갤러리에서 1~10개의 사진을 선택 후 recyclerView에 박음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        select_imgs_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        getImageFromGallery();
    }

    void getImageFromGallery(){
        FishBun.with(ActivityImageSelect.this)
                .MultiPageMode()
                .setMaxCount(10)
                .setMinCount(1)
                .setPickerSpanCount(5)
                .setActionBarColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"), true)
                .setActionBarTitleColor(Color.parseColor("#000000"))
                .setAlbumSpanCount(2, 3)
                .setButtonInAlbumActivity(false)
                .setCamera(true)
                .exceptGif(true)
                .setReachLimitAutomaticClose(true)
                .setAllViewTitle("All")
                .setActionBarTitle("사진선택")
                .textOnNothingSelected("Please select one or more!")
                .startAlbum();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    //FishBun에서 가져온 이미지 uri 저장
                    final ArrayList<Uri> path = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    ImageSingleton.getInstance().setImgUri(path);

                    String IMG_URL = UrlSingleton.getInstance().getSERVER_URL();

                    RequestOptions options = new RequestOptions().placeholder(R.drawable.image_loding);
//                    adapter = new SnsImageSlideAdapter(ActivityImageSelect.this, path, IMG_URL);
                    adapter = new SnsImageSlideAdapter(ActivityImageSelect.this, path);
                    select_imgs_recyclerview.setAdapter(adapter);
                }
        }
    }

    public static Bitmap getResizedBitmapFromUri(Context c, Uri uri)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = 2;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }
}