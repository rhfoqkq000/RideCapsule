package com.npe.horse.travel.tourist;

/**
 * Created by JRokH on 2017-10-27.
 */

public class TourContentSingleton {

    private String content_id;
    private String totalCount;

    private TourContentSingleton() {}
    private static class Singleton {
        private static final TourContentSingleton instance = new TourContentSingleton();
    }

    public static TourContentSingleton getInstance () {
        return Singleton.instance;
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }
}
