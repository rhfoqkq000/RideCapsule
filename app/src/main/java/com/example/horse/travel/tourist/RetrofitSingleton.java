package com.example.horse.travel.tourist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.horse.travel.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ekekd on 2017-11-01.
 */

public class RetrofitSingleton extends AppCompatActivity {


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
        Log.i("city", "city0");
        cityDialog(areaData.getCitys());
    }


    AreaData areaData = new AreaData();
    String[] region = areaData.getSeoUl(); //초기값 서울


    TourOverviewRepo.Item overviewOneItem ;

    private RetrofitSingleton() {}
    private static class Singleton {
        private static final RetrofitSingleton instance = new RetrofitSingleton();
    }

    public static RetrofitSingleton getInstance() {
        return Singleton.instance;
    }



    //Tour Retorofit
    public void tourRetrofit(final TourRecyclerAdapter adapter, String cat2) {
        Retrofit client = new Retrofit.Builder().baseUrl("http://api.visitkorea.or.kr/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        TourListRepo.TourListAppInterface tourService = client.create(TourListRepo.TourListAppInterface.class);

        Call<TourListRepo> call = tourService.get_tour_retrofit
                ("3", "1", "AND",
                        "TourList",
                        "mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==",
                        "Y", "P", "25", "1", "C01",cat2,"json");
        call.enqueue(new Callback<TourListRepo>() {
            @Override
            public void onResponse(Call<TourListRepo> call, Response<TourListRepo> response) {
                Log.d("RetrofitSingleTon", response.raw().request().url().toString()); // uri 출력
                Log.d("RetrofitSingleTon", response.body().getResponse().getHeader().getResultMsg());
                ArrayList<TourListRepo.Item> itemList = response.body().getResponse().getBody().getItems().getItem();
                /*
                ArrayList<TourOverviewRepo.Item> overviewitemList = new ArrayList<TourOverviewRepo.Item>();
                String[] overviews = new String[response.body().getResponse().getBody().getItems().getItem().size()];
                for (int index = 0; index < response.body().getResponse().getBody().getItems().getItem().size(); index++) {
                    //overviews[index] = overviewRetrofit(response.body().getResponse().getBody().getItems().getItem().get(index).getContentid(), index);
                    Log.i("summer", "2");
                    overviewRetrofit(response.body().getResponse().getBody().getItems().getItem().get(index).getContentid(), index);
                    Log.i("summer", index+"");
                    Log.i("summer", overviewitemList.get(index).getOverview().toString());
                    overviewitemList.set(index, overviewOneItem);

                }*/

                adapter.addNew(itemList);
                Log.d("RetrofitSingleTon", itemList.toString());
            }
            @Override
            public void onFailure(Call<TourListRepo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //공통정보조회 Retrofit (코스에 대한 개요, 주소 얻어오기 위해)
    public void overviewRetrofit(String ContentId, final int index) {
        Retrofit client = new Retrofit.Builder().baseUrl("http://api.visitkorea.or.kr/").addConverterFactory(GsonConverterFactory.create()).build();
        TourOverviewRepo.TourOverviewAppInterface overviewService = client.create(TourOverviewRepo.TourOverviewAppInterface.class);
        Call<TourOverviewRepo> call = overviewService.get_overview_retrofit
                ("mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==","1", "1",
                        "AND","TourList",ContentId, "25", "Y", "Y", "json");
        call.enqueue(new Callback<TourOverviewRepo>() {
            @Override
            public void onResponse(Call<TourOverviewRepo> call, Response<TourOverviewRepo> response) {
                Log.d("RetrofitSingleTon", response.raw().request().url().toString()); // uri 출력
                Log.d("RetrofitSingleTon", response.body().getResponse().getHeader().getResultMsg());
                overviewOneItem.setOverview(response.body().getResponse().getBody().getItems().getItem().getOverview().toString());
                //overviewitemList.get(index).setOverview(response.body().getResponse().getBody().getItems().getItem().getOverview());
            }
            @Override
            public void onFailure(Call<TourOverviewRepo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //날씨, 도시 관련 설정 Start
    private void cityDialog(final String[] region) {
        Log.i("city", "city1");
        AlertDialog.Builder cityBuilder = new AlertDialog.Builder(this);//getContext()
        Log.i("city", "city2");
        cityBuilder.setTitle("도시를 선택해주세요");
        cityBuilder.setSingleChoiceItems(region, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 각 리스트를 선택했을때
                        setLatAndLonOfCity(region[whichButton]);
                        cityBtn.setText(region[whichButton]);
                    }
                }).setPositiveButton("선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 선택 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                        weatherRetrofit(areaData.getLat(), areaData.getLon());
                        areaCodeRetrofit();
                        tourRetrofit(ActivityFamilyCourse.adapter, "C0112");
                        //tourRetrofit("C01"); //여행코스
/*                        tourRetrofit(false, "A05"); //맛집
                        tourRetrofit(false, "A02"); //예술, 문화, 역사
                        tourRetrofit(false, "A03"); //레포츠
                        tourRetrofit(false, "A04"); //쇼핑*/
                        //festivalRetrofit(false);
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
    public void weatherRetrofit(String lat, String lon) {
        Retrofit client = new Retrofit.Builder().baseUrl("http://apis.skplanetx.com/").addConverterFactory(GsonConverterFactory.create()).build();
        WeatherRepo.WeatherApiInterface service = client.create(WeatherRepo.WeatherApiInterface.class);
        Call<WeatherRepo> call = service.get_Weather_retrofit(1, lat, lon);
        call.enqueue(new Callback<WeatherRepo>() {
            @Override
            public void onResponse(Call<WeatherRepo> call, Response<WeatherRepo> response) {
//                //현재온도
                Log.i("RetrofitSingleTon", response.body().getWeather().getHourly().get(0).getTemperature().getTc());
                weather_sky.setText(response.body().getWeather().getHourly().get(0).getTemperature().getTc());
                //현재 하늘 상태
                Log.i("RetrofitSingleTon", response.body().getWeather().getHourly().get(0).getSky().getName());
                weather_tem.setText(response.body().getWeather().getHourly().get(0).getSky().getName());
                //하늘 상태에 따른 이미지
                setWeatherImg(response.body().getWeather().getHourly().get(0).getSky().getName());
            }
            @Override
            public void onFailure(Call<WeatherRepo> call, Throwable t) {
                Log.e("RetrofitSingleTon", "날씨정보 불러오기 실패 :" + t.getMessage());
                Log.e("RetrofitSingleTon", "요청 메시지 :" + call.request());
            }
        });
    }
    //도시, 날씨 관련 END

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
                Log.d("RetrofitSingleTon", response.raw().request().url().toString()); // uri 출력
                Log.d("RetrofitSingleTon", response.body().getResponse().getHeader().getResultMsg());

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
