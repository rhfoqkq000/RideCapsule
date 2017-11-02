package com.npe.horse.travel.sns.like;

import com.npe.horse.travel.ApiClient;

import retrofit2.Call;

/**
 * Created by qazz92 on 2017. 10. 16..
 */

public class SnsItemUnLike {
    private InterfaceSnsUnLike interfaceSnsUnLike;
    public SnsItemUnLike(){
        interfaceSnsUnLike = ApiClient.getClient().create(InterfaceSnsUnLike.class);
    }

    public Call<SnsItemUnLikeDTO> unlike(String content_id,String user_id){
        return interfaceSnsUnLike.unlike(content_id,user_id);
    }
}
