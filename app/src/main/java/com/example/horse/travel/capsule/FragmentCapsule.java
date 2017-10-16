package com.example.horse.travel.capsule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.horse.travel.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by horse on 2017. 10. 9..
 */

public class FragmentCapsule extends Fragment {

    @OnClick(R.id.capsule_button)
    void capsuleClick(){
        Intent intent = new Intent(getActivity(), ActivityCapsuleImg.class);
        startActivity(intent);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_capsule, container, false);
        ButterKnife.bind(this, rootview);
        return rootview;
    }
}
