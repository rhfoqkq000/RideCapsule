package com.example.horse.travel.kakao;

/**
 * Created by pmkjkr on 2017. 11. 2..
 */

public class KakaoSingleton {
    private KakaoSingleton () {}
    private int id;
    private String email, nickname, smallImage;

    private static class Singleton {
        private static final KakaoSingleton instance = new KakaoSingleton();
    }

    public static KakaoSingleton getInstance () {
        return Singleton.instance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
