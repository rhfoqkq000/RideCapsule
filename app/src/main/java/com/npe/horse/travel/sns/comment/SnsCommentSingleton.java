package com.npe.horse.travel.sns.comment;


import com.npe.horse.travel.sns.list.SnsListItem;

/**
 * Created by qazz92 on 2017. 10. 29..
 */

public class SnsCommentSingleton {

//    private int content_id;
//    private String content_article;
//    private String updated_at;
//    private String name;
//    private String profileImg;
    private SnsListItem item;

    private SnsCommentSingleton () {}
    private static class Singleton {
        private static final SnsCommentSingleton instance = new SnsCommentSingleton();
    }

    public static SnsCommentSingleton getInstance () {
        return SnsCommentSingleton.Singleton.instance;
    }

    public SnsListItem getItem() {
        return item;
    }

    public void setItem(SnsListItem item) {
        this.item = item;
    }

    //
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getProfileImg() {
//        return profileImg;
//    }
//
//    public void setProfileImg(String profileImg) {
//        this.profileImg = profileImg;
//    }
//
//    public int getContent_id() {
//        return content_id;
//    }
//
//    public void setContent_id(int content_id) {
//        this.content_id = content_id;
//    }
//
//    public String getContent_article() {
//        return content_article;
//    }
//
//    public void setContent_article(String content_article) {
//        this.content_article = content_article;
//    }
//
//    public String getUpdated_at() {
//        return updated_at;
//    }
//
//    public void setUpdated_at(String updated_at) {
//        this.updated_at = updated_at;
//    }
}
