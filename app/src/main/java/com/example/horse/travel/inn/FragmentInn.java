package com.example.horse.travel.inn;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.horse.travel.R;
import com.example.horse.travel.capsule.FragmentCapsule;
import com.example.horse.travel.tourist.AreaData;
import com.example.horse.travel.tourist.AreaRepo;

import java.util.ArrayList;
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
 * Created by horse on 2017. 10. 9..
 */

public class FragmentInn extends Fragment {

    @BindView(R.id.inn_recyclerview)
    RecyclerView inn_recyclerview;

    @BindView(R.id.city)
    Button cityBtn;
    @OnClick(R.id.city)
    void clickCity(){
        cityDialog(areaData.getCitys());
    }
    @BindView(R.id.town)
    Button townBtn;
    @OnClick(R.id.town)
    void clickTown(){
        townDialog(region);
    }

    AreaData areaData = new AreaData();
    String[] region = areaData.getSeoUl(); //초기값 서울
    TourItemVerticalAdapter adapter;

/*    @BindView(R.id.innTitleItem1)
    TextView innTitleItem1;
    @BindView(R.id.innTitleItem2)
    TextView innTitleItem2;*/
  
    public static FragmentInn newInstance(int arg) {
        FragmentInn fragment = new FragmentInn();

        // Set the arguments.
        Bundle args = new Bundle();

        args.putInt("ARG", arg);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_inn, container, false);
        ButterKnife.bind(this, rootview);
        innRetrofit(false,"0");
        innRetrofit(false,"1");
        return rootview;
    }



    private void cityDialog(final String[] region) {
        AlertDialog.Builder cityBuilder = new AlertDialog.Builder(getContext());
        cityBuilder.setTitle("도시를 선택해주세요");
        cityBuilder.setSingleChoiceItems(region, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 각 리스트를 선택했을때
                        setAreaCode(region[whichButton]);
                        cityBtn.setText(region[whichButton]);
                        townBtn.setText("전체");
                    }
                }).setPositiveButton("선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 선택 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                        areaCodeRetrofit();
                        innRetrofit(false,"0");
                        innRetrofit(false,"1");


                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 취소 버튼 클릭시
                        dialog.cancel();
                    }
                });
        cityBuilder.show();
    }
    private void townDialog(final String[] region) {
        AlertDialog.Builder townBuilder = new AlertDialog.Builder(getContext());
        townBuilder.setTitle("상세 지역을 선택해주세요");
        townBuilder.setSingleChoiceItems(region, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 각 리스트를 선택했을때
                        townBtn.setText(region[whichButton]);
                        areaData.setSigunguName(region[whichButton]);
                    }
                }).setPositiveButton("선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        areaCodeRetrofit();
                        innRetrofit(false,"0");
                        innRetrofit(false,"1");
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 취소 버튼 클릭시
                        dialog.cancel();
                    }
                });
        townBuilder.show();
    }

    private void setAreaCode(String selectCity) {
        switch (selectCity) {
            case "서울특별시" : region = areaData.getSeoUl(); areaData.setAreaCode("1"); break;
            case "경기도" : region = areaData.getGyeongGi(); areaData.setAreaCode("31"); break;
            case "강원도" : region = areaData.getGangWon(); areaData.setAreaCode("32"); break;
            case "부산광역시" : region = areaData.getBuSan(); areaData.setAreaCode("6"); break;
            case "인천광역시" : region = areaData.getInChen(); areaData.setAreaCode("2"); break;
            case "대구광역시" : region = areaData.getDaeGu(); areaData.setAreaCode("4"); break;
            case "대전광역시" : region = areaData. getDaeJun(); areaData.setAreaCode("3"); break;
            case "광주광역시" : region = areaData.getGwangJu(); areaData.setAreaCode("5"); break;
            case "울산광역시" : region = areaData.getUlSan(); areaData.setAreaCode("7"); break;
            case "세종특별자치시" : region = areaData.getSeJong(); areaData.setAreaCode("8"); break;
            case "충청북도" :region =  areaData.getChungBuk(); areaData.setAreaCode("33"); break;
            case "충청남도" : region = areaData.getChungNam(); areaData.setAreaCode("34"); break;
            case "경상북도" : region = areaData.getGyeongBuk(); areaData.setAreaCode("35"); break;
            case "경상남도" : region = areaData.getGyeongNam(); areaData.setAreaCode("36"); break;
            case "전라북도" : region = areaData.getJunBuk(); areaData.setAreaCode("37"); break;
            case "전라남도" : region = areaData.getJunNam(); areaData.setAreaCode("38"); break;
            case "제주도" :region =  areaData.getJeJu(); areaData.setAreaCode("39"); break;
        }
    }


    //*****************************************RETROFIT**********************************************
    public void innRetrofit(boolean selectSi, String goodStay) {
        Retrofit client = new Retrofit.Builder().baseUrl("http://api.visitkorea.or.kr/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        InnRepo.InnAppInterface tourService = client.create(InnRepo.InnAppInterface.class);
        //시가 선택되있으면 true, goodStay가 0이면 굿스테이 아님(1이면 굿스테이)
        Call<InnRepo> call ;
        if (selectSi != true) {
            call = tourService.get_inn_retrofit("mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "2", "1", "AND", "TourList", "P", "Y", areaData.getAreaCode(), "32",goodStay, "json");
        } else {
            call = tourService.get_inn_retrofit("mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "2", "1", "AND", "TourList", "P", "Y", areaData.getAreaCode(), areaData.getSigunguCode(), "32",goodStay, "json");
        }

        call.enqueue(new Callback<InnRepo>() {
            @Override
            public void onResponse(Call<InnRepo> call, Response<InnRepo> response) {
                //파라미터 받아서 처리하기
                Log.d("MainActivity", response.raw().request().url().toString()); // uri 출력
                Log.d("MainActivity", response.body().getResponse().getHeader().getResultMsg());
                Log.d("MainActivity", response.body().getResponse().getBody().getItems().getItem().size()+"");
                //innTitleItem1.setText(response.body().getResponse().getBody().getItems().getItem().get(0).getTitle());
                //innTitleItem2.setText(response.body().getResponse().getBody().getItems().getItem().get(1).getTitle());
                List<TourListItem> itemList = new ArrayList<>();
                for (int i = 0; i < response.body().getResponse().getBody().getItems().getItem().size(); i++) {
                    TourListItem item = new TourListItem();
                    item.setTour_title(response.body().getResponse().getBody().getItems().getItem().get(i).getTitle());
                    item.setTour_image(response.body().getResponse().getBody().getItems().getItem().get(i).getFirstimage());
                    itemList.add(item);
                }

                Log.d("FragmentTourists", itemList.toString());

                final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setItemPrefetchEnabled(true);
                inn_recyclerview.setLayoutManager(layoutManager);
                adapter = new TourItemVerticalAdapter(getContext(), getActivity());
                ArrayList<String> arrTitle = new ArrayList<>();
                arrTitle.add("한국관광공사 인증 우수 숙박 업체 ");
                arrTitle.add("일반 숙박 업체");
                adapter.addNew(itemList, arrTitle);
                inn_recyclerview.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<InnRepo> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }
    public void areaCodeRetrofit() {
        Retrofit client = new Retrofit.Builder().baseUrl("http://api.visitkorea.or.kr/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        AreaRepo.areaCodeAppInterface areaCodeService = client.create(AreaRepo.areaCodeAppInterface.class);
        //요청 파라미터 입력
        Call<AreaRepo> call = areaCodeService.get_areaCode_retrofit("mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "40", "1", "AND", "TourList", areaData.getAreaCode(), "json");

        call.enqueue(new Callback<AreaRepo>() {
            @Override
            public void onResponse(Call<AreaRepo> call, Response<AreaRepo> response) {
                //파라미터 받아서 처리하기
                Log.d("MainActivity", response.raw().request().url().toString()); // uri 출력
                Log.d("MainActivity", response.body().getResponse().getHeader().getResultMsg());

                //areaData.setSigunguCodes();
                for (int index = 0; index < response.body().getResponse().getBody().getItems().getItem().size(); index++) {
                    if (response.body().getResponse().getBody().getItems().getItem().get(index).getName().equals(areaData.getSigunguName())){
                        areaData.setSigunguCode(response.body().getResponse().getBody().getItems().getItem().get(index).getCode());
                        return;
                    }
                    //[areaData.getAreaCode()][index] =
                    //Log.d("시들", response.body().getResponse().getBody().getItems().getItem().get(index).getCode());
                }
            }
            @Override
            public void onFailure(Call<AreaRepo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
