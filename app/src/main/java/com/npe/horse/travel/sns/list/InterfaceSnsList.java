package com.npe.horse.travel.sns.list;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by qazz92 on 2017. 10. 10..
 */

public interface InterfaceSnsList {
    @GET("/sns/{user_id}/list/{page}")
    Call<SnsListDTO> listSns(@Path("user_id") int user_id, @Path("page") int page);

    @GET("/sns/{user_id}/list/{category}/{hashtag}/{page}")
    Call<SnsListDTO> listSnsForHashTag(@Path("user_id") int user_id, @Path("category") int category, @Path("hashtag") String hashtag,  @Path("page") int page);

    @GET("/sns/list/like/{page}/{user_id}")
    Call<SnsListDTO> listSnsLike(@Path("user_id") int user_id, @Path("page") int page);
}