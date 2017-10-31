package com.example.horse.travel.tourist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.horse.travel.R;
import com.example.horse.travel.capsule.FragmentCapsule;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
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
 * -1028
 * sigunguCode도 설정해서 get~으로 설정
 * ㅇ 축제 데이터 받아올 때 startDate 뭘로 할 지 결정(ex.현재데이터 두달전), endDate 어떻게 할 지 결정 (ex.오늘보다 작은 수)
 * 검색 버튼을 넣을 지, 아니면 다이얼로그 선택할 때마다 새로 가져올 지
 * 날씨 고정해놓음 ( 트래픽 때문에)
 * 옆으로 넘기는 거 찾아보기
 */

public class FragmentTourist extends Fragment {
    @BindView(R.id.tour_recyclerview) RecyclerView tour_recyclerview;

    @BindView(R.id.weather_sky)
    TextView weather_sky;
    @BindView(R.id.weather_tem)
    TextView weather_tem;
    @BindView(R.id.weather_img)
    ImageView weatherImg;

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


//
//    @BindView(R.id.festival_title1)
//    TextView festival_title1;
//    @BindView(R.id.festival_title2)
//    TextView festival_title2;
//    @BindView(R.id.festival_title3)
//    TextView festival_title3;
//    @BindView(R.id.festival_title4)
//    TextView festival_title4;
//    @BindView(R.id.festival_title5)
//    TextView festival_title5;
//
//    @BindView(R.id.tour_title1) TextView tour_title1;
//    @BindView(R.id.tour_title2) TextView tour_title2;
//    @BindView(R.id.tour_title3) TextView tour_title3;
//    @BindView(R.id.tour_title4) TextView tour_title4;
//    @BindView(R.id.tour_title5) TextView tour_title5;
//
//    @BindView(R.id.food_title1) TextView food_title1;
//    @BindView(R.id.food_title2) TextView food_title2;
//    @BindView(R.id.food_title3) TextView food_title3;
//    @BindView(R.id.food_title4) TextView food_title4;
//    @BindView(R.id.food_title5) TextView food_title5;
//
//    @BindView(R.id.arts_title1) TextView arts_title1;
//    @BindView(R.id.arts_title2) TextView arts_title2;
//    @BindView(R.id.arts_title3) TextView arts_title3;
//    @BindView(R.id.arts_title4) TextView arts_title4;
//    @BindView(R.id.arts_title5) TextView arts_title5;
//
//    @BindView(R.id.sports_title1) TextView sports_title1;
//    @BindView(R.id.sports_title2) TextView sports_title2;
//    @BindView(R.id.sports_title3) TextView sports_title3;
//    @BindView(R.id.sports_title4) TextView sports_title4;
//    @BindView(R.id.sports_title5) TextView sports_title5;
//
//    @BindView(R.id.shoping_title1) TextView shoping_title1;
//    @BindView(R.id.shoping_title2) TextView shoping_title2;
//    @BindView(R.id.shoping_title3) TextView shoping_title3;
//    @BindView(R.id.shoping_title4) TextView shoping_title4;
//    @BindView(R.id.shoping_title5) TextView shoping_title5;
//
//    @BindView(R.id.pager)
//    ViewPager mViewPager;

    AreaData areaData = new AreaData();
    String[] region = areaData.getSeoUl(); //초기값 서울
    String minusTwoMonths;
    String minusOneDay;
    TourItemVerticalAdapter adapter;

    public FragmentTourist() {
//        Required empty public constructor
    }

    public static FragmentTourist newInstance(int arg) {
        FragmentTourist fragment = new FragmentTourist();

        // Set the arguments.
        Bundle args = new Bundle();

        args.putInt("ARG", arg);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_tourist, container, false);
        ButterKnife.bind(this, rootview);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        minusTwoMonths = fmt.print(DateTime.now().minusMonths(2));
        minusOneDay = fmt.print(DateTime.now().minusDays(1));



