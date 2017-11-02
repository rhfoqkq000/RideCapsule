package com.npe.horse.travel.sns.write;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by pmkjkr on 2017. 10. 29..
 */

public interface InterfaceSnsLocationSearch {
    @POST("location/search")
    @FormUrlEncoded
    Call<SnsLocationDTO> searchLocation(@Field("search") String search);

}
