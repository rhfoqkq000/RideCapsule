package com.npe.horse.travel.sns.like;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by qazz92 on 2017. 10. 16..
 */

public interface InterfaceSnsUnLike {
    @FormUrlEncoded
    @POST("/sns/unlike")
    Call<SnsItemUnLikeDTO> unlike(@Field("content_id") String content_id, @Field("user_id") String user_id);
}
