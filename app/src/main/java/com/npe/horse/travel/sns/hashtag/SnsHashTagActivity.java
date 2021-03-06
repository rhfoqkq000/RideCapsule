package com.npe.horse.travel.sns.hashtag;

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
import com.npe.horse.travel.ApiClient;
import com.npe.horse.travel.SearchViewCustom;
import com.npe.horse.travel.EndlessRecyclerViewScrollListener;
import com.npe.horse.travel.R;
import com.npe.horse.travel.kakao.KakaoSingleton;

import com.npe.horse.travel.sns.list.InterfaceSnsList;
import com.npe.horse.travel.sns.list.SnsListDTO;
import com.npe.horse.travel.sns.list.SnsListItem;
import com.npe.horse.travel.sns.list.SnsRecyclerAdapter;
import com.npe.horse.travel.sns.search.InterfaceSnsSearch;
import com.npe.horse.travel.sns.search.SnsKeyWordDTO;
import com.npe.horse.travel.sns.write.ActivityImageSelect;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JRokH on 2017-10-30.
 */

public class SnsHashTagActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SearchViewCustom.OnQueryTextListener, SearchViewCustom.SearchViewListener{

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

    final int LIKE = 3;

    final int MYLOOK = 4;

    int category = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 해시태그, 유저 닉네임, 지역명으로 검색된 결과를 출력하는 액티비티임
        // FragmentSns와 거의 유사함.. 받아온 hashtag 값으로 SNS 목록 받아옴
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

        if (hashtag.substring(0, 2).equals("@@")){
            Toast.makeText(this, "해당 글의 빈칸을 길게 누르시면 글이 삭제됩니다.", Toast.LENGTH_SHORT).show();
            Log.e("SnsHashTagActivity", "hashtag2::::"+hashtag.substring(0, 2));
            Log.e("SnsHashTagActivity", "hashtag3::::"+hashtag.substring(2, hashtag.length()));
            category = MYLOOK;
            hashtag = hashtag.substring(2, hashtag.length());
            getSnsList(hashtag, 1, init_page);
        } else if (hashtag.charAt(0) == '#'){
            category = HASHTAG;
            hashtag = hashtag.substring(1, hashtag.length());
            getSnsList(hashtag, category, init_page);
        } else if (hashtag.charAt(0) == '@'){
            category = PROFILE;
            hashtag = hashtag.substring(1, hashtag.length());

            Log.d("SUBSTRING",hashtag.substring(1));
            getSnsList(hashtag, category, init_page);
        } else if (hashtag.charAt(0) == '~'){
            category = LOCATION;
            hashtag = hashtag.substring(1, hashtag.length());

            getSnsList(hashtag, category, init_page);
        } else {
            category = LIKE;
            getSnsListLike(init_page);
        }
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                Log.d("SCROLL","END! | "+page);
                if (category == LIKE){
                    getSnsListLike(page+1);
                }else{
                    getSnsList(hashtag,category,page+1);
                }
            }
        };

        // endless scrolling
        snsRe.addOnScrollListener(endlessRecyclerViewScrollListener);
    }

    void getSnsList(String hashtag, int category2, int page){
        InterfaceSnsList list = ApiClient.getClient().create(InterfaceSnsList.class);
        Call<SnsListDTO> call = list.listSnsForHashTag(KakaoSingleton.getInstance().getId(), category2, hashtag, page);
        call.enqueue(new Callback<SnsListDTO>() {
            @Override
            public void onResponse(@NonNull Call<SnsListDTO> call, @NonNull Response<SnsListDTO> response) {
                Log.d("URL",response.raw().request().url().toString());
                if (response.body().getItems_count()!=0){
                    allItems.addAll(response.body().getResult_body());
                    Log.e("SnsHashTagActivity", "1");
                    int curSize = adapter.getItemCount();
                    adapter.addNew(allItems);
                    Log.e("SnsHashTagActivity", "2");
                    snsRe.getRecycledViewPool().setMaxRecycledViews(0,allItems.size());
                    Log.e("SnsHashTagActivity", "3");
                    if (isRe){
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.notifyItemRangeChanged(curSize,allItems.size()-1);
                        Log.e("SnsHashTagActivity", "4");
                    }
                    if(category == MYLOOK){
                        adapter.setDeletable(true);
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
                Toast.makeText(getApplicationContext(),"글을 불러오는데 실패했습니다. 잠시후 다시 시도해 주세요.1",Toast.LENGTH_LONG).show();
            }
        });
    }

    void getSnsListLike(int page){
        InterfaceSnsList list = ApiClient.getClient().create(InterfaceSnsList.class);
        Call<SnsListDTO> call = list.listSnsLike(KakaoSingleton.getInstance().getId(), page);
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
                Toast.makeText(getApplicationContext(),"글을 불러오는데 실패했습니다. 잠시후 다시 시도해 주세요.2",Toast.LENGTH_LONG).show();
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
            if (category == LIKE) {
                getSnsListLike(1);
            }else{
                if (category == MYLOOK){
                    getSnsList(hashtag,1,1);
                }else{
                    getSnsList(hashtag,category,1);
                }
            }
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
                t.printStackTrace();
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.setDeletable(false);
    }
}
