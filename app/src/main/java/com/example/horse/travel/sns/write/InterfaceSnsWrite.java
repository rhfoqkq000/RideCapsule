package com.example.horse.travel.sns.write;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by qazz92 on 2017. 10. 10..
 */

public interface InterfaceSnsWrite {
    @FormUrlEncoded
    @POST("sns/write")
    Call<SnsWriteDTO> writeSns(@Field("post") String post, @Field("user_id") String user_id, @Field("hash") List<String> hash);
}
