package com.example.horse.travel.sns.write;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.horse.travel.R;

import java.util.ArrayList;

/**
 * Created by JRokH on 2017-10-17.
 */

public class ActivityImageFilter extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_filter);

        ArrayList<Uri> uris = ImageSingleton.getInstance().getImgUri();

        for (Uri uri : uris){
            Log.d("URI",uri.toString());
        }
    }
}
