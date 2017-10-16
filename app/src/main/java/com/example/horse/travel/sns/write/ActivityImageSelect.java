package com.example.horse.travel.sns.write;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;

import com.example.horse.travel.R;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by qazz92 on 2017. 10. 10..
 */

public class ActivityImageSelect extends AppCompatActivity {

    @BindView(R.id.toolbar_home)
    Toolbar toolbar;

    @BindView(R.id.select_imgs_btn)
    Button select_imgs_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getImageFromGallery();

//        select_imgs_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
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
                    ArrayList<Uri> path = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    ArrayList<File> imgFileArr = new ArrayList<>();
                    for(int i = 0; i < path.size(); i++){
                        try {
                            File file = new File(getPath(path.get(i)));
                            //compressor = 이미지 용량 줄여줌 화질이나 사이즈 변화가 얼마나 있는지는 아직 확인안함
                            File compressedImageFile = new Compressor(this).compressToFile(file);
//                            Log.e("AFTER RESIZING SIZE OF FILE"+i, String.valueOf(compressedImageFile.length()/1024));
                            imgFileArr.add(compressedImageFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

//                    InterfaceSnsWrite uploadImage = ApiClient.getClient().create(InterfaceSnsWrite.class);
//                    MultipartBody.Part[] imagesParts = new MultipartBody.Part[imgFileArr.size()];
//
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

}
