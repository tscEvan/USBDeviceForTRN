package com.example.usbdevicefortrn.ownDevice;

public class OwnDeviceBean {
    int type;
    String deviceId;
    String deviceName;
    int permission;

    public OwnDeviceBean() {
    }

    public OwnDeviceBean(int type, String deviceId, String deviceName, int permission) {
        this.type = type;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.permission = permission;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}
