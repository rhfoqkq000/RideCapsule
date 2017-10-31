package com.example.horse.travel.sns.write;

import android.widget.EditText;

/**
 * Created by pmkjkr on 2017. 10. 29..
 */

public class SnsLocationTextViewSingleton {
    private SnsLocationTextViewSingleton(){}

    private EditText loaction_et, location_alias_et;

    public EditText getLoaction_et() {
        return loaction_et;
    }

    public void setLoaction_et(EditText loaction_et) {
        this.loaction_et = loaction_et;
    }

    public EditText getLocation_alias_et() {
        return location_alias_et;
    }

    public void setLocation_alias_et(EditText location_alias_et) {
        this.location_alias_et = location_alias_et;
    }

    private static class Singleton {
        private static final SnsLocationTextViewSingleton instance = new SnsLocationTextViewSingleton();
    }

    public static SnsLocationTextViewSingleton getInstance () {
        return SnsLocationTextViewSingleton.Singleton.instance;
    }
}
