package com.example.horse.travel.sns.like;

import com.example.horse.travel.ApiClient;

import retrofit2.Call;

/**
 * Created by qazz92 on 2017. 10. 16..
 */

public class SnsItemLike {
    private InterfaceSnsLike interfaceSnsLike;
    public SnsItemLike(){
        interfaceSnsLike = ApiClient.getClient().create(InterfaceSnsLike.class);
    }

    public Call<SnsItemLikeDTO> like(String content_id, String user_id){
        return interfaceSnsLike.like(content_id, user_id);
    }
}
