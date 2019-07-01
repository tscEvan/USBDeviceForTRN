package com.example.usbdevicefortrn.userInformation;

public class UserInformationBean {
    String nickname;
    String uid;
    String email;

    public UserInformationBean() {
    }

    public UserInformationBean(String nickname, String uid, String email) {
        this.nickname = nickname;
        this.uid = uid;
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
