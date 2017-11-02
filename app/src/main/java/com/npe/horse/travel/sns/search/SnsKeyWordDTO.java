package com.npe.horse.travel.sns.search;

import java.util.Arrays;

/**
 * Created by JRokH on 2017-10-31.
 */

public class SnsKeyWordDTO {
    private int result_code;

    private int items_count;

    private String[] result_body;

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public int getItems_count() {
        return items_count;
    }

    public void setItems_count(int items_count) {
        this.items_count = items_count;
    }

    public String[] getResult_body() {
        return result_body;
    }

    public void setResult_body(String[] result_body) {
        this.result_body = result_body;
    }

    @Override
    public String toString() {
        return "SnsKeyWordDTO{" +
                "result_code=" + result_code +
                ", items_count=" + items_count +
                ", result_body=" + Arrays.toString(result_body) +
                '}';
    }
}
