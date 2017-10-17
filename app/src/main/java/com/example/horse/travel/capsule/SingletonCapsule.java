package com.example.horse.travel.capsule;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by horse on 2017. 10. 16..
 */

public class SingletonCapsule {
    private static SingletonCapsule ourInstance = null;

    private ArrayList<Uri> uri;
    private int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ArrayList<Uri> getUri() {
        return uri;
    }

    public void setUri(ArrayList<Uri> uri) {
        this.uri = uri;
    }

    static SingletonCapsule getInstance() {
        if(ourInstance == null){
            ourInstance = new SingletonCapsule();
        }
        return ourInstance;
    }

    private SingletonCapsule() {
    }
}
