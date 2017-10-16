package com.example.horse.travel;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by qazz92 on 2017. 10. 11..
 */

public class ApiClient {

    private static String BASE_URL = "http://169.254.149.208:5000";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
//
//    public Retrofit getClient(){
//        return new Retrofit.Builder().baseUrl("http://172.30.1.5:5000")
//                    .addConverterFactory(GsonConverterFactory.create()).build();
//    }
}
