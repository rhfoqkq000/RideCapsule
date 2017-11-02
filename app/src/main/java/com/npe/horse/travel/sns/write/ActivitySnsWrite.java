package com.npe.horse.travel.sns.write;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.npe.horse.travel.MainActivity;
import com.npe.horse.travel.R;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by qazz92 on 2017. 10. 10..
 */

public class ActivitySnsWrite extends AppCompatActivity {

    private HashTagHelper mTextHashTagHelper;
    String location, location_alias;

    @BindView(R.id.snsWriteText)
    EditText snsWriteText;

    @OnClick(R.id.testBtn)
    void snsWrite(){
        List<String> allHashTags = mTextHashTagHelper.getAllHashTags();
        Log.e("allHashTags", allHashTags.toString());
        InterfaceSnsWrite write = new Retrofit.Builder()
                .baseUrl("http://168.115.226.218:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceSnsWrite.class);
        RequestBody requestBodyPost = RequestBody.create(MediaType.parse("text/plain"), snsWriteText.getText().toString());
        RequestBody requestBodyLocation = RequestBody.create(MediaType.parse("text/plain"), location);
        RequestBody requestBodyLocation_alias = RequestBody.create(MediaType.parse("text/plain"), location_alias);
        Call<SnsWriteDTO> call = write.writeSns(requestBodyPost, hashtagToRequestBodyArray(allHashTags),
                uriArrToImagesParts(ImageSingleton.getInstance().getImgUri()), 1, requestBodyLocation, requestBodyLocation_alias);
        call.enqueue(new Callback<SnsWriteDTO>() {
            @Override
            public void onResponse(Call<SnsWriteDTO> call, Response<SnsWriteDTO> response) {
                deleteCache(getApplicationContext());
                Toast.makeText(getApplicationContext(),response.body().getResult_body(),Toast.LENGTH_SHORT).show();
                Log.e("ActivitySnsWrite", response.body().getResult_body());
                Intent i = new Intent(ActivitySnsWrite.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }

            @Override
            public void onFailure(Call<SnsWriteDTO> call, Throwable t) {
                deleteCache(getApplicationContext());
                Toast.makeText(getApplicationContext(),"글을 저장하는데 실패했습니다. 잠시후 다시 시도해 주세요.",Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns_write);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        location = intent.getStringExtra("location");
        location_alias = intent.getStringExtra("location_alias");

        mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.blue), new HashTagHelper.OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {
                Log.d("TAG Click",hashTag);
            }
        });

        mTextHashTagHelper.handle(snsWriteText);
    }

    public MultipartBody.Part[] uriArrToImagesParts(ArrayList<Uri> path){
        ArrayList<File> imgFileArr = new ArrayList<>();
        for(int i = 0; i < path.size(); i++){
            try {
                Log.e("ActivitySnsWrite", path.get(i).toString());
//                File file = new File(URI.create(path.get(i).toString()));
                File file = new File(getPath(path.get(i)));
                File compressedImageFile = new Compressor(this).setQuality(75).compressToFile(file);
//                  Log.e("AFTER RESIZING SIZE OF FILE"+i, String.valueOf(compressedImageFile.length()/1024));
                imgFileArr.add(compressedImageFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        MultipartBody.Part[] imagesParts = new MultipartBody.Part[imgFileArr.size()];
        for (int i = 0; i < imgFileArr.size(); i++) {
            File file = imgFileArr.get(i);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            imagesParts[i] = MultipartBody.Part.createFormData("imagefile", file.getName(), requestBody);
        }
        return imagesParts;
    }

    public List<RequestBody> hashtagToRequestBodyArray(List<String> list){
        List<RequestBody> hashArr = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            hashArr.add(RequestBody.create(MediaType.parse("text/plain"), list.get(i)));
        }
        return hashArr;
    }

    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}