package com.npe.horse.travel.sns;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.npe.horse.travel.ApiClient;
import com.npe.horse.travel.SearchViewCustom;
import com.npe.horse.travel.EndlessRecyclerViewScrollListener;
import com.npe.horse.travel.R;
import com.npe.horse.travel.sns.hashtag.HashTagSingleton;
import com.npe.horse.travel.sns.hashtag.InterfaceSnsHashtag;
import com.npe.horse.travel.sns.hashtag.SnsHashTagActivity;
import com.npe.horse.travel.sns.hashtag.SnsHashtagDTO;
import com.npe.horse.travel.sns.list.InterfaceSnsList;
import com.npe.horse.travel.sns.list.SnsListDTO;
import com.npe.horse.travel.sns.list.SnsListItem;
import com.npe.horse.travel.sns.list.SnsRecyclerAdapter;
import com.npe.horse.travel.sns.write.ActivityImageSelect;


import java.util.ArrayList;
import java.util.Arrays;
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

    @BindView(R.id.search_view)
    SearchViewCustom searchView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    List<SnsListItem> allItems;

    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    int init_page= 1;

    boolean isRe = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        BusProvider.getInstance().register(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootview = inflater.inflate(R.layout.fragment_sns, container, false);

        ButterKnife.bind(this, rootview);

        InterfaceSnsHashtag hashtag = ApiClient.getClient().create(InterfaceSnsHashtag.class);
        Call<SnsHashtagDTO> call = hashtag.getHashtag();
        call.enqueue(new Callback<SnsHashtagDTO>(){
            @Override
            public void onResponse(Call<SnsHashtagDTO> call, Response<SnsHashtagDTO> response) {
                if(response.body().getResult_code()==200){
                    String[] suggestions = new String[response.body().getResult_body().size()];
                    for(int i = 0; i < response.body().getResult_body().size(); i++){
                        Log.e("FragmentSns", "ALL::"+response.body().getResult_body().toString());
                        suggestions[i] = response.body().getResult_body().get(i).getResult();
                    }
                    Log.e("FragmentSns", Arrays.toString(suggestions));
                    searchView.setSuggestions(suggestions);
                    searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String query = (String) adapterView.getItemAtPosition(i);
                            Log.d("RE",query);
                            Intent intent = new Intent(getContext(), SnsHashTagActivity.class);
                            HashTagSingleton.getInstance().setHash(query);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            getContext().startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<SnsHashtagDTO> call, Throwable t) {
                t.printStackTrace();
            }
        });

        progressBar.setVisibility(View.INVISIBLE);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

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

        adapter = new SnsRecyclerAdapter(Glide.with(this), getContext().getContentResolver());
        adapter.setHasStableIds(true);
        adapter.setContext(getContext());
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
                progressBar.setVisibility(View.VISIBLE);
                getSnsList(page + 1);
                progressBar.setVisibility(View.INVISIBLE);
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
                Log.e("RETROFIT", t.getMessage());
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
//            refresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        BusProvider.getInstance().unregister(this);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
    }

}
