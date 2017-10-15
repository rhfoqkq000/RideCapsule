package com.example.horse.travel.sns.write;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;

import com.example.horse.travel.R;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qazz92 on 2017. 10. 10..
 */

public class ActivityImageSelect extends AppCompatActivity {

    @BindView(R.id.toolbar_home)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

    }
}
