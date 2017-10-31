package com.example.horse.travel.hotchu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.horse.travel.R;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import butterknife.ButterKnife;

/**
 * Created by horse on 2017. 10. 9..
 */

public class FragmentHot extends Fragment {
  
    public static FragmentHot newInstance(int arg) {
        FragmentHot fragment = new FragmentHot();

        // Set the arguments.
        Bundle args = new Bundle();

        args.putInt("ARG", arg);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_hot, container, false);
        ButterKnife.bind(this, rootview);

        final HorizontalInfiniteCycleViewPager horizontalInfiniteCycleViewPager = rootview.findViewById(R.id.hicvp);
        horizontalInfiniteCycleViewPager.setAdapter(new HorizontalPagerAdapter(getContext(), false));

        return rootview;
    }
}
