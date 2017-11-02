package com.npe.horse.travel.hotchu;

import java.util.ArrayList;

/**
 * Created by qazz92 on 2017. 11. 1..
 */

public class HotchuDTO
{
    private int result_code;

    private int items_count;

    private ArrayList<HotchuItem> result_body;

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

    public ArrayList<HotchuItem> getResult_body() {
        return result_body;
    }

    public void setResult_body(ArrayList<HotchuItem> result_body) {
        this.result_body = result_body;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [result_code = "+result_code+", items_count = "+items_count+", result_body = "+result_body+"]";
    }


}

