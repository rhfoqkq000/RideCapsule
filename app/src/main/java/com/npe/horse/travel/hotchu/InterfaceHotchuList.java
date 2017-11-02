package com.npe.horse.travel.hotchu;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by qazz92 on 2017. 11. 1..
 */

interface InterfaceHotchuList {
    @GET("/hotchu")
    public Call<HotchuDTO> getList();
}
