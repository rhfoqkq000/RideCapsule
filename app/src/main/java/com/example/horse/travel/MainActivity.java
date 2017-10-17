package com.example.horse.travel;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.horse.travel.capsule.FragmentCapsule;
import com.example.horse.travel.inn.FragmentInn;
import com.example.horse.travel.mypage.FragmentMypage;
import com.example.horse.travel.sns.FragmentSns;
import com.example.horse.travel.tourist.FragmentTourist;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    Fragment active;
    Fragment fragmentSample = FragmentSample.newInstance(1);
    Fragment fragmentSns = FragmentSns.newInstance(2);
    Fragment fragmentCapsule = FragmentCapsule.newInstance(3);
    Fragment fragmentInn = FragmentInn.newInstance(4);
    Fragment fragmentMypage = FragmentMypage.newInstance(5);

//    butterknife 각 layout에서 id연결
    @BindView(R.id.bottombar)
    BottomBar bottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();
//
        createFragments();
        setUIListeners();


        // 절대 지우지 말것!! 혹시나...
//        final FragmentTourist fragmentTourist = new FragmentTourist();

//        active = fragmentSample;
//
//        final FragmentManager fm = getSupportFragmentManager();
//
//        fm.beginTransaction().add(R.id.content, fragmentSample, "1").commit();
//        fm.beginTransaction().add(R.id.content, fragmentSns, "2").commit();
//        fm.beginTransaction().add(R.id.content, fragmentCapsule, "3").commit();
//        fm.beginTransaction().add(R.id.content, fragmentInn, "4").commit();
//        fm.beginTransaction().add(R.id.content, fragmentMypage, "5").commit();
//
//
////        fm.beginTransaction().hide(fragmentSample).commit();
//        fm.beginTransaction().hide(fragmentSns).commit();
//        fm.beginTransaction().hide(fragmentCapsule).commit();
//        fm.beginTransaction().hide(fragmentInn).commit();
//        fm.beginTransaction().hide(fragmentMypage).commit();
//
//        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelected(int tabId) {
//                if (tabId == R.id.bottom_tourist) {
//                    if (active != fragmentSample)
//                        fm.beginTransaction().hide(active).show(fragmentSample).commit();
//                    else
//                        fm.beginTransaction().show(fragmentSample).commit();
//                    active = fragmentSample;
//                }
////                sns
//                else if (tabId == R.id.bottom_sns) {
//                    fm.beginTransaction().hide(active).show(fragmentSns).commit();
//                    active = fragmentSns;
//                }
//
////                타임캡슐
//                else if (tabId == R.id.bottom_capsule) {
//                    fm.beginTransaction().hide(active).show(fragmentCapsule).commit();
//                    active = fragmentCapsule;
//                }
//
////                숙박
//                else if (tabId == R.id.bottom_house) {
//                    fm.beginTransaction().hide(active).show(fragmentInn).commit();
//                    active = fragmentInn;
//                }
//
////                마이페이지
//                else if (tabId == R.id.bottom_mypage) {
//                    fm.beginTransaction().hide(active).show(fragmentMypage).commit();
//                    active = fragmentMypage;
//                }
//            }
//        });
    }

    private void setUIListeners() {
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.bottom_tourist:
                    if (active != fragmentSample)
                        hideShowFragment(active,fragmentSample);
//                    else
//                        fm.beginTransaction().show(fragmentSample).commit();
                    active = fragmentSample;
                        break;
                    case R.id.bottom_sns:
                        hideShowFragment(active, fragmentSns);
                        active = fragmentSns;
                        break;
                    case R.id.bottom_capsule:
                        hideShowFragment(active, fragmentCapsule);
                        active = fragmentCapsule;
                        break;
                    case R.id.bottom_house:
                        hideShowFragment(active, fragmentInn);
                        active = fragmentInn;
                        break;
                    case R.id.bottom_mypage:
                        hideShowFragment(active, fragmentMypage);
                        active = fragmentMypage;
                        break;
                }
            }
        });
    }
//
    //Method to add and hide all of the fragments you need to. In my case I hide 4 fragments, while 1 is visible, that is the first one.
    private void addHideFragment(Fragment fragment) {
        if(!fragment.isAdded()){
            fragmentManager.beginTransaction().add(R.id.content, fragment).hide(fragment).commit();
        }
    }
//
    //Method to hide and show the fragment you need. It is called in the BottomBar click listener.
    private void hideShowFragment(Fragment hide, Fragment show) {
        fragmentManager.beginTransaction().hide(hide).show(show).commit();
    }
//
    //Add all the fragments that need to be added and hidden. Also, add the one that is supposed to be the starting one, that one is not hidden.
    private void createFragments() {
        addHideFragment(fragmentSample);
        addHideFragment(fragmentSns);
        addHideFragment(fragmentCapsule);
        addHideFragment(fragmentInn);
        addHideFragment(fragmentMypage);
        fragmentManager.beginTransaction().show(fragmentSample).commit();
        active = fragmentSample;
    }
}

