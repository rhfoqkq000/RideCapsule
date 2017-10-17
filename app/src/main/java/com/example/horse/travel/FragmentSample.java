package com.example.horse.travel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.horse.travel.mypage.FragmentMypage;

public class FragmentSample extends Fragment {

    public static FragmentSample newInstance(int arg) {
        FragmentSample fragment = new FragmentSample();

        // Set the arguments.
        Bundle args = new Bundle();

        args.putInt("ARG", arg);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_sample, container, false);
        return rootview;
    }
}