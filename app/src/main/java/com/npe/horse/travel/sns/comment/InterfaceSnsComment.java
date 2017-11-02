package com.npe.horse.travel.sns.comment;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by qazz92 on 2017. 10. 29..
 */

interface InterfaceSnsComment {
    @GET("/sns/{content_id}/comment/{page}")
    Call<SnsCommentDTO> commentlistSns(@Path("content_id") int content_id, @Path("page") int page);

    @FormUrlEncoded
    @POST("/sns/comment/write")
    Call<SnsCommentDTO> commentWrite(@Field("content_id") int content_id, @Field("user_id") int user_id, @Field("article") String article);

}
