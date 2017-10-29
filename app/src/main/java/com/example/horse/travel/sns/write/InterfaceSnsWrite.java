package com.example.horse.travel.sns.write;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by qazz92 on 2017. 10. 10..
 */

public interface InterfaceSnsWrite {
    @Multipart
    @POST("sns/write")
    Call<SnsWriteDTO> writeSns(@Part("post") RequestBody post, @Part("hash") List<String> hash, @Part MultipartBody.Part[] imagefile, @Part("user_id") int user_id);
}
