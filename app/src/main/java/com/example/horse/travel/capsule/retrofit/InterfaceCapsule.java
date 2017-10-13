package com.example.horse.travel.capsule.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by horse on 2017. 10. 6..
 */

public interface InterfaceCapsule {
    @FormUrlEncoded
    @POST("/capsule")
    Call<MasterCapsule> capsule(@Field("contents") String contents
            , @Field("want_date") String want_date
            , @Field("user_id") int user_id);
}
