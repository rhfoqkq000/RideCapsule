package com.npe.horse.travel.tourist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.npe.horse.travel.R;
import com.npe.horse.travel.tourist.detailPage.DetailActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ekekd on 2017-11-01.
 */

public class ActivityHealingCourse extends AppCompatActivity {

    @BindView(R.id.family_re)
    RecyclerView family_re;

    @BindView(R.id.course_healing_img)
    ImageView course_healing_img;



    static TourRecyclerAdapter adapter;

    RetrofitSingleton singleton = RetrofitSingleton.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healing_course);
        ButterKnife.bind(this);

        Picasso.with(getApplicationContext()).load(R.drawable.course_healing_img).into(course_healing_img);

        family_re.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new TourRecyclerAdapter(Glide.with(getApplicationContext()));
        family_re.setAdapter(adapter);

        singleton.areaCodeRetrofit();
        //singleton.weatherRetrofit();
        singleton.tourRetrofit(adapter,"C0114");

        adapter.setItemClick(new TourRecyclerAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                Call<TourOverviewRepo> call = RetrofitSingleton.overviewRetrofit();
                call.enqueue(new Callback<TourOverviewRepo>() {
                    @Override
                    public void onResponse(Call<TourOverviewRepo> call, Response<TourOverviewRepo> response) {
                        RetrofitSingleton.overview = response.body();
                        Call<SubCourseRepo> call2 = RetrofitSingleton.subcourseRetrofit();
                        call2.enqueue(new Callback<SubCourseRepo>() {
                            @Override
                            public void onResponse(Call<SubCourseRepo> call, Response<SubCourseRepo> response) {
                                RetrofitSingleton.subCourse = response.body();
                                Intent detailintent = new Intent(ActivityHealingCourse.this, DetailActivity.class);
                                startActivity(detailintent);
                            }

                            @Override
                            public void onFailure(Call<SubCourseRepo> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<TourOverviewRepo> call, Throwable t) {
                        t.printStackTrace();
                    }
                });



            }
        });


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
