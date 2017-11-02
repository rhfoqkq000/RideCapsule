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
import android.widget.TextView;
import android.widget.Toast;

import com.example.horse.travel.tourist.SubCourseRepo;
import com.npe.horse.travel.ApiClient;
import com.npe.horse.travel.R;
import com.npe.horse.travel.UrlSingleton;
import com.npe.horse.travel.tourist.RetrofitSingleton;
import com.npe.horse.travel.tourist.TourContentSingleton;
import com.npe.horse.travel.tourist.TourListRepo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CardFragment extends Fragment {

    private CardView cardView;
    RetrofitSingleton singleton;

    private final String NUMOFROW = "10";
    private final String PAGE_NO = "1";
    private final String OS = "AND";
    private final String APPNAME = "tourlist";
    private final String CONTENTTYPEID = "25";
    private final String DETAILYN = "Y";
    private final String TYPE = "json";

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

        TextView title = (TextView) view.findViewById(R.id.title);
        Button button = (Button)view.findViewById(R.id.button);

        title.setText(String.format("Card %d", getArguments().getInt("position")));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Button in Card " + getArguments().getInt("position")
                        + "Clicked!", Toast.LENGTH_SHORT).show();
                getDetailJSON();
            }
        });

        return view;
    }

    public CardView getCardView() {
        return cardView;
    }

    private void getDetailJSON(){
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
//                Toast.makeText(getContext(), "?????", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<SubCourseRepo> call, Throwable t) {
                Log.d("URL_BTN",t.getMessage());

            }
        });
    }
}
