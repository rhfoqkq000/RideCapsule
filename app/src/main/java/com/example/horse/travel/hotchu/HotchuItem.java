package com.example.horse.travel.hotchu;

import java.util.Date;

/**
 * Created by qazz92 on 2017. 11. 1..
 */

public class HotchuItem {
    private String summary;

    private String img_path;

    private int id;

    private String content;

    private String title;

    private Date updated_at;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [summary = "+summary+", img_path = "+img_path+", id = "+id+", content = "+content+", title = "+title+", updated_at = "+updated_at+"]";
    }
}
