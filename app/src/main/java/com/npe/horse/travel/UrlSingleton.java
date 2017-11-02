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
//        return "http://168.115.229.66:5000/";
    }
  
    public String getPublicServer(){
        return "http://api.visitkorea.or.kr/";
    }

    public String serviceKey(){
        return "mWOUP6hFibrsdKm56wULHkl93YWqbqfALbjYOD9XH/1ASgmGqBlXVo5YZIpfA5P5DgSlFTaggM2zrYBUWiHQug==";
    }
}
