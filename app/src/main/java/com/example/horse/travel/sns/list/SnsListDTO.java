package com.example.horse.travel.sns.list;

import java.util.List;

/**
 * Created by qazz92 on 2017. 10. 10..
 */

public class SnsListDTO {
    private int result_code;
    private List<SnsListItem> result_body;
    private int items_count;

    public int getItems_count() {
        return items_count;
    }

    public int getResult_code() {
        return result_code;
    }

    public List<SnsListItem> getResult_body() {
        return result_body;
    }

//    public class Result_body {
//        private String email;
//        private int id;
//        private String post;
//        private String updated_at;
//
//        public String getEmail() {
//            return email;
//        }
//
//        public void setEmail(String email) {
//            this.email = email;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getPost() {
//            return post;
//        }
//
//        public void setPost(String post) {
//            this.post = post;
//        }
//
//        public String getUpdated_at() {
//            return updated_at;
//        }
//
//        public void setUpdated_at(String updated_at) {
//            this.updated_at = updated_at;
//        }
//    }
}
