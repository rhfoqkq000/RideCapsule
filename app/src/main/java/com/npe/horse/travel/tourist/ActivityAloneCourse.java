package com.npe.horse.travel.tourist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.npe.horse.travel.EndlessRecyclerViewScrollListener;
import com.npe.horse.travel.R;
import com.npe.horse.travel.tourist.detailPage.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
  
    @BindView(R.id.alone_course_progressBar)
    ProgressBar progressBar;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;

    @BindView(R.id.course_alone_img)
    ImageView course_alone_img;

    static TourRecyclerAdapter adapter;

    RetrofitSingleton singleton = RetrofitSingleton.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alone_course);
        ButterKnife.bind(this);

        Picasso.with(getApplicationContext()).load(R.drawable.course_alone_img).into(course_alone_img);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        layoutManager.setInitialPrefetchItemCount(10);
//        layoutManager.setItemPrefetchEnabled(true);
        family_re.setLayoutManager(layoutManager);
        adapter = new TourRecyclerAdapter();
        family_re.setAdapter(adapter);

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                Log.d("SCROLL","END! | "+page);
                progressBar.setVisibility(View.VISIBLE);
                RetrofitSingleton.tourRetrofit(adapter, "C0113", page+1);
                progressBar.setVisibility(View.INVISIBLE);
            }
        };
        family_re.addOnScrollListener(endlessRecyclerViewScrollListener);

        singleton.areaCodeRetrofit();
        singleton.tourRetrofit(adapter,"C0113", 1);
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
