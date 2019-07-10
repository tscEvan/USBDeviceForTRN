package com.example.usbdevicefortrn.ownDevice;

import java.util.ArrayList;
import java.util.List;

public class DevicesKeyBaseBean {
    String deviceId;
    String version;
    String macAddress;
    List<Double> chaosKey;

    public DevicesKeyBaseBean() {
    }

    public DevicesKeyBaseBean(String deviceId, String version, List<Double> chaosKey) {
        this.deviceId = deviceId;
        this.version = version;
        this.chaosKey = chaosKey;
    }

    public DevicesKeyBaseBean(String deviceId, String version, String macAddress, List<Double> chaosKey) {
        this.deviceId = deviceId;
        this.version = version;
        this.macAddress = macAddress;
        this.chaosKey = chaosKey;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public List<Double> getChaosKey() {
        return chaosKey;
    }

    public void setChaosKey(List<Double> chaosKey) {
        this.chaosKey = chaosKey;
    }
}
