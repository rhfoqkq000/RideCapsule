package com.example.horse.travel.tourist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.horse.travel.R;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;

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

    @BindView(R.id.festival_title1)
    TextView festival_title1;
    @BindView(R.id.festival_title2)
    TextView festival_title2;
    @BindView(R.id.festival_title3)
    TextView festival_title3;
    @BindView(R.id.festival_title4)
    TextView festival_title4;
    @BindView(R.id.festival_title5)
    TextView festival_title5;

    @BindView(R.id.tour_title1)
    TextView tour_title1;
    @BindView(R.id.tour_title2)
    TextView tour_title2;
    @BindView(R.id.tour_title3)
    TextView tour_title3;
    @BindView(R.id.tour_title4)
    TextView tour_title4;
    @BindView(R.id.tour_title5)
    TextView tour_title5;

    @BindView(R.id.areaCodeTest)
    TextView areaCodeTest;
    @BindView(R.id.sigunguCodeTest)
    TextView sigunguCodeTest;

    @BindView(R.id.pager)
    ViewPager mViewPager;

    AreaData areaData = new AreaData();
    String minusTwoMonths;
    String minusOneDay;


    public FragmentTourist() {
//        Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_tourist, container, false);
        ButterKnife.bind(this, rootview);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        minusTwoMonths = fmt.print(DateTime.now().minusMonths(2));
        minusOneDay = fmt.print(DateTime.now().minusDays(1));

        //날씨 불러옴
        //weatherRetrofit(areaData.getLat(), areaData.getLon());
        //areaCode
        areaCodeRetrofit();
        //축제 불러옴
        festivalRetrofit(false);
        //여행지 불러옴
        tourRetrofit(false);



        //프레그먼트 생성
        //fragment_create(areaData.citys);
        return rootview;
    }


    //***********************************************

    //AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    String[] region = areaData.getSeoUl(); //초기값 서울

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
                        //weatherRetrofit(areaData.getLat(), areaData.getLon());
                        areaCodeRetrofit();
                        tourRetrofit(false);
                        festivalRetrofit(false);
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
                        areaData.setSigunguName( region[whichButton]);
                    }
                }).setPositiveButton("선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        areaCodeRetrofit();
                        tourRetrofit(true);
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
/*    private void sigunguCodeInit(){
        mRealm = Realm.getInstance(this.getContext());
        RealmResults<sigunguCodeVO> sigunguCodeList = getsigunguCodeList();
        Log.i("시군구 코드", sigunguCodeList.size()+"");
    }
    private RealmResults<sigunguCodeVO>  getsigunguCodeList(){
        return mRealm.where(sigunguCodeVO.class).findAll();
    }*/
    // ********************************************************* Retrofit들 *****************************************************************
    private void weatherRetrofit(String lat, String lon) {
        Retrofit client = new Retrofit.Builder().baseUrl("http://apis.skplanetx.com/").addConverterFactory(GsonConverterFactory.create()).build();
        WeatherRepo.WeatherApiInterface service = client.create(WeatherRepo.WeatherApiInterface.class);
        Call<WeatherRepo> call = service.get_Weather_retrofit(1, lat, lon);
        call.enqueue(new Callback<WeatherRepo>() {
            @Override
            public void onResponse(Call<WeatherRepo> call, Response<WeatherRepo> response) {
                //현재온도
                Log.i("MainActivity", response.body().getWeather().getHourly().get(0).getTemperature().getTc());
                weather_sky.setText(response.body().getWeather().getHourly().get(0).getTemperature().getTc());
                //현재 하늘 상태
                Log.i("MainActivity", response.body().getWeather().getHourly().get(0).getSky().getName());
                weather_tem.setText(response.body().getWeather().getHourly().get(0).getSky().getName());
                //하늘 상태에 따른 이미지
                setWeatherImg(response.body().getWeather().getHourly().get(0).getSky().getName());
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
            call = festialService.get_festival_retrofit("mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "5", "1", "AND", "TourList", "B", "Y", areaData.getAreaCode(), "1",  minusTwoMonths,minusOneDay, "json");
        }
        call.enqueue(new Callback<FestivalRepo>() {
            @Override
            public void onResponse(Call<FestivalRepo> call, Response<FestivalRepo> response) {
                //파라미터 받아서 처리하기
                Log.d("MainActivity", response.raw().request().url().toString()); // uri 출력
                Log.d("MainActivity", response.body().getResponse().getHeader().getResultMsg());
                festival_title1.setText(response.body().getResponse().getBody().getItems().getItem().get(0).getTitle());
                festival_title2.setText(response.body().getResponse().getBody().getItems().getItem().get(1).getTitle());
                festival_title3.setText(response.body().getResponse().getBody().getItems().getItem().get(2).getTitle());
                festival_title4.setText(response.body().getResponse().getBody().getItems().getItem().get(3).getTitle());
                festival_title5.setText(response.body().getResponse().getBody().getItems().getItem().get(4).getTitle());
            }

            @Override
            public void onFailure(Call<FestivalRepo> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }
    public void tourRetrofit(boolean siSelect) {
        Retrofit client = new Retrofit.Builder().baseUrl("http://api.visitkorea.or.kr/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        TourListRepo.TourListAppInterface tourService = client.create(TourListRepo.TourListAppInterface.class);
        //시가 선택됐을 때는 true, 시가 선택되지 않았을 때는 false
        Call<TourListRepo> call;
        if(siSelect != true) {
             call = tourService.get_tour_retrofit("5", "1", "AND", "TourList", "mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "Y", "B", "12", areaData.getAreaCode(), "json");
        } else {
             call = tourService.get_tour_retrofit("5", "1", "AND", "TourList", "mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==", "Y", "B", "12", areaData.getAreaCode(), "2", "json");
        }
        call.enqueue(new Callback<TourListRepo>() {
            @Override
            public void onResponse(Call<TourListRepo> call, Response<TourListRepo> response) {
                //파라미터 받아서 처리하기
                Log.d("MainActivity", response.raw().request().url().toString()); // uri 출력
                Log.d("MainActivity", response.body().getResponse().getHeader().getResultMsg());
                tour_title1.setText(response.body().getResponse().getBody().getItems().getItem().get(0).getTitle());
                tour_title2.setText(response.body().getResponse().getBody().getItems().getItem().get(1).getTitle());
                tour_title3.setText(response.body().getResponse().getBody().getItems().getItem().get(2).getTitle());
                tour_title4.setText(response.body().getResponse().getBody().getItems().getItem().get(3).getTitle());
                tour_title5.setText(response.body().getResponse().getBody().getItems().getItem().get(4).getTitle());
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
                areaCodeTest.setText(areaData.getAreaCode());
                sigunguCodeTest.setText(response.body().getResponse().getBody().getItems().getItem().get(0).getName());
                Log.d("soTest", areaData.getSigunguName());
                for (int index = 0; index < response.body().getResponse().getBody().getItems().getItem().size(); index++) {
                    if (response.body().getResponse().getBody().getItems().getItem().get(index).getName().equals(areaData.getSigunguName())){
                        Log.d("selectSi", response.body().getResponse().getBody().getItems().getItem().get(index).getName());
                        //시군구 코드를 setsiCode
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

/*    private void fragment_create(String[] city) {
        //어댑터를 생성한다. 섹션마다 프래그먼트를 생성하여 리턴해준다.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getFragmentManager());

        //액션바를 설정한다.
        final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        //액션바 코너에 있는 Home버튼을 비활성화 한다.
        actionBar.setHomeButtonEnabled(true);

        //탭을 액션바에 보여줄 것이라고 지정한다.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //ViewPager를 설정하고

        //ViewPager에 어댑터를 연결한다.
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        //사용자가 섹션사이를 스와이프할때 발생하는 이벤트에 대한 리스너를 설정한다.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override //스와이프로 페이지 이동시 호출됨
            public void onPageSelected(int position) {
                //swipe - 손가락을 화면에 댄 후, 일직선으로 드래그했다가 손을 떼는 동작이다.
                //화면을 좌우로 스와이핑하여 섹션 사이를 이동할 때, 현재 선택된 탭의 위치이다.

                //액션바의 탭위치를 페이지 위치에 맞춘다.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        //각각의 섹션을 위한 탭을 액션바에 추가한다.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(areaData.citys); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            //어댑터에서 정의한 페이지 제목을 탭에 보이는 문자열로 사용한다.
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            //TabListener 인터페이스를 구현할 액티비티 오브젝트도 지정한다.
                            .setTabListener(this));

        }

    }
    @Override  //액션바의 탭 선택시 호출됨
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        //액션바에서 선택된 탭에 대응되는 페이지를 뷰페이지에서 현재 보여지는 페이지로 변경한다.
        mViewPager.setCurrentItem(tab.getPosition());
        setLatAndLonOfCity(tab.getText().toString());
        weatherRetrofit(areaData.getLat(), areaData.getLon());
        //town버튼 눌렀을 때 위도 경도 설정

        townBtn.setText("전체");
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    //세션에 대응되는 프래그먼트를 리턴한다
    //FragmentPagerAdapter는 메모리에 프래그먼트를 로드한 상태로 유지하지만(3개 프래그먼트 유지하는게 적당함)
    //FragmentStatePagerAdapter는 화면에 보이지 않는 프래그먼트는 메모리에서 제거한다.
    public class AppSectionsPagerAdapter extends FragmentPagerAdapter {
        FragmentManager fm;

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int pos) {
            //태그로 프래그먼트를 찾는다.
            Fragment fragment = fm.findFragmentByTag("android:switcher:" + mViewPager.getId() + ":" + getItemId(pos));

            //프래그먼트가 이미 생성되어 있는 경우
            if (fragment != null) {
                return fragment;
            }

            //프래그먼트의 인스턴스를 생성한다.
            switch(pos) {
                case 0: return FragmentFirst.newInstance("FirstFragment, Instance 1");
                case 1: return FragmentSecond.newInstance("SecondFragment, Instance 1");
                case 2: return FragmentThird.newInstance("ThirdFragment, Instance 1");
                case 3: return FragmentThird.newInstance("ThirdFragment, Instance 2");
                case 4: return FragmentThird.newInstance("ThirdFragment, Instance 3");
                default: return FragmentThird.newInstance("ThirdFragment, Default");
            }
        }

        //프래그먼트 생성 개수
        @Override
        public int getCount() {return 5;}
        public int getCount(String[] city) {return city.length;}

        //탭의 제목으로 사용되는 문자열 생성
        @Override
        public CharSequence getPageTitle(int position) {
            return areaData.citys[position];
        }
    }*/
}
