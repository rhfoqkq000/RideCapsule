package com.example.horse.travel.kakao;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by pmkjkr on 2017. 11. 2..
 */

public interface InterfaceReg {
    @FormUrlEncoded
    @POST("/reg")
    Call<KakaoRegDTO> reg(@Field("email") String email, @Field("nickname") String nickname, @Field("image") String image);
}