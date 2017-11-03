package com.npe.horse.travel.tourist;

import android.content.Intent;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.npe.horse.travel.EndlessRecyclerViewScrollListener;
import com.npe.horse.travel.R;

import android.widget.ImageView;
import android.widget.TextView;

import com.npe.horse.travel.R;
import com.npe.horse.travel.tourist.detailPage.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.npe.horse.travel.tourist.RetrofitSingleton.areaData;

/**
 * Created by ekekd on 2017-11-01.
 */

public class ActivityFamilyCourse extends AppCompatActivity {


    @BindView(R.id.family_re)
    RecyclerView family_re;
    private ProgressDialog mProgressDialog;

    @BindView(R.id.family_course_progressBar)
    ProgressBar progressBar;

    @BindView(R.id.course_family_img)
    ImageView course_family_img;

    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;

    static TourRecyclerAdapter adapter;

    RetrofitSingleton singleton = RetrofitSingleton.getInstance();

    ArrayList<TourListRepo.Item> itemList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_course);
        ButterKnife.bind(this);

        itemList = new ArrayList<>();

        Picasso.with(getApplicationContext()).load(R.drawable.course_family_img).into(course_family_img);

        progressBar.setVisibility(View.INVISIBLE);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setInitialPrefetchItemCount(10);
        layoutManager.setItemPrefetchEnabled(true);
        family_re.setLayoutManager(layoutManager);
        adapter = new TourRecyclerAdapter();
        family_re.setAdapter(adapter);

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                Log.d("SCROLL","END! | "+page);
                progressBar.setVisibility(View.VISIBLE);
//                RetrofitSingleton.tourRetrofit(adapter, "C0113", page+1);
                tourRetrofit(adapter, "C0112", page+1);
                progressBar.setVisibility(View.INVISIBLE);
            }
        };
        family_re.addOnScrollListener(endlessRecyclerViewScrollListener);


        singleton.areaCodeRetrofit();
        singleton.tourRetrofit(adapter,"C0112", 1);
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
                                Intent detailintent = new Intent(ActivityFamilyCourse.this, DetailActivity.class);
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

    public void tourRetrofit(final TourRecyclerAdapter adapter, String cat2, int page) {
        Retrofit client = new Retrofit.Builder().baseUrl("http://api.visitkorea.or.kr/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        TourListRepo.TourListAppInterface tourService = client.create(TourListRepo.TourListAppInterface.class);

        Call<TourListRepo> call = tourService.get_tour_retrofit
                ("10", String.valueOf(page), "AND",
                        "TourList",
                        "mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==",
                        "Y", "P", "25", areaData.getareaCode(), "C01",cat2,"json");
        call.enqueue(new Callback<TourListRepo>() {
            @Override
            public void onResponse(Call<TourListRepo> call, Response<TourListRepo> response) {
                Log.d("RetrofitSingleTon", response.raw().request().url().toString()); // uri 출력
                Log.d("RetrofitSingleTon", response.body().getResponse().getHeader().getResultMsg());
                itemList.addAll(response.body().getResponse().getBody().getItems().getItem());
                adapter.addNew(itemList);
                adapter.notifyDataSetChanged();
                Log.d("RetrofitSingleTon", itemList.toString());
                TourContentSingleton.getInstance().setTotalCount(response.body().getResponse().getBody().getTotalCount());
            }
            @Override
            public void onFailure(Call<TourListRepo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}

