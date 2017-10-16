package com.example.horse.travel.sns.list;

import java.util.ArrayList;

/**
 * Created by qazz92 on 2017. 10. 11..
 */

public class SnsListItem {
    private String email;
    private int id;
    private int like_count;
    private int like_id;
    private String like_user;

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getLike_id() {
        return like_id;
    }

    public void setLike_id(int like_id) {
        this.like_id = like_id;
    }

    public String getLike_user() {
        return like_user;
    }

    public void setLike_user(String like_user) {
        this.like_user = like_user;
    }

    private String post;
    private String updated_at;
    private String imgs;

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
