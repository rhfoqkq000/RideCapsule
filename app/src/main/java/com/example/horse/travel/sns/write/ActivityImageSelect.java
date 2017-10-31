package com.example.horse.travel.sns.write;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.horse.travel.MainActivity;
import com.example.horse.travel.R;
import com.example.horse.travel.sns.list.SnsImageRecyclerAdapter;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.gpu.InvertFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.KuwaharaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.PixelationFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SwirlFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by qazz92 on 2017. 10. 10..
 */

public class ActivityImageSelect extends AppCompatActivity {

    @BindView(R.id.toolbar_home)
    Toolbar toolbar;
    @BindView(R.id.select_imgs_recyclerview)
    RecyclerView select_imgs_recyclerview;

    SnsImageRecyclerAdapter adapter;

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

                    RequestOptions options = new RequestOptions().placeholder(R.drawable.image_loding);
                    adapter = new SnsImageRecyclerAdapter(Glide.with(this), ActivityImageSelect.this, getContentResolver());
                    adapter.addNew(path);
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