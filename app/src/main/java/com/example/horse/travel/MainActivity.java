package com.example.horse.travel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.horse.travel.capsule.FragmentCapsule;
import com.example.horse.travel.inn.FragmentInn;
import com.example.horse.travel.mypage.FragmentMypage;
import com.example.horse.travel.sns.FragmentSns;
import com.example.horse.travel.tourist.FragmentTourist;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import net.danlew.android.joda.JodaTimeAndroid;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

//    butterknife 각 layout에서 id연결
    @BindView(R.id.bottombar)
    BottomBar bottomBar;

//    FragmentSns fragmentSns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        JodaTimeAndroid.init(this);


        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            Fragment fragment = null;
            @Override
//            bottomNavigation item추가
            public void onTabSelected(int tabId) {

//                관광지 정보
                if (tabId == R.id.bottom_tourist){
                   fragment = new FragmentTourist();
                }

//                sns
                else if (tabId == R.id.bottom_sns){
                    fragment = new FragmentSns();
                }

//                타임캡슐
                else if (tabId == R.id.bottom_capsule){
                    fragment = new FragmentCapsule();
                }

//                숙박
                else if (tabId == R.id.bottom_house){
                    fragment = new FragmentInn();
                }

//                마이페이지
                else if (tabId == R.id.bottom_mypage){
                    fragment = new FragmentMypage();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, fragment)
                        .commit();
            }
        });
    }


}
