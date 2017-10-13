package com.example.horse.travel.sns.write;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.horse.travel.ApiClient;
import com.example.horse.travel.R;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @BindView(R.id.snsWriteText)
    EditText snsWriteText;


    @OnClick(R.id.testBtn)
    void getAllHashTag(){
        List<String> allHashTags = mTextHashTagHelper.getAllHashTags();
        InterfaceSnsWrite write = ApiClient.getClient().create(InterfaceSnsWrite.class);
        Call<SnsWriteDTO> call = write.writeSns(snsWriteText.getText().toString(),"9",allHashTags);

        call.enqueue(new Callback<SnsWriteDTO>() {
            @Override
            public void onResponse(Call<SnsWriteDTO> call, Response<SnsWriteDTO> response) {
                Toast.makeText(getApplicationContext(),response.body().getResult_body(),Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<SnsWriteDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"글을 저장하는데 실패했습니다. 잠시후 다시 시도해 주세요.",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns_write);
        ButterKnife.bind(this);

        mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.blue), new HashTagHelper.OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {
                Log.d("TAG Click",hashTag);
            }
        });

        mTextHashTagHelper.handle(snsWriteText);

    }
}
