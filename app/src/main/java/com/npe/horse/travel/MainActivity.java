package com.npe.horse.travel;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.npe.horse.travel.capsule.FragmentCapsule;
import com.npe.horse.travel.hotchu.FragmentHot;
import com.npe.horse.travel.kakao.KakaoSingleton;
import com.npe.horse.travel.mypage.FragmentMypage;
import com.npe.horse.travel.sns.FragmentSns;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.npe.horse.travel.tourist.FragmentTourist;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import net.danlew.android.joda.JodaTimeAndroid;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    Fragment active;
    private MainActivity.SessionCallback callback;

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    Fragment fragmentTourist = FragmentTourist.newInstance(1);
    //Fragment fragmentSamplee = FragmentSample.newInstance(2);
    Fragment fragmentSns = FragmentSns.newInstance(2);
    Fragment fragmentCapsule = FragmentCapsule.newInstance(3);
    Fragment fragmentInn = FragmentHot.newInstance(4);
    Fragment fragmentMypage = FragmentMypage.newInstance(5);

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
        Log.d("MEM_Main","Trim!");

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
        Log.d("MEM_Main","LOW!");
    }

    //    butterknife 각 layout에서 id연결
    @BindView(R.id.bottombar)
    BottomBar bottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        JodaTimeAndroid.init(this);

        getKeyHash(getApplicationContext());

        fragmentManager = getSupportFragmentManager();
//        createFragments();
//        setUIListeners();

        //카카오로그인 세션 체크
        callback = new MainActivity.SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        if (Session.getCurrentSession().isOpened()){
            Log.e("LoginActivity isOpened", "OPEN!");
            requestMe();
        }else{
            Log.e("LoginActivity isOpened", "NOT OPENDED");
        }
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
                    if (active != fragmentTourist)
                        hideShowFragment(active,fragmentTourist);
//                    else
//                        fm.beginTransaction().show(fragmentSample).commit();
                    active = fragmentTourist;
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
        addHideFragment(fragmentTourist);
        addHideFragment(fragmentSns);
        addHideFragment(fragmentCapsule);
        addHideFragment(fragmentInn);
        addHideFragment(fragmentMypage);
        fragmentManager.beginTransaction().show(fragmentTourist).commit();
        active = fragmentTourist;
    }
    private void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Log.e("MainActivity", message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e("MainActivity", "onSessionClosed");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.e("MainActivity", "UserProfile : " + userProfile);
                KakaoSingleton.getInstance().setEmail(userProfile.getEmail());
                KakaoSingleton.getInstance().setNickname(userProfile.getNickname());
                KakaoSingleton.getInstance().setSmallImage(userProfile.getThumbnailImagePath());
                createFragments();
                setUIListeners();
            }

            @Override
            public void onNotSignedUp() {
                //세션 오픈은 성공했으나 사용자 정보 요청 결과 사용자 가입이 안된 상태로 자동 가입 앱이 아닌 경우에만 호출된다
                Log.e("MainActivity", "onNotSignedUp");
            }
        });
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
        }
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {    //뒤로가기 버튼 두 번 누르면 종료
            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)  //연속 누를 때 2초 안에 안누르면 종료 x
            {
                super.onBackPressed();
            } else    //종료
            {
                backPressedTime = tempTime;
                Toast.makeText(this, "'뒤로' 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();

        }
    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (android.content.pm.Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("SIGNATURE", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;

    }
}