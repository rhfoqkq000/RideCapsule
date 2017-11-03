package com.npe.horse.travel.sns.delete;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by pmkjkr on 2017. 10. 30..
 */

public interface InterfaceSnsDelete {
    @GET("/sns/delete/{number}")
    Call<SnsDeleteDTO> deleteSns(@Path("number") int sns_id);
}