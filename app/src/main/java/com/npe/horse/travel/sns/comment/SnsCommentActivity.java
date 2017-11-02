package com.npe.horse.travel.sns.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.npe.horse.travel.ApiClient;
import com.npe.horse.travel.R;
import com.npe.horse.travel.TimeCal;
import com.npe.horse.travel.sns.list.SnsListItem;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by qazz92 on 2017. 10. 29..
 */

public class SnsCommentActivity extends AppCompatActivity{

    @BindView(R.id.sns_article_main)
    TextView articleView;

    @BindView(R.id.sns_author_main)
    TextView authorView;

    @BindView(R.id.sns_comment_updated_at_main)
    TextView updatedAtView;

    @BindView(R.id.sns_comment_re)
    RecyclerView sns_comment_re;

    @BindView(R.id.comment_load_more)
    TextView comment_load_more;

    @BindView(R.id.sns_comment_write_edit)
    EditText sns_comment_write;

    @OnClick(R.id.sns_comment_write_btn)
    void writeComment(){
        // 댓글 쓰기
        postArticle = sns_comment_write.getText().toString();
        postWrite(content_id,user_id,postArticle);
    }



    @OnClick(R.id.comment_load_more)
    void loadMore(){
        getSnsCommentList(content_id,++page);
        Log.d("loadMore","load");
    }

    SnsCommentRecyclerAdapter adapter;

    int page = 0;

    int content_id = 0;

    int user_id = 1;

    String postArticle;

    List<SnsCommentItem> allItems;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns_comment);
        ButterKnife.bind(this);

        SnsCommentSingleton singleton = SnsCommentSingleton.getInstance();
        SnsListItem item = singleton.getItem();
        content_id = item.getId();
        String article = item.getPost();
        Date updated_at = item.getUpdated_at();
        String author = item.getNickname();
        Log.d("article",article);

        // 해시태그 설정
        HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getApplication().getResources().getColor(R.color.blue), new HashTagHelper.OnHashTagClickListener() {
                @Override
                public void onHashTagClicked(String hashTag) {
                    Log.d("TAG",hashTag);
                }
        });

        mTextHashTagHelper.handle(articleView);

        String timeString = TimeCal.formatTimeString(updated_at.getTime());

        articleView.setText(article);
        authorView.setText(author);
        updatedAtView.setText(timeString);


        allItems = new ArrayList<>();
        adapter = new SnsCommentRecyclerAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        sns_comment_re.setLayoutManager(linearLayoutManager);
        sns_comment_re.setAdapter(adapter);
        getSnsCommentList(content_id,++page);

    }

    private void postWrite(int content_id, int user_id, final String postArticle) {
        InterfaceSnsComment interfaceSnsComment = ApiClient.getClient().create(InterfaceSnsComment.class);
        Call<SnsCommentDTO> call = interfaceSnsComment.commentWrite(content_id, user_id, postArticle);
        call.enqueue(new Callback<SnsCommentDTO>() {
            @Override
            public void onResponse(Call<SnsCommentDTO> call, Response<SnsCommentDTO> response) {
                Log.e("comment_write_success", String.valueOf(response.body().getResult_code()));
                allItems.addAll(response.body().getResult_body());
                sns_comment_write.setText("");
                adapter.addNew(allItems);
//                if(allItems.size()==1){
//                    adapter.notifyDataSetChanged();
//                }else{
                    adapter.notifyItemInserted(allItems.size());
//                }
                sns_comment_re.scrollToPosition(allItems.size() - 1);
            }

            @Override
            public void onFailure(Call<SnsCommentDTO> call, Throwable t) {
                Log.e("comment_write_error",t.getMessage());
            }
        });

    }

    private void getSnsCommentList(int content_id, int page) {

        InterfaceSnsComment list = ApiClient.getClient().create(InterfaceSnsComment.class);
        Call<SnsCommentDTO> call = list.commentlistSns(content_id, page);

        call.enqueue(new Callback<SnsCommentDTO>() {
            @Override
            public void onResponse(Call<SnsCommentDTO> call, Response<SnsCommentDTO> response) {
                int result_code = response.body().getResult_code();
                if (result_code==200 && response.body().getItems_count()>0){
                    int curSize = adapter.getItemCount();
                    allItems.addAll(response.body().getResult_body());
                    sns_comment_re.getRecycledViewPool().setMaxRecycledViews(0,response.body().getItems_count());
                    adapter.addNew(allItems);
                    adapter.notifyItemRangeChanged(curSize,allItems.size()-1);
                    sns_comment_re.scrollToPosition(allItems.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<SnsCommentDTO> call, Throwable t) {
                Log.e("ERROR",t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        BusProvider.getInstance().post(1);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
