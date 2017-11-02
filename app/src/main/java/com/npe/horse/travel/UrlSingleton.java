package com.npe.horse.travel;

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

    public String getSERVER_URL(){
        return "http://dongaboomin.xyz:20090/";
    }
}
