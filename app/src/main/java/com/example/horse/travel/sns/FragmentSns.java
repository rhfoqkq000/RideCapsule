package com.example.horse.travel.sns;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.horse.travel.R;

import butterknife.ButterKnife;

/**
 * Created by horse on 2017. 10. 9..
 */

public class FragmentSns extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_sns, container, false);
        ButterKnife.bind(this, rootview);
        return rootview;
    }
}
