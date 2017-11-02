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

public class ActivityAloneCourse extends AppCompatActivity {

    @BindView(R.id.family_re)
    RecyclerView family_re;

    @BindView(R.id.course_alone_img)
    ImageView course_alone_img;



    static TourRecyclerAdapter adapter;

    RetrofitSingleton singleton = RetrofitSingleton.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alone_course);
        ButterKnife.bind(this);

        Picasso.with(getApplicationContext()).load(R.drawable.course_alone_img).into(course_alone_img);

        family_re.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new TourRecyclerAdapter(Glide.with(getApplicationContext()));
        family_re.setAdapter(adapter);

        singleton.areaCodeRetrofit();
        //singleton.weatherRetrofit();
        singleton.tourRetrofit(adapter,"C0113");
        //TourContentSingleton.getInstance().getContent_id()
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
                                Intent detailintent = new Intent(ActivityAloneCourse.this, DetailActivity.class);
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
}
