package com.npe.horse.travel.sns.list;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by qazz92 on 2017. 10. 10..
 */

public interface InterfaceSnsList {
    @GET("/sns/list/{page}")
    Call<SnsListDTO> listSns(@Path("page") int page);

    @GET("/sns/list/{category}/{hashtag}/{page}")
    Call<SnsListDTO> listSnsForHashTag(@Path("category") int category, @Path("hashtag") String hashtag,  @Path("page") int page);
}
