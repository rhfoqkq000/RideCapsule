package com.npe.horse.travel.sns.search;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by qazz92 on 2017. 10. 10..
 */

public interface InterfaceSnsSearch {
    @GET("/sns/search/{keyword}")
    Call<SnsKeyWordDTO> getKeyword(@Path("keyword") String keyword);
}
