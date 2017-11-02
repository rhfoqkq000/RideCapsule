package com.npe.horse.travel.sns.hashtag;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by pmkjkr on 2017. 10. 30..
 */

public interface InterfaceSnsHashtag {
    @GET("/hashtag")
    Call<SnsHashtagDTO> getHashtag();
}