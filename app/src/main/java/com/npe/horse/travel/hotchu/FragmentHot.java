package com.npe.horse.travel.hotchu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.npe.horse.travel.ApiClient;
import com.npe.horse.travel.R;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


        InterfaceHotchuList interfaceHotchuList = ApiClient.getClient().create(InterfaceHotchuList.class);
        Call<HotchuDTO> call = interfaceHotchuList.getList();
        call.enqueue(new Callback<HotchuDTO>() {
            @Override
            public void onResponse(Call<HotchuDTO> call, Response<HotchuDTO> response) {
                if (response.body().getResult_code()==200){
                    horizontalInfiniteCycleViewPager.setAdapter(new HorizontalPagerAdapter(getContext(), false, response.body().getResult_body()));

                }
            }

            @Override
            public void onFailure(Call<HotchuDTO> call, Throwable t) {
                Log.e("FragmentHot",t.getMessage());
                Toast.makeText(getContext(),"글을 불러오지 못했습니다.",Toast.LENGTH_SHORT).show();
            }
        });



        return rootview;
    }
}
