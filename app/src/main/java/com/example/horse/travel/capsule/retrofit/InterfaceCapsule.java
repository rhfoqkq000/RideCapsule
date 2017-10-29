package com.example.horse.travel.capsule.retrofit;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by horse on 2017. 10. 6..
 */

public interface InterfaceCapsule {
    @Multipart
    @POST("/capsule")
    Call<CapsuleDTO> capsule(@Part("contents") RequestBody contents
            , @Part("want_date") RequestBody want_date
            , @Part("user_id") int user_id
            , @Part MultipartBody.Part[] imagefile
            , @Part("together") ArrayList<RequestBody> together_mail);
}
