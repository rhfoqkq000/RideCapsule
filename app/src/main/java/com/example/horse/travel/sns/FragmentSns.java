package com.example.horse.travel.sns;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.horse.travel.ApiClient;
import com.example.horse.travel.EndlessRecyclerViewScrollListener;
import com.example.horse.travel.R;
import com.example.horse.travel.sns.list.InterfaceSnsList;
import com.example.horse.travel.sns.list.SnsListDTO;
import com.example.horse.travel.sns.list.SnsListItem;
import com.example.horse.travel.sns.list.SnsRecyclerAdapter;
import com.example.horse.travel.sns.write.ActivityImageSelect;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by horse on 2017. 10. 9..
 */

public class FragmentSns extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public static FragmentSns newInstance(int arg) {
        FragmentSns fragment = new FragmentSns();

        // Set the arguments.
        Bundle args = new Bundle();

        args.putInt("ARG", arg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(getContext()).clearMemory();
        Log.d("MEM_FragmentSns","LOW!");
    }

    @BindView(R.id.writeBtn)
    FloatingActionButton writeBtn;

    @BindView(R.id.snsRecyclerView)
    RecyclerView snsRe;

    SnsRecyclerAdapter adapter;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    List<SnsListItem> allItems;

    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    int init_page= 1;

    boolean isRe = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_sns, container, false);

        ButterKnife.bind(this, rootview);

        swipeRefreshLayout.setOnRefreshListener(this);
        allItems = new ArrayList<>();

        // recyclerview init
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setInitialPrefetchItemCount(10);
        layoutManager.setItemPrefetchEnabled(true);
        snsRe.setLayoutManager(layoutManager);
        snsRe.setFocusable(false);
//        snsRe.setNestedScrollingEnabled(false);
        snsRe.getRecycledViewPool().setMaxRecycledViews(0,10);

        RequestOptions options = new RequestOptions().placeholder(R.drawable.image_loding).error(R.mipmap.ic_launcher);
        adapter = new SnsRecyclerAdapter(Glide.with(this));
        adapter.setHasStableIds(true);
        snsRe.setAdapter(adapter);

        getSnsList(init_page);

        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ActivityImageSelect.class);
                startActivity(intent);
            }
        });

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                Log.d("SCROLL","END! | "+page);
                getSnsList(page+1);
            }
        };

        // endless scrolling
        snsRe.addOnScrollListener(endlessRecyclerViewScrollListener);
        return rootview;
    }
    void getSnsList(int page){
        InterfaceSnsList list = ApiClient.getClient().create(InterfaceSnsList.class);
        Call<SnsListDTO> call = list.listSns(page);
        call.enqueue(new Callback<SnsListDTO>() {
            @Override
            public void onResponse(@NonNull Call<SnsListDTO> call, @NonNull Response<SnsListDTO> response) {
                Log.d("URL",response.raw().request().url().toString());
                if (response.body().getItems_count()!=0){
                    allItems.addAll(response.body().getResult_body());
                    int curSize = adapter.getItemCount();
                    adapter.addNew(allItems);
                    snsRe.getRecycledViewPool().setMaxRecycledViews(0,allItems.size());
                    if (isRe){
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.notifyItemRangeChanged(curSize,allItems.size()-1);
                    }
                } else {
                    Log.d("SCROLL","END!");
                }
                if (isRe){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
            @Override
            public void onFailure(@NonNull Call<SnsListDTO> call, @NonNull Throwable t) {
                //Log.d("RETROFIT",t.getMessage());
                Toast.makeText(getContext(),"글을 불러오는데 실패했습니다. 잠시후 다시 시도해 주세요.",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        refresh();
    }
    @Override
    public void onResume() {
        super.onResume();
            Log.d("ONRESUME","재실행");
            refresh();
//        if (!EventBus.getDefault().isRegistered(this)) { EventBus.getDefault().register(this); }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) { EventBus.getDefault().unregister(this); }

    }

    private void refresh(){
        if (adapter != null && !allItems.isEmpty()){
        endlessRecyclerViewScrollListener.resetState();
        adapter.removeAll();
        allItems.clear();
        isRe = true;
            snsRe.getRecycledViewPool().setMaxRecycledViews(0,10);
        getSnsList(1);
        }
    }
}
