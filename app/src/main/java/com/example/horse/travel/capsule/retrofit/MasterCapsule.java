package com.example.horse.travel.capsule.retrofit;

import java.util.ArrayList;

/**
 * Created by horse on 2017. 10. 6..
 */

public class MasterCapsule {
    int result_code;
    ResultCapsule result_body;

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public ResultCapsule getResult_body() {
        return result_body;
    }

    public void setResult_body(ResultCapsule result_body) {
        this.result_body = result_body;
    }
}
