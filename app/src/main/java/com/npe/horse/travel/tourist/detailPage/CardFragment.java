package com.npe.horse.travel.tourist.detailPage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.npe.horse.travel.ApiClient;
import com.npe.horse.travel.R;
import com.npe.horse.travel.UrlSingleton;
import com.npe.horse.travel.tourist.RetrofitSingleton;
import com.npe.horse.travel.tourist.SubCourseRepo;
import com.npe.horse.travel.tourist.TourContentSingleton;

import com.npe.horse.travel.tourist.TourOverviewRepo;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CardFragment extends Fragment {

    private CardView cardView;
    RetrofitSingleton singleton;

    TextView detail_title;
    ImageView detail_img ;
    TextView detail_content;



    public static Fragment getInstance(int position) {
        CardFragment f = new CardFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        f.setArguments(args);

        return f;
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tour_detail_viewpager, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        detail_title = (TextView) view.findViewById(R.id.detail_title);
        detail_img = (ImageView) view.findViewById(R.id.detail_img) ;
        detail_content = (TextView) view.findViewById(R.id.detail_content) ;

        if ( getArguments().getInt("position") == 0 ){
            TourOverviewRepo.Item item = RetrofitSingleton.getOverview().getResponse().getBody().getItems().getItem();
            detail_title.setText("미리보기");
            Picasso.with(getContext()).load(item.getFirstimage()).into(detail_img);
            detail_content.setText(item.getOverview());
        } else {
            SubCourseRepo.Item[] items = RetrofitSingleton.getSubCourse().getResponse().getBody().getItems().getItem();
            detail_title.setText(items[getArguments().getInt("position") - 1].getSubname());
            Picasso.with(getContext()).load(items[getArguments().getInt("position") -1].getSubdetailimg()).into(detail_img);
            detail_content.setText(items[getArguments().getInt("position") -1].getSubdetailoverview());
        }
        //detail_title.setText(String.format("Card %d", getArguments().getInt("position")));
/*        detail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*Toast.makeText(getActivity(), "Button in Card " + getArguments().getInt("position")
                        + "Clicked!", Toast.LENGTH_SHORT).show();*//*
                getDetailJSON();
            }
        });*/

        return view;
    }

    public CardView getCardView() {
        return cardView;
    }

/*    private void getDetailJSON(){
//        Retrofit client = new Retrofit.Builder().baseUrl("http://api.visitkorea.or.kr/")
//                .addConverterFactory(GsonConverterFactory.create()).build();
//        SubCourseRepo.SubCourseAppInterface subCourseAppInterface = client.create(SubCourseRepo.SubCourseAppInterface.class);
        SubCourseRepo.SubCourseAppInterface retrofit = ApiClient.getPublicClient().create(SubCourseRepo.SubCourseAppInterface.class);
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://api.visitkorea.or.kr/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        SubCourseRepo.SubCourseAppInterface subCourseAppInterface = retrofit.create(SubCourseRepo.SubCourseAppInterface.class);
        Call<SubCourseRepo> call = retrofit.get_subcourse_retrofit(UrlSingleton.getInstance().serviceKey(),NUMOFROW,PAGE_NO,OS,APPNAME, TourContentSingleton.getInstance().getContent_id(),CONTENTTYPEID,DETAILYN,TYPE);
        call.enqueue(new Callback<SubCourseRepo>() {
            @Override
            public void onResponse(Call<SubCourseRepo> call, Response<SubCourseRepo> response) {
                Log.d("URL_BTN",response.raw().request().url().toString());
                //Toast.makeText(getContext(), "?????", Toast.LENGTH_SHORT).show();
                if ( getArguments().getInt("position") == 0 ){
                    overviewRetrofit();
                } else {
                    detail_title.setText(response.body().getResponse().getBody().getItems().getItem()[getArguments().getInt("position")].getSubname());
                    Picasso.with(getContext()).load(response.body().getResponse().getBody().getItems().getItem()[getArguments().getInt("position")].getSubdetailimg()).into(detail_img);
                    detail_content.setText(response.body().getResponse().getBody().getItems().getItem()[getArguments().getInt("position")].getSubdetailoverview());
                }
            }

            @Override
            public void onFailure(Call<SubCourseRepo> call, Throwable t) {
                Log.d("URL_BTN",t.getMessage());

            }
        });
    }*/




}
