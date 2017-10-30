package com.example.horse.travel;

/**
 * Created by JRokH on 2017-10-27.
 */

public class UrlSingleton {

    private UrlSingleton () {}
    private static class Singleton {
        private static final UrlSingleton instance = new UrlSingleton();
    }

    public static UrlSingleton getInstance () {
        return Singleton.instance;
    }

    public String getSERVER_URL() {
//        return "http://168.115.204.65:5000/";
        return  "http://220.84.195.101:5000/";
    }
}
