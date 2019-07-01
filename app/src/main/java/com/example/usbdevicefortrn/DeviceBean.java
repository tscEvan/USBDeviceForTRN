package com.example.usbdevicefortrn;

public class DeviceBean {
    String deviceId;
    String macId;
    String owner;
    double[] key;

    public DeviceBean() {
    }

    public DeviceBean(String deviceId, String macId, String owner, double[] key) {
        this.deviceId = deviceId;
        this.macId = macId;
        this.owner = owner;
        this.key = key;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double[] getKey() {
        return key;
    }

    public void setKey(double[] key) {
        this.key = key;
    }
}
