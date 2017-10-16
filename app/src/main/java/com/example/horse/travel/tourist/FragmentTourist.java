package com.example.horse.travel.tourist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.horse.travel.R;

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

public class FragmentTourist extends Fragment implements ActionBar.TabListener{

    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.town)
    Button btn1;
    @BindView(R.id.pager)
    ViewPager mViewPager;
    @OnClick(R.id.town)
    void clickTown(){
        DialogSelectOption(region);
    }

    public FragmentTourist() {
//        Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_tourist, container, false);
        ButterKnife.bind(this, rootview);

        //날씨 불러옴
        setWeather(lat, lon);

        //프레그먼트 생성
        fragment_create(items);

        return rootview;
    }


    //더한 코드

    final static String TAG = "WeatherThread";
    Context mContext;
    WeatherRepo weatherRepo;
    Handler handler;

    int version = 1;
    String lat = "37.540705";
    String lon = "126.956764";

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    //도시
    final String items[] = { "서울특별시", "경기도", "강원도", "부산광역시", "인천광역시", "대구광역시", "대전광역시", "광주광역시","울산광역시", "세종특별자치시",
            "충청북도","충청남도","경상북도", "경상남도", "전라북도", "전라남도", "제주도"};
    //도시 별 지역
    String[] seoUl = {"강남구","강동구","강북구","강서구","관악구","광진구","구로구","금천구","노원구","도봉구","동대문구","동작구","마포구","서대문구","서초구","성동구","성북구","송파구","양천구","영등포구","용산구","은평구","종로","중구","중랑구"};
    String[] gyeongGi = {"가평군","고양시","과천시", "광명시","광주시","구리시","군포시","김포시","남양주시","동두천시","부천시","성남시","수원시","시흥시","안산시","안성시","안양시","양주시","양평군","여주시","연천군","오산시","용인시","의왕시","의정부시","이천시","파주시","평택시","포천시","하남시","화성시"};
    String[] gangWon = {"강릉시","고성군","동해시","삼척시","속초시","양구군","양양군","영월군","원주시","인제군","정선군","철원군","춘천시","태백시","평창군","홍천군","화천군","횡성군"};
    String[] buSan = {"강서구","금정구","기장군","남구","동구","동래구","부산진구","북구","사상구","사하구","서구","수영구","연제구","영도구","중구","해운대구"};
    String[] inChen = {"강화군","계양구","남구","남동구","동구","부평구","서구","연수구","옹진군","중구"};
    String[] daeGu = {"남구","달서구","달성군","동구","북구","서구","수성구","중구"};
    String[] daeJun = {"대덕구","동구","서구","유성구","중구"};
    String[] gwangJu = {"광산구","남구","동구","북구","서구"};
    String[] ulSan = {"중구","남구","동구","북구","울주군"};
    String[] seJong = {"세종특별자치시"};
    String[] chungBuk = {"괴산군","단양군","보은군","영동군","옥천군","음성군","제천시","진천군","청원군","청주시","충주시","증평군"};
    String[] chungNam = {"공주시","금산군","논산시","당진시","보령시","부여군","서산시","서천군","아산시","예산군","천안시","청양군","태안군","홍성군","계룡시"};
    String[] gyeongBuk = {"경산시","경주시","고령군","구미시","군위군","김천시","문경시","봉화군","상주시","성주군","안동시","영덕군","양양군","영주시","영천시","예천군","울릉군","울진군","의성군","청도군","청송군","칠곡군","포항시"};
    String[] gyeongNam = {"거제시","고성군","김해시","남해군","마산시","밀양시","사천시","산청군","양산시","의령군","진주시","진해시","창녕군","창원시","통영시","하동군","함안군","함양군","합천군"};
    String[] junBuk = {"고창군","군산시","김제시","남원시","무주군","부안군","순창군","완주군","익산시","임실군","장수군","전주시","정읍시","진안군"};
    String[] junNam = {"강진군","고흥군","곡성군","광양시","구례군","나주시","담양군","목포시","무안군","보성군","순천시","신안군","여수시","영광군","영암군","완도군","장성군","장흥군","진도군"};
    String[] jeJu = {"남제주군","북제주군","서귀포시","제주시"};

    String[] region = seoUl; //초기값 서울

    @Override
    public void onResume() {
        super.onResume();
        setWeather(lat, lon);
    }


    private void DialogSelectOption(final String[] region) {

        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setTitle("지역을 선택해주세요");
        ab.setSingleChoiceItems(region, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 각 리스트를 선택했을때
                        setLatAndLonOfCity(region[whichButton]);
                        btn1.setText(region[whichButton]);
                    }
                }).setPositiveButton("선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 선택 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                        setWeather(lat, lon);

                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 취소 버튼 클릭시
                        dialog.cancel();
                    }
                });
        ab.show();
    }

    private void setWeather(String lat, String lon) {
        Retrofit client = new Retrofit.Builder().baseUrl("http://apis.skplanetx.com/").addConverterFactory(GsonConverterFactory.create()).build();
        WeatherRepo.WeatherApiInterface service = client.create(WeatherRepo.WeatherApiInterface.class);
        Call<WeatherRepo> call = service.get_Weather_retrofit(1, lat, lon);
        call.enqueue(new Callback<WeatherRepo>() {
            @Override
            public void onResponse(Call<WeatherRepo> call, Response<WeatherRepo> response) {
                //현재온도
                Log.i("MainActivity", response.body().getWeather().getHourly().get(0).getTemperature().getTc());
                tv1.setText(response.body().getWeather().getHourly().get(0).getTemperature().getTc());
                //현재 하늘 상태
                Log.i("MainActivity", response.body().getWeather().getHourly().get(0).getSky().getName());
                tv2.setText(response.body().getWeather().getHourly().get(0).getSky().getName());
            }

            @Override
            public void onFailure(Call<WeatherRepo> call, Throwable t) {
                Log.e(TAG, "날씨정보 불러오기 실패 :" + t.getMessage());
                Log.e(TAG, "요청 메시지 :" + call.request());
            }
        });
    }

    private void setLatAndLonOfCity(String selectCity) {
        if (selectCity.equals("서울특별시")) setLatAndLonAndRegion("37.540705","126.956764", seoUl);
        else if (selectCity.equals("경기도")) setLatAndLonAndRegion("37.567167","127.190292", gyeongGi);
        else if (selectCity.equals("강원도")) setLatAndLonAndRegion("37.555837", "128.209315", gangWon);
        else if (selectCity.equals("부산광역시")) setLatAndLonAndRegion("35.198362", "129.053922",buSan);
        else if (selectCity.equals("인천광역시")) setLatAndLonAndRegion("37.469221", "126.573234",inChen);
        else if (selectCity.equals("대구광역시")) setLatAndLonAndRegion("35.798838", "128.583052",daeGu);
        else if (selectCity.equals("대전광역시")) setLatAndLonAndRegion("36.321655", "127.378953",daeJun);
        else if (selectCity.equals("광주광역시")) setLatAndLonAndRegion("35.126033", "126.831302",gwangJu);
        else if (selectCity.equals("울산광역시")) setLatAndLonAndRegion("35.519301", "129.239078", ulSan);
        else if (selectCity.equals("세종특별자치시")) setLatAndLonAndRegion("36.483066", "127.289808",seJong);
        else if (selectCity.equals("충청북도")) setLatAndLonAndRegion("36.628503", "127.929344",chungBuk);
        else if (selectCity.equals("충청남도")) setLatAndLonAndRegion("36.557229", "126.779757",chungNam);
        else if (selectCity.equals("경상북도")) setLatAndLonAndRegion("36.248647", "128.664734",gyeongBuk);
        else if (selectCity.equals("경상남도")) setLatAndLonAndRegion("35.259787", "128.664734",gyeongNam);
        else if (selectCity.equals("전라북도")) setLatAndLonAndRegion("35.716705", "127.144185",junBuk);
        else if (selectCity.equals("전라남도")) setLatAndLonAndRegion("34.819400", "126.893113",junNam);
        else if (selectCity.equals("제주도")) setLatAndLonAndRegion("33.364805", "126.542671",jeJu);
    }
    private void setLatAndLonAndRegion(String lat, String lon, String[] region) {
        this.lat = lat;
        this.lon = lon;
        this.region = region;
    }

    private void fragment_create(String[] city) {
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
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(items); i++) {
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
        setWeather(lat, lon);
        //town버튼 눌렀을 때 위도 경도 설정

        btn1.setText("전체");
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
            return items[position];
        }
    }
}
