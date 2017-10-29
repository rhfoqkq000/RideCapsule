package com.example.horse.travel.tourist;

import android.graphics.drawable.Drawable;

/**
 * Created by ekekd on 2017-10-29.
 */

class TourListItem {
    private String tour_title;
    private Drawable tour_image;

    public String getTour_title() {
        return tour_title;
    }

    public void setTour_title(String tour_title) {
        this.tour_title = tour_title;
    }

    public Drawable getTour_image() {
        return tour_image;
    }

    public void setTour_image(Drawable tour_image) {
        this.tour_image = tour_image;
    }
}
