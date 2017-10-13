package com.example.horse.travel.sns.list;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by qazz92 on 2017. 10. 10..
 */

public interface InterfaceSnsList {
    @GET("/sns/list")
    Call<SnsListDTO> listSns(@Query("page") String page);
}
