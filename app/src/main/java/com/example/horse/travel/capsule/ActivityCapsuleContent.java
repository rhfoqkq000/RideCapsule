package com.example.horse.travel.capsule;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.horse.travel.R;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;

/**
 * Created by horse on 2017. 10. 16..
 */

public class ActivityCapsuleContent extends AppCompatActivity {

    private int dotsCount;
    private ImageView[] dots;

    @BindView(R.id.capsule_img_bt)
    Button capsule_img_bt;
    @BindView(R.id.capsule_viewPager)
    ViewPager capsule_viewPager;
    @BindView(R.id.sliderDots)
    LinearLayout sliderDotsPanel;

    @OnClick(R.id.capsule_img_bt)
    void img_select() {
        FishBun.with(ActivityCapsuleContent.this)
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
//        Intent intent = new Intent(ActivityCapsuleContent.this, ActivityCapsule.class);
//        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    //FishBun에서 가져온 이미지 uri 저장
                    ArrayList<Uri> path = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    Log.e("============", path.get(0).toString());
                    ArrayList<File> imgFileArr = new ArrayList<>();
                    SingletonCapsule.getInstance().setLength(path.size());
                    for(int i = 0; i < path.size(); i++){
                        try {
//                            images에 갤러리 사진 url넣기
                            SingletonCapsule.getInstance().setUri(path);
//                            Log.d("================", ""+images[0]);
                            File file = new File(getPath(path.get(i)));
//                            compressor = 이미지 용량 줄여줌 화질이나 사이즈 변화가 얼마나 있는지는 아직 확인안함
                            File compressedImageFile = new Compressor(this).compressToFile(file);
//                            Log.e("AFTER RESIZING SIZE OF FILE"+i, String.valueOf(compressedImageFile.length()/1024));
                            imgFileArr.add(compressedImageFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
                    capsule_viewPager.setAdapter(viewPagerAdapter);

                    dotsCount = viewPagerAdapter.getCount();
                    dots = new ImageView[dotsCount];

                    for(int i = 0; i<dotsCount; i++){
                        dots[i] = new ImageView(this);
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        params.setMargins(8,0,8,0);
                        sliderDotsPanel.addView(dots[i], params);
                    }

                    dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                    capsule_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            for(int i=0; i<dotsCount; i++){
                                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                            }
                            dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    capsule_viewPager.setVisibility(View.VISIBLE);
                    sliderDotsPanel.setVisibility(View.VISIBLE);
                    capsule_img_bt.setVisibility(View.GONE);

//                    InterfaceSnsWrite uploadImage = ApiClient.getClient().create(InterfaceSnsWrite.class);
//                    MultipartBody.Part[] imagesParts = new MultipartBody.Part[imgFileArr.size()];

//                    int user_id = 9;
//
//                    for (int i = 0; i < imgFileArr.size(); i++) {
//                        File file = imgFileArr.get(i);
//                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
//                        imagesParts[i] = MultipartBody.Part.createFormData("imagefile", file.getName(), requestBody);
//                    }
//
//                    Call<SnsWriteDTO> call = uploadImage.writeSns("post",null,imagesParts,user_id);
//                    call.enqueue(new Callback<SnsWriteDTO>() {
//                        @Override
//                        public void onResponse(Call<SnsWriteDTO> call, Response<SnsWriteDTO> response) {
//                            Log.e("IMAGE UPLOAD", ""+response.body().getResult_code());
//                        }
//
//                        @Override
//                        public void onFailure(Call<SnsWriteDTO> call, Throwable t) {
//                            t.printStackTrace();
//                        }
//                    });
//
                    break;
                }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = null;
        try {
            if( uri == null ) {
                return null;
            }
            String[] projection = { MediaStore.Images.Media.DATA };
            cursor = getContentResolver().query(uri, projection, null, null, null);
            if( cursor != null ){
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
        } catch (Exception e){
            Log.e("GET_PATH",e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return uri.getPath();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capsule_content);
        ButterKnife.bind(this);



//        if (capsule_img_bt.getVisibility() == View.VISIBLE) {
//            capsule_viewPager.setVisibility(View.GONE);
//            capsule_img_bt.setVisibility(View.VISIBLE);
//        }else{
//            capsule_viewPager.setVisibility(View.VISIBLE);
//            capsule_img_bt.setVisibility(View.GONE);
//        }
    }
}