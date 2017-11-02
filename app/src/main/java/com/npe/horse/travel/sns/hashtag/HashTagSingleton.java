package com.npe.horse.travel.sns.hashtag;

/**
 * Created by JRokH on 2017-10-30.
 */

public class HashTagSingleton {
    private String hash;

    private HashTagSingleton () {}
    private static class Singleton {
        private static final HashTagSingleton instance = new HashTagSingleton();
    }

    public static HashTagSingleton getInstance () {
        return Singleton.instance;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
