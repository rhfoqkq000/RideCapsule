package com.npe.horse.travel.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.npe.horse.travel.R;
import com.npe.horse.travel.kakao.KakaoSingleton;
import com.npe.horse.travel.sns.hashtag.HashTagSingleton;
import com.npe.horse.travel.sns.hashtag.SnsHashTagActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by horse on 2017. 10. 9..
 */

public class FragmentMypage extends Fragment {

    @BindView(R.id.mypage_profileImg)
    CircleImageView mypage_profileImg;

    @BindView(R.id.mypage_nicknameTv)
    TextView mypage_nicknameTv;

    @BindView(R.id.mypage_emailTv)
    TextView mypage_emailTv;

    public static FragmentMypage newInstance(int arg) {
        FragmentMypage fragment = new FragmentMypage();

        // Set the arguments.
        Bundle args = new Bundle();

        args.putInt("ARG", arg);
        fragment.setArguments(args);
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_mypage, container, false);
        ButterKnife.bind(this, rootview);

        Log.e("FragmentMyPage", KakaoSingleton.getInstance().getSmallImage()+", "+KakaoSingleton.getInstance().getNickname()+", "+KakaoSingleton.getInstance().getEmail());

        Glide.with(getContext()).load(KakaoSingleton.getInstance().getSmallImage()).into(mypage_profileImg);
        mypage_emailTv.setText(KakaoSingleton.getInstance().getEmail());
        mypage_nicknameTv.setText(KakaoSingleton.getInstance().getNickname());

        return rootview;
    }

    @OnClick(R.id.mypage_writtenBtn)
    void writtenBtnClicked(){
        Intent intent = new Intent(getContext(), SnsHashTagActivity.class);
        HashTagSingleton.getInstance().setHash("@"+KakaoSingleton.getInstance().getNickname());
        startActivity(intent);
    }

    @OnClick(R.id.mypage_likeBtn)
    void likeBtnClicked(){
        Intent intent = new Intent(getContext(), SnsHashTagActivity.class);
        HashTagSingleton.getInstance().setHash("*");
        startActivity(intent);
    }
}