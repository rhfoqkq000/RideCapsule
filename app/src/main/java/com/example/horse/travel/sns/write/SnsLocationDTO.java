package com.example.horse.travel.sns.write;

import java.util.List;

/**
 * Created by pmkjkr on 2017. 10. 29..
 */

class SnsLocationDTO {
    private int result_code;
    private List<SnsLocationInfo> result_body;

    int getResult_code() {
        return result_code;
    }

    List<SnsLocationInfo> getResult_body() {
        return result_body;
    }
}