        //날씨 불러옴
        weatherRetrofit(areaData.getLat(), areaData.getLon());
        //areaCode
        areaCodeRetrofit();
        //축제 불러옴
        festivalRetrofit(false);
        //여행지 불러옴
        tourRetrofit(false, "C01"); //여행코스
        tourRetrofit(false, "A05"); //맛집
        tourRetrofit(false, "A02"); //예술, 문화, 역사
        tourRetrofit(false, "A03"); //레포츠
        tourRetrofit(false, "A04"); //쇼핑
        return rootview;
    }


    //***********************************************

    //AppSectionsPagerAdapter mAppSectionsPagerAdapter;



    @Override
    public void onResume() {
        super.onResume();
        weatherRetrofit(areaData.getLat(), areaData.getLon());
    }

    private void cityDialog(final String[] region) {
        AlertDialog.Builder cityBuilder = new AlertDialog.Builder(getContext());
        cityBuilder.setTitle("도시를 선택해주세요");
        cityBuilder.setSingleChoiceItems(region, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 각 리스트를 선택했을때
                        setLatAndLonOfCity(region[whichButton]);
                        cityBtn.setText(region[whichButton]);
                        townBtn.setText("전체");
                    }
                }).setPositiveButton("선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 선택 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                        weatherRetrofit(areaData.getLat(), areaData.getLon());
                        areaCodeRetrofit();
                        tourRetrofit(false, "C01"); //여행코스
                        tourRetrofit(false, "A05"); //맛집
                        tourRetrofit(false, "A02"); //예술, 문화, 역사
                        tourRetrofit(false, "A03"); //레포츠
                        tourRetrofit(false, "A04"); //쇼핑
                        festivalRetrofit(false);
                        weatherRetrofit(areaData.getLat(), areaData.getLon());
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
                        tourRetrofit(true, "C01"); //여행코스
                        tourRetrofit(true, "A05"); //맛집
                        tourRetrofit(true, "A02"); //예술, 문화, 역사
                        tourRetrofit(true, "A01"); //자연
                        tourRetrofit(true, "A03"); //레포츠
                        tourRetrofit(true, "A04"); //쇼핑
                        festivalRetrofit(true);
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

    private void setLatAndLonOfCity(String selectCity) {
        switch (selectCity) {
            case "서울특별시" : setLatAndLonAndRegion("37.540705","126.956764", areaData.getSeoUl()); areaData.setAreaCode("1"); break;
            case "경기도" : setLatAndLonAndRegion("37.567167","127.190292", areaData.getGyeongGi()); areaData.setAreaCode("31"); break;
            case "강원도" : setLatAndLonAndRegion("37.555837", "128.209315", areaData.getGangWon()); areaData.setAreaCode("32"); break;
            case "부산광역시" : setLatAndLonAndRegion("35.198362", "129.053922",areaData.getBuSan()); areaData.setAreaCode("6"); break;
            case "인천광역시" : setLatAndLonAndRegion("37.469221", "126.573234",areaData.getInChen());areaData.setAreaCode("2"); break;
            case "대구광역시" : setLatAndLonAndRegion("35.798838", "128.583052",areaData.getDaeGu()); areaData.setAreaCode("4"); break;
            case "대전광역시" : setLatAndLonAndRegion("36.321655", "127.378953",areaData.getDaeJun()); areaData.setAreaCode("3"); break;
            case "광주광역시" : setLatAndLonAndRegion("35.126033", "126.831302",areaData.getGwangJu()); areaData.setAreaCode("5"); break;
            case "울산광역시" : setLatAndLonAndRegion("35.519301", "129.239078", areaData.getUlSan()); areaData.setAreaCode("7"); break;
            case "세종특별자치시" : setLatAndLonAndRegion("36.483066", "127.289808",areaData.getSeJong()); areaData.setAreaCode("8"); break;
            case "충청북도" : setLatAndLonAndRegion("36.628503", "127.929344",areaData.getChungBuk()); areaData.setAreaCode("33"); break;
            case "충청남도" : setLatAndLonAndRegion("36.557229", "126.779757",areaData.getChungNam()); areaData.setAreaCode("34"); break;
            case "경상북도" : setLatAndLonAndRegion("36.248647", "128.664734",areaData.getGyeongBuk());areaData.setAreaCode("35"); break;
            case "경상남도" : setLatAndLonAndRegion("35.259787", "128.664734",areaData.getGyeongNam()); areaData.setAreaCode("36"); break;
            case "전라북도" : setLatAndLonAndRegion("35.716705", "127.144185",areaData.getJunBuk()); areaData.setAreaCode("37"); break;
            case "전라남도" : setLatAndLonAndRegion("34.819400", "126.893113",areaData.getJunNam()); areaData.setAreaCode("38"); break;
            case "제주도" : setLatAndLonAndRegion("33.364805", "126.542671", areaData.getJeJu()); areaData.setAreaCode("39"); break;
        }
    }
    private void setLatAndLonAndRegion(String lat, String lon, String[] region) {
        areaData.setLat(lat);
        areaData.setLon(lon);
        this.region = region;
    }

    private void setWeatherImg(String currentWeather){
        /*Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd-hh:mm:ss");
        String time = dateFormat.format(date).toString();
        Log.e("날씨", time);*/
        switch (currentWeather) {
            case "맑음" : weatherImg.setImageResource(R.drawable.weather01); break;
            case "구름조금" : weatherImg.setImageResource(R.drawable.weather02); break;
            case "구름많음" : weatherImg.setImageResource(R.drawable.weather03); break;
            case "구름많고 비" : weatherImg.setImageResource(R.drawable.weather12); break;
            case "구름많고 눈" : weatherImg.setImageResource(R.drawable.weather13); break;
            case "구름많고 비 또는 눈" : weatherImg.setImageResource(R.drawable.weather14); break;
            case "흐림" : weatherImg.setImageResource(R.drawable.weather18); break;
            case "흐리고 비" : weatherImg.setImageResource(R.drawable.weather21); break;
            case "흐리고 눈" : weatherImg.setImageResource(R.drawable.weather32); break;
            case "흐리고 비 또는 눈" : weatherImg.setImageResource(R.drawable.weather04); break;
            case "흐리고 낙뢰" : weatherImg.setImageResource(R.drawable.weather29); break;
            case "뇌우, 비" : weatherImg.setImageResource(R.drawable.weather26); break;
            case "뇌우, 눈" : weatherImg.setImageResource(R.drawable.weather27); break;
            case "뇌우, 비또는 눈" : weatherImg.setImageResource(R.drawable.weather28); break;
            default:  weatherImg.setImageResource(R.drawable.weather38);
        }
    }
    // ********************************************************* Retrofit들 *****************************************************************
    private void weatherRetrofit(String lat, String lon) {
        Retrofit client = new Retrofit.Builder().baseUrl("http://apis.skplanetx.com/").addConverterFactory(GsonConverterFactory.create()).build();
        WeatherRepo.WeatherApiInterface service = client.create(WeatherRepo.WeatherApiInterface.class);
        Call<WeatherRepo> call = service.get_Weather_retrofit(1, lat, lon);
        call.enqueue(new Callback<WeatherRepo>() {
            @Override
            public void onResponse(Call<WeatherRepo> call, Response<WeatherRepo> response) {
//                //현재온도
//                Log.i("MainActivity", response.body().getWeather().getHourly().get(0).getTemperature().getTc());
//                weather_sky.setText(response.body().getWeather().getHourly().get(0).getTemperature().getTc());
//                //현재 하늘 상태
//                Log.i("MainActivity", response.body().getWeather().getHourly().get(0).getSky().getName());
//                weather_tem.setText(response.body().getWeather().getHourly().get(0).getSky().getName());
//                //하늘 상태에 따른 이미지
//                setWeatherImg(response.body().getWeather().getHourly().get(0).getSky().getName());
            }
            @Override
            public void onFailure(Call<WeatherRepo> call, Throwable t) {
                Log.e("FragmentTourist", "날씨정보 불러오기 실패 :" + t.getMessage());
                Log.e("FragmentTourist", "요청 메시지 :" + call.request());
            }
        });
    }

    public void festivalRetrofit(boolean siSelect) {
        Retrofit client = new Retrofit.Builder().baseUrl("http://api.visitkorea.or.kr/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        FestivalRepo.FestivalAppInterface festialService = client.create(FestivalRepo.FestivalAppInterface.class);
        //시가 선택됐을 때는 true, 시가 선택되지 않았을 때는 false
        Call<FestivalRepo> call;
        if(siSelect != true) {
            call = festialService.get_festival_retrofit("mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "5", "1", "AND", "TourList", "B", "Y", areaData.getAreaCode(), minusTwoMonths,minusOneDay, "json");
        } else {
            call = festialService.get_festival_retrofit("mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "5", "1", "AND", "TourList", "B", "Y", areaData.getAreaCode(), areaData.getSigunguCode(),  minusTwoMonths,minusOneDay, "json");
        }
        call.enqueue(new Callback<FestivalRepo>() {
            @Override
            public void onResponse(Call<FestivalRepo> call, Response<FestivalRepo> response) {
                Log.d("MainActivity", response.raw().request().url().toString()); // uri 출력
                Log.d("MainActivity", response.body().getResponse().getHeader().getResultMsg());
//                festival_title1.setText(response.body().getResponse().getBody().getItems().getItem().get(0).getTitle());
//                festival_title2.setText(response.body().getResponse().getBody().getItems().getItem().get(1).getTitle());
//                festival_title3.setText(response.body().getResponse().getBody().getItems().getItem().get(2).getTitle());
//                festival_title4.setText(response.body().getResponse().getBody().getItems().getItem().get(3).getTitle());
//                festival_title5.setText(response.body().getResponse().getBody().getItems().getItem().get(4).getTitle());
            }
            @Override
            public void onFailure(Call<FestivalRepo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    public void tourRetrofit(boolean siSelect, final String cat) {
        Retrofit client = new Retrofit.Builder().baseUrl("http://api.visitkorea.or.kr/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        TourListRepo.TourListAppInterface tourService = client.create(TourListRepo.TourListAppInterface.class);
        //시가 선택됐을 때는 true, 시가 선택되지 않았을 때는 false
        Call<TourListRepo> call;
        if(siSelect != true) {
            switch (cat) {
                case "C01" : call = tourService.get_tour_retrofit("5", "1", "AND", "TourList", "mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "Y", "B", "25", areaData.getAreaCode(), cat, "json");break;
                case "A05" : call = tourService.get_tour_retrofit("5", "1", "AND", "TourList", "mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "Y", "B", "39", areaData.getAreaCode(), cat, "json");break;
                case "A02" : call = tourService.get_tour_retrofit("5", "1", "AND", "TourList", "mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "Y", "B", "12", areaData.getAreaCode(), cat, "json");break;
                case "A03" : call = tourService.get_tour_retrofit("5", "1", "AND", "TourList", "mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "Y", "B", "28", areaData.getAreaCode(), cat, "json");break;
                case "A04" : call = tourService.get_tour_retrofit("5", "1", "AND", "TourList", "mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "Y", "B", "38", areaData.getAreaCode(), cat, "json");break;
                default: call = tourService.get_tour_retrofit("5", "1", "AND", "TourList", "mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "Y", "B", "12", areaData.getAreaCode(), cat, "json");break;
            }
        } else {
            switch (cat) {
                case "C01": call = tourService.get_tour_retrofit("5", "1", "AND", "TourList", "mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "Y", "B", "25", areaData.getAreaCode(), areaData.getSigunguCode(), cat, "json");break;
                case "A05": call = tourService.get_tour_retrofit("5", "1", "AND", "TourList", "mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "Y", "B", "39", areaData.getAreaCode(), areaData.getSigunguCode(), cat, "json");break;
                case "A02": call = tourService.get_tour_retrofit("5", "1", "AND", "TourList", "mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "Y", "B", "12", areaData.getAreaCode(), areaData.getSigunguCode(), cat, "json");break;
                case "A03": call = tourService.get_tour_retrofit("5", "1", "AND", "TourList", "mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "Y", "B", "28", areaData.getAreaCode(), areaData.getSigunguCode(), cat, "json");break;
                case "A04": call = tourService.get_tour_retrofit("5", "1", "AND", "TourList", "mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "Y", "B", "38", areaData.getAreaCode(), areaData.getSigunguCode(), cat, "json");break;
                default: call = tourService.get_tour_retrofit("5", "1", "AND", "TourList", "mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "Y", "B", "12", areaData.getAreaCode(), areaData.getSigunguCode(), cat, "json");break;
            }
        }
        call.enqueue(new Callback<TourListRepo>() {
            @Override
            public void onResponse(Call<TourListRepo> call, Response<TourListRepo> response) {
                Log.d("MainActivity", response.raw().request().url().toString()); // uri 출력
                Log.d("MainActivity", response.body().getResponse().getHeader().getResultMsg());
//                switch (cat) {
//                    case "C01" :
                List<TourListItem> itemList = new ArrayList<>();
                for (int i = 0; i < response.body().getResponse().getBody().getItems().getItem().size(); i++) {
                    TourListItem item = new TourListItem();
                    item.setTour_title(response.body().getResponse().getBody().getItems().getItem().get(i).getTitle());
                    item.setTour_image(response.body().getResponse().getBody().getItems().getItem().get(i).getFirstimage());//이미지가 없으면 안불러오려면 어케해야되징..?
                    itemList.add(item);
                }

                Log.d("FragmentTourists", itemList.toString());

                final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setItemPrefetchEnabled(true);
                tour_recyclerview.setLayoutManager(layoutManager);
                adapter = new TourItemVerticalAdapter(getContext(), getActivity());
                ArrayList<String> arrTitle = new ArrayList<>();
                arrTitle.add("축제");
                arrTitle.add("여행코스");
                arrTitle.add("맛집");
                arrTitle.add("예술 문화 역사");
                arrTitle.add("레포츠");
                arrTitle.add("쇼핑");
                adapter.addNew(itemList, arrTitle);
                tour_recyclerview.setAdapter(adapter);
//                        tour_title1.setText(response.body().getResponse().getBody().getItems().getItem().get(0).getTitle());
//                        tour_title2.setText(response.body().getResponse().getBody().getItems().getItem().get(1).getTitle());
//                        tour_title3.setText(response.body().getResponse().getBody().getItems().getItem().get(2).getTitle());
//                        tour_title4.setText(response.body().getResponse().getBody().getItems().getItem().get(3).getTitle());
//                        tour_title5.setText(response.body().getResponse().getBody().getItems().getItem().get(4).getTitle());
//                        break;
//                    case "A05" :
//                        food_title1.setText(response.body().getResponse().getBody().getItems().getItem().get(0).getTitle());
//                        food_title2.setText(response.body().getResponse().getBody().getItems().getItem().get(1).getTitle());
//                        food_title3.setText(response.body().getResponse().getBody().getItems().getItem().get(2).getTitle());
//                        food_title4.setText(response.body().getResponse().getBody().getItems().getItem().get(3).getTitle());
//                        food_title5.setText(response.body().getResponse().getBody().getItems().getItem().get(4).getTitle());
//                        break;
//                    case "A02" :
//                        food_title1.setText(response.body().getResponse().getBody().getItems().getItem().get(0).getTitle());
//                        arts_title2.setText(response.body().getResponse().getBody().getItems().getItem().get(1).getTitle());
//                        arts_title3.setText(response.body().getResponse().getBody().getItems().getItem().get(2).getTitle());
//                        arts_title4.setText(response.body().getResponse().getBody().getItems().getItem().get(3).getTitle());
//                        arts_title5.setText(response.body().getResponse().getBody().getItems().getItem().get(4).getTitle());
//                        break;
//                    case "A03" :
//                        sports_title1.setText(response.body().getResponse().getBody().getItems().getItem().get(0).getTitle());
//                        sports_title2.setText(response.body().getResponse().getBody().getItems().getItem().get(1).getTitle());
//                        sports_title3.setText(response.body().getResponse().getBody().getItems().getItem().get(2).getTitle());
//                        sports_title4.setText(response.body().getResponse().getBody().getItems().getItem().get(3).getTitle());
//                        sports_title5.setText(response.body().getResponse().getBody().getItems().getItem().get(4).getTitle());
//                        break;
//                    case "A04" :
//                        shoping_title1.setText(response.body().getResponse().getBody().getItems().getItem().get(0).getTitle());
//                        shoping_title2.setText(response.body().getResponse().getBody().getItems().getItem().get(1).getTitle());
//                        shoping_title3.setText(response.body().getResponse().getBody().getItems().getItem().get(2).getTitle());
//                        shoping_title4.setText(response.body().getResponse().getBody().getItems().getItem().get(3).getTitle());
//                        shoping_title5.setText(response.body().getResponse().getBody().getItems().getItem().get(4).getTitle());
//                        break;
//                }
            }
            @Override
            public void onFailure(Call<TourListRepo> call, Throwable t) {
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
