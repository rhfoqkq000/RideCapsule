package com.npe.horse.travel.tourist;

/**
 * Created by ekekd on 2017-10-07.
 */

public class Weather {

    private static Weather weather = null;

    String temperature;
    String dust_grade;
    String dust_value;
    String uv;
    String cloud;
    String wind_direction;
    String wind_speed;
    String icon;

    public String getTemperature() {
        return temperature;
    }

    public String getDust_grade() {
        return dust_grade;
    }

    public String getDust_value() {
        return dust_value;
    }

    public String getUv() {
        return uv;
    }

    public String getCloud() {
        return cloud;
    }

    public String getWind_direction() {
        return wind_direction;
    }

    public String getWind_speed() {
        return wind_speed;
    }

    public String getIcon() {
        return icon;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setDust_grade(String dust_grade) {
        this.dust_grade = dust_grade;
    }

    public void setDust_value(String dust_value) {
        this.dust_value = dust_value;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public void setWind_direction(String wind_direction) {
        this.wind_direction = wind_direction;
    }

    public void setWind_speed(String wind_speed) {
        this.wind_speed = wind_speed;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    static Weather getInstance() {
        if(weather == null){
            weather = new Weather();
        }
        return weather;
    }
}
