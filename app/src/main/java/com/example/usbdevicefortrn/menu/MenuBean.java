package com.example.usbdevicefortrn.menu;

import androidx.annotation.Nullable;

public class MenuBean {
    int image;
    String title;
    @Nullable String depiction;

    public MenuBean(int image, String title, @Nullable String depiction) {
        this.image = image;
        this.title = title;
        this.depiction = depiction;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Nullable
    public String getDepiction() {
        return depiction;
    }

    public void setDepiction(@Nullable String depiction) {
        this.depiction = depiction;
    }
}
