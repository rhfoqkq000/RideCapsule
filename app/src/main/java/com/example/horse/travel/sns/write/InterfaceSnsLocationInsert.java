package com.example.horse.travel.sns.write;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by pmkjkr on 2017. 10. 29..
 */

public interface InterfaceSnsLocationInsert {
    @FormUrlEncoded
    @POST("/location/insert")
    Call<SnsWriteDTO> writeLocation(@Field("location") String location, @Field("location_alias") String location_alias);
}
