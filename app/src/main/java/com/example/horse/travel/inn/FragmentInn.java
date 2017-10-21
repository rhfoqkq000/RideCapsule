package com.example.horse.travel.inn;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.horse.travel.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by horse on 2017. 10. 9..
 */

public class FragmentInn extends Fragment {

    @BindView(R.id.innTitleItem1)
    TextView innTitleItem1;
    @BindView(R.id.innTitleItem2)
    TextView innTitleItem2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_inn, container, false);
        ButterKnife.bind(this, rootview);
        innRetrofit();
        return rootview;
    }

    public void innRetrofit() {
        Retrofit client = new Retrofit.Builder().baseUrl("http://api.visitkorea.or.kr/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        InnRepo.InnAppInterface tourService = client.create(InnRepo.InnAppInterface.class);
        //요청 파라미터 입력
        Call<InnRepo> call = tourService.get_inn_retrofit("mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "2", "1", "AND", "TourList", "A", "Y", "1", "1", "15", "json");

        call.enqueue(new Callback<InnRepo>() {
            @Override
            public void onResponse(Call<InnRepo> call, Response<InnRepo> response) {
                //파라미터 받아서 처리하기
                Log.d("MainActivity", response.raw().request().url().toString()); // uri 출력
                Log.d("MainActivity", response.body().getResponse().getHeader().getResultMsg());
                innTitleItem1.setText(response.body().getResponse().getBody().getItems().getItem().get(0).getTitle());
                innTitleItem2.setText(response.body().getResponse().getBody().getItems().getItem().get(1).getTitle());
            }

            @Override
            public void onFailure(Call<InnRepo> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }
}
