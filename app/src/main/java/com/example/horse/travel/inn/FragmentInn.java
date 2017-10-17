package com.example.horse.travel.inn;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.horse.travel.R;
import com.example.horse.travel.capsule.FragmentCapsule;

import butterknife.ButterKnife;

/**
 * Created by horse on 2017. 10. 9..
 */

public class FragmentInn extends Fragment {

    public static FragmentInn newInstance(int arg) {
        FragmentInn fragment = new FragmentInn();

        // Set the arguments.
        Bundle args = new Bundle();

        args.putInt("ARG", arg);
        fragment.setArguments(args);
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_inn, container, false);
        ButterKnife.bind(this, rootview);
        return rootview;
    }
}
