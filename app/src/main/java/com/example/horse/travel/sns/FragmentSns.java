package com.example.horse.travel.sns;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.horse.travel.ApiClient;
import com.example.horse.travel.R;
import com.example.horse.travel.sns.list.InterfaceSnsList;
import com.example.horse.travel.sns.list.SnsListDTO;
import com.example.horse.travel.sns.list.SnsListItem;
import com.example.horse.travel.sns.list.SnsRecyclerAdapter;
import com.example.horse.travel.sns.write.ActivityImageSelect;
import com.example.horse.travel.sns.write.ActivitySnsWrite;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by horse on 2017. 10. 9..
 */

public class FragmentSns extends Fragment {

    @BindView(R.id.writeBtn)
    Button writeBtn;

//    SnsListViewAdapter adapter;
    @BindView(R.id.snsRecyclerView)
    RecyclerView snsRe;

    SnsRecyclerAdapter adapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_sns, container, false);

        ButterKnife.bind(this, rootview);
//        adapter = new SnsListViewAdapter();
//        getSnsList();
        initViews();
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ActivityImageSelect.class);
                startActivity(intent);
            }
        });

        return rootview;
    }



    private void initViews() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        snsRe.setLayoutManager(layoutManager);
        adapter = new SnsRecyclerAdapter();
        snsRe.setAdapter(adapter);
        getSnsList();
    }

    void getSnsList(){
        InterfaceSnsList list = ApiClient.getClient().create(InterfaceSnsList.class);
        Call<SnsListDTO> call = list.listSns("1");
        call.enqueue(new Callback<SnsListDTO>() {
            @Override
            public void onResponse(Call<SnsListDTO> call, Response<SnsListDTO> response) {
                Log.d("URL",response.raw().request().url().toString());
                adapter.addNew(response.body().getResult_body());
            }

            @Override
            public void onFailure(Call<SnsListDTO> call, Throwable t) {
                Toast.makeText(getContext(),"글을 불러오는데 실패했습니다. 잠시후 다시 시도해 주세요.",Toast.LENGTH_LONG).show();
            }
        });
    }
}
