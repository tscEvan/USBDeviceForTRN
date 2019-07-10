package com.example.usbdevicefortrn.ownDevice;

public class HIstoryBean {
    String time;
    String user;
    String deviceId;

    public HIstoryBean() {
    }

    public HIstoryBean(String time, String user, String deviceId) {
        this.time = time;
        this.user = user;
        this.deviceId = deviceId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
