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
import com.example.horse.travel.R;
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

    @BindView(R.id.select_imgs_btn)
    Button select_imgs_btn;

    @OnClick(R.id.image_select_nextBtn)
    public void imageSelectNextBtn(){
        Intent intent = new Intent(this, ActivitySnsWrite.class);
        startActivity(intent);
    }

    @BindView(R.id.select_photo_img1)
    ImageView select_photo_img1;
    @BindView(R.id.select_photo_img2)
    ImageView select_photo_img2;
    @BindView(R.id.select_photo_img3)
    ImageView select_photo_img3;
    @BindView(R.id.select_photo_img4)
    ImageView select_photo_img4;
    @BindView(R.id.select_photo_img5)
    ImageView select_photo_img5;
    @BindView(R.id.select_photo_img6)
    ImageView select_photo_img6;
    @BindView(R.id.select_photo_img7)
    ImageView select_photo_img7;
    @BindView(R.id.select_photo_img8)
    ImageView select_photo_img8;
    @BindView(R.id.select_photo_img9)
    ImageView select_photo_img9;
    @BindView(R.id.select_photo_img10)
    ImageView select_photo_img10;

    ArrayList<ImageView> imgViewArr;

    private PopupWindow mPopupWindow ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        imgViewArr = new ArrayList<>();
        imgViewArr.add(select_photo_img1);
        imgViewArr.add(select_photo_img2);
        imgViewArr.add(select_photo_img3);
        imgViewArr.add(select_photo_img4);
        imgViewArr.add(select_photo_img5);
        imgViewArr.add(select_photo_img6);
        imgViewArr.add(select_photo_img7);
        imgViewArr.add(select_photo_img8);
        imgViewArr.add(select_photo_img9);
        imgViewArr.add(select_photo_img10);

        getImageFromGallery();

        select_imgs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityImageSelect.this, ActivityImageFilter.class);
                startActivity(intent);
            }
        });
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

                    for(int i = 0; i < path.size(); i++){
                        try {
                            path.set(i, bitmapToUri(getResizedBitmapFromUri(getApplicationContext(), path.get(i)), getFileName(path.get(i))));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Glide.with(this)
                                .load(path.get(i))
                                .into(imgViewArr.get(i));
                        final int finalI = i;
                        imgViewArr.get(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                View popupView = getLayoutInflater().inflate(R.layout.dialog_filterselect, null);
                                mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                mPopupWindow.setFocusable(true);
                                mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                                ImageView filterImg1 = popupView.findViewById(R.id.filter1);
                                ImageView filterImg2 = popupView.findViewById(R.id.filter2);
                                ImageView filterImg3 = popupView.findViewById(R.id.filter3);
                                ImageView filterImg4 = popupView.findViewById(R.id.filter4);
                                ImageView filterImg5 = popupView.findViewById(R.id.filter5);
                                ImageView filterImg6 = popupView.findViewById(R.id.filter6);
                                ImageView filterImg7 = popupView.findViewById(R.id.filter7);
                                ImageView filterImg8 = popupView.findViewById(R.id.filter8);
                                ImageView filterImg9 = popupView.findViewById(R.id.filter9);

                                settingImages(filterImg1, path, finalI, bitmapTransform(new ToonFilterTransformation()), getFileName(path.get(finalI)));
                                settingImages(filterImg2, path, finalI, bitmapTransform(new SepiaFilterTransformation()), getFileName(path.get(finalI)));
                                settingImages(filterImg3, path, finalI, bitmapTransform(new SketchFilterTransformation()), getFileName(path.get(finalI)));
                                settingImages(filterImg4, path, finalI, bitmapTransform(new BlurTransformation(3)), getFileName(path.get(finalI)));
                                settingImages(filterImg5, path, finalI, bitmapTransform(new PixelationFilterTransformation()), getFileName(path.get(finalI)));
                                settingImages(filterImg6, path, finalI, bitmapTransform(new KuwaharaFilterTransformation()), getFileName(path.get(finalI)));
                                settingImages(filterImg7, path, finalI, bitmapTransform(new SwirlFilterTransformation()), getFileName(path.get(finalI)));
                                settingImages(filterImg8, path, finalI, bitmapTransform(new VignetteFilterTransformation()), getFileName(path.get(finalI)));
                                settingImages(filterImg9, path, finalI, bitmapTransform(new InvertFilterTransformation()), getFileName(path.get(finalI)));
                            }
                        });
                    }
                    break;
                }
        }
    }

    public void settingImages(final ImageView filterImgView, final ArrayList<Uri> path, final int i, final RequestOptions option, final String fileName){
        filterImgView.setDrawingCacheEnabled(true);
        Glide.with(getApplicationContext()).load(path.get(i))
                .apply(option)
                .into(filterImgView);
        filterImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("ORIGINAL", path.toString());
                Glide.with(getApplicationContext()).load(path.get(i))
                        .apply(option)
                        .into(imgViewArr.get(i));
                Bitmap filter1Bitmap = filterImgView.getDrawingCache();
                ArrayList<Uri> tempUriArr = ImageSingleton.getInstance().getImgUri();
                tempUriArr.set(i, bitmapToUri(filter1Bitmap, fileName));
                ImageSingleton.getInstance().setImgUri(tempUriArr);
                Log.e("TEMP", ImageSingleton.getInstance().getImgUri().toString());
                mPopupWindow.dismiss();
            }
        });
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private Uri bitmapToUri(Bitmap originalBitmap, String fileName) {
        try {
            File f = new File(getApplicationContext().getCacheDir(), fileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            originalBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);

            Bitmap resized = Bitmap.createScaledBitmap(originalBitmap,(int)(originalBitmap.getWidth()*0.8), (int)(originalBitmap.getHeight()*0.8), true);
            resized.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return Uri.fromFile(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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