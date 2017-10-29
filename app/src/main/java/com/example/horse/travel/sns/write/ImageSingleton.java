package com.example.horse.travel.sns.write;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by JRokH on 2017-10-17.
 */

public class ImageSingleton {
    private ImageSingleton(){}

    private ArrayList<Uri> imgUri;

    public ArrayList<Uri> getImgUri() {
        return imgUri;
    }

    public void setImgUri(ArrayList<Uri> imgUri) {
        this.imgUri = imgUri;
    }

    private static class Singleton {
        private static final ImageSingleton instance = new ImageSingleton();
    }
    public static ImageSingleton getInstance () {
        System.out.println("create instance");
        return ImageSingleton.Singleton.instance;
    }
}

//
//public class InitializationOnDemandHolderIdiom {
//
//    private InitializationOnDemandHolderIdiom () {}
//    private static class Singleton {
//        private static final InitializationOnDemandHolderIdiom instance = new InitializationOnDemandHolderIdiom();
//    }
//
//    public static InitializationOnDemandHolderIdiom getInstance () {
//        System.out.println("create instance");
//        return Singleton.instance;
//    }
//}

