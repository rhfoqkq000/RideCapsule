package com.example.horse.travel.tourist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.horse.travel.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ekekd on 2017-11-01.
 */

public class ActivityHealingCourse extends AppCompatActivity {

    @BindView(R.id.family_re)
    RecyclerView family_re;

    @BindView(R.id.weather_sky)
    TextView weather_sky;
    @BindView(R.id.weather_tem)
    TextView weather_tem;
    @BindView(R.id.weather_img)
    ImageView weatherImg;



    static TourRecyclerAdapter adapter;

    RetrofitSingleton singleton = RetrofitSingleton.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healing_course);
        ButterKnife.bind(this);

        family_re.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new TourRecyclerAdapter();
        family_re.setAdapter(adapter);

        singleton.areaCodeRetrofit();
        //singleton.weatherRetrofit();
        singleton.tourRetrofit(adapter,"C0114");





    }





/*

    public void tourRetrofit() {
        Retrofit client = new Retrofit.Builder().baseUrl("http://api.visitkorea.or.kr/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        TourListRepo.TourListAppInterface tourService = client.create(TourListRepo.TourListAppInterface.class);

        Call<TourListRepo>  call = tourService.get_tour_retrofit
                ("10", "1", "AND",
                        "TourList",
                        "mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==",
                        "Y", "P", "25", "1", "C01","json");
        call.enqueue(new Callback<TourListRepo>() {
            @Override
            public void onResponse(Call<TourListRepo> call, Response<TourListRepo> response) {
                Log.d("FamilyCourse", response.raw().request().url().toString()); // uri 출력
                Log.d("FamilyCourse", response.body().getResponse().getHeader().getResultMsg());
                ArrayList<TourListRepo.Item> itemList = response.body().getResponse().getBody().getItems().getItem();
//                for (int i = 0; i < response.body().getResponse().getBody().getItems().getItem().size(); i++) {
////                    itemList.Items.Item.class.items.setTitle(response.body().getResponse().getBody().getItems().getItem().get(i).getTitle());
////                    repo.setAddr1(response.body().getResponse().getBody().getItems().getItem().get(i).getAddr1());
////                    repo.setFirstimage(response.body().getResponse().getBody().getItems().getItem().get(i).getFirstimage());
////                    itemList.add(items);
//                    itemList.get(i).getAddr1();
//                }
                adapter.addNew(itemList);
                Log.d("ActivityFamilyCourse", itemList.toString());

//                final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());//아니면 액티비티이름.this
//                layoutManager.setItemPrefetchEnabled(true);
//                family_re.setLayoutManager(layoutManager);
//                adapter = new TourRecyclerAdapter();
//                //ArrayList<String> arrTitle = new ArrayList<>();
//                adapter.addNew(itemList);
//                family_re.setAdapter(adapter);



            }
            @Override
            public void onFailure(Call<TourListRepo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }*/
}
