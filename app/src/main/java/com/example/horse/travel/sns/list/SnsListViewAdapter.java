package com.example.horse.travel.sns.list;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.horse.travel.R;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.util.ArrayList;

/**
 * Created by qazz92 on 2017. 10. 11..
 */

public class SnsListViewAdapter extends BaseAdapter {
    private ArrayList<SnsListItem> listViewItemList = new ArrayList<SnsListItem>() ;

    // ListViewAdapter의 생성자
    public SnsListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();


        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_sns, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView contentTextView = (TextView) convertView.findViewById(R.id.sns_con);
        final TextView userIdTextView = (TextView) convertView.findViewById(R.id.user_id);

        ImageView like = convertView.findViewById(R.id.love);
        final ImageView myImageView = convertView.findViewById(R.id.main_img);


        // Hashtag 설정
        HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(context.getResources().getColor(R.color.blue), new HashTagHelper.OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {
                Log.d("TAG",hashTag);
            }
        });

        mTextHashTagHelper.handle(contentTextView);

        userIdTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UserID",userIdTextView.getText().toString());
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LIKE","CLICK");
            }
        });

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        SnsListItem listViewItem = listViewItemList.get(position);

        // 언더라인
        SpannableString content = new SpannableString(listViewItem.getEmail());
        content.setSpan(new UnderlineSpan(), 0, listViewItem.getEmail().length(), 0);

        // 아이템 내 각 위젯에 데이터 반영
//        iconImageView.setImageDrawable(listViewItem.getIcon());
        Glide.with(convertView).load("http://172.30.1.5:5000/test.jpg").into(myImageView);
        contentTextView.setText(listViewItem.getPost());
        userIdTextView.setText(content);

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String content, String userId) {
        SnsListItem item = new SnsListItem();

        item.setPost(content);
        item.setEmail(userId);

        listViewItemList.add(item);
    }
}
