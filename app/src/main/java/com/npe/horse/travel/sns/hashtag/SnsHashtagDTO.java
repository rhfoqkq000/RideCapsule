package com.npe.horse.travel.sns.hashtag;

import java.util.ArrayList;

/**
 * Created by pmkjkr on 2017. 10. 30..
 */

public class SnsHashtagDTO {
    private int result_code;
    private ArrayList<SnsHashtagInfo> result_body;

    public int getResult_code() {
        return result_code;
    }
    public ArrayList<SnsHashtagInfo> getResult_body() {
        return result_body;
    }
}
