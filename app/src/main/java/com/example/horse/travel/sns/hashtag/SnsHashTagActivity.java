package com.example.horse.travel.sns.hashtag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.horse.travel.ApiClient;
import com.example.horse.travel.SearchViewCustom;
import com.example.horse.travel.EndlessRecyclerViewScrollListener;
import com.example.horse.travel.R;
import com.example.horse.travel.kakao.KakaoSingleton;
import com.example.horse.travel.sns.list.InterfaceSnsList;
import com.example.horse.travel.sns.list.SnsListDTO;
import com.example.horse.travel.sns.list.SnsListItem;
import com.example.horse.travel.sns.list.SnsRecyclerAdapter;
import com.example.horse.travel.sns.search.InterfaceSnsSearch;
import com.example.horse.travel.sns.search.SnsKeyWordDTO;
import com.example.horse.travel.sns.write.ActivityImageSelect;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by JRokH on 2017-10-30.
 */

public class SnsHashTagActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SearchViewCustom.OnQueryTextListener, SearchViewCustom.SearchViewListener{
    @BindView(R.id.writeBtn)
    FloatingActionButton writeBtn;

    @BindView(R.id.snsRecyclerView)
    RecyclerView snsRe;

    SnsRecyclerAdapter adapter;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.search_view)
    SearchViewCustom searchView;

    List<SnsListItem> allItems;

    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;

    int init_page= 1;

    boolean isRe = false;

    String hashtag;

    final int HASHTAG = 0;

    final int PROFILE = 1;

    final int LOCATION = 2;

    int category = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag_result);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hashtag = HashTagSingleton.getInstance().getHash();

//        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));

        searchView.setOnQueryTextListener(this);
        searchView.setOnSearchViewListener(this);

        swipeRefreshLayout.setOnRefreshListener(this);
        allItems = new ArrayList<>();

        // recyclerview init
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setInitialPrefetchItemCount(10);
        layoutManager.setItemPrefetchEnabled(true);
        snsRe.setLayoutManager(layoutManager);
        snsRe.setFocusable(false);
//        snsRe.setNestedScrollingEnabled(false);
        snsRe.getRecycledViewPool().setMaxRecycledViews(0,10);

        Log.e("SnsHashTagActivity", "hashtag::::"+hashtag.substring(1, hashtag.length()));

        adapter = new SnsRecyclerAdapter(Glide.with(this), getContentResolver());
        adapter.setHasStableIds(true);
        adapter.setContext(getApplicationContext());
        snsRe.setAdapter(adapter);

        if (hashtag.charAt(0) == '#'){
            category = HASHTAG;
            hashtag = hashtag.substring(1, hashtag.length());
            getSnsList(hashtag, category, init_page);
        } else if (hashtag.charAt(0) == '@'){
            category = PROFILE;
            hashtag = hashtag.substring(1, hashtag.length());

            Log.d("SUBSTRING",hashtag.substring(1));
            getSnsList(hashtag, category, init_page);
        } else {
            category = LOCATION;
            hashtag = hashtag.substring(1, hashtag.length());

            getSnsList(hashtag, category, init_page);
        }

        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(),ActivityImageSelect.class);
                startActivity(intent);
            }
        });

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                Log.d("SCROLL","END! | "+page);
                getSnsList(hashtag,category,page+1);
            }
        };

        // endless scrolling
        snsRe.addOnScrollListener(endlessRecyclerViewScrollListener);
    }

    void getSnsList(String hashtag, int category, int page){
        InterfaceSnsList list = ApiClient.getClient().create(InterfaceSnsList.class);
        Call<SnsListDTO> call = list.listSnsForHashTag(KakaoSingleton.getInstance().getId(), category, hashtag, page);
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
                t.getStackTrace();
                Log.e("RETROFIT", t.getMessage());
                Toast.makeText(getApplicationContext(),"글을 불러오는데 실패했습니다. 잠시후 다시 시도해 주세요.",Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onRefresh() {
        refresh();
    }
    private void refresh(){
        if (adapter != null && !allItems.isEmpty()){
            endlessRecyclerViewScrollListener.resetState();
            adapter.removeAll();
            allItems.clear();
            isRe = true;
            snsRe.getRecycledViewPool().setMaxRecycledViews(0,10);
            getSnsList(hashtag,category,1);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("onQueryTextSubmit",query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length()>0){
            Log.d("KEYWORD","|"+newText);
            getSnskeyword(newText);
        }
        return false;
    }

    private void getSnskeyword(String newText) {
        InterfaceSnsSearch snsHashSearch = ApiClient.getClient().create(InterfaceSnsSearch.class);
        Call<SnsKeyWordDTO> dtoCall =   snsHashSearch.getKeyword(newText);
        dtoCall.enqueue(new Callback<SnsKeyWordDTO>() {
            @Override
            public void onResponse(Call<SnsKeyWordDTO> call, Response<SnsKeyWordDTO> response) {
                if (response.body().getItems_count()>0){
                    Log.d("KEYWORD",response.body().getResult_body()[0]);
                    searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
                }
            }

            @Override
            public void onFailure(Call<SnsKeyWordDTO> call, Throwable t) {

            }
        });
    }

    @Override
    public void onSearchViewShown() {
        Log.d("onSearchViewShown","shown");

    }

    @Override
    public void onSearchViewClosed() {
        Log.d("onSearchViewClosed","close");

    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK: {
//                Toast.makeText(getApplicationContext(),"뒤로가기",Toast.LENGTH_LONG).show();
////                if (searchView.isSearchOpen()) {
////                    searchView.closeSearch();
////                }
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Log.d("doback","close");
//        Toast.makeText(getApplicationContext(),"뒤로가기",Toast.LENGTH_LONG).show();
//    }
    //    @Override
//    public void onBackPressed() {
//        Log.d("doback","close");
//
//        if (searchView.isSearchOpen()) {
//            searchView.closeSearch();
//        } else {
//            super.onBackPressed();
//        }
//    }
}
