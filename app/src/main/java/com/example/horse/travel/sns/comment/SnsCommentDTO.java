package com.example.horse.travel.sns.comment;

import java.util.List;

/**
 * Created by qazz92 on 2017. 10. 29..
 */

class SnsCommentDTO {
    private int result_code;
    private List<SnsCommentItem> result_body;
    private int items_count;

    public int getItems_count() {
        return items_count;
    }

    public void setItems_count(int items_count) {
        this.items_count = items_count;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public List<SnsCommentItem> getResult_body() {
        return result_body;
    }

    public void setResult_body(List<SnsCommentItem> result_body) {
        this.result_body = result_body;
    }
}
