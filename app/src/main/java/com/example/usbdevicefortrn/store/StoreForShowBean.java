package com.example.usbdevicefortrn.store;

public class StoreForShowBean {
    String title;
    String depiction;
    String imgUrl;

    public StoreForShowBean() {
    }

    public StoreForShowBean(String title, String depiction, String imgUrl) {
        this.title = title;
        this.depiction = depiction;
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepiction() {
        return depiction;
    }

    public void setDepiction(String depiction) {
        this.depiction = depiction;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
