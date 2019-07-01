package com.example.usbdevicefortrn.store;

public class StoreForSellBean {
    Boolean onUse;
    String productKey;
    String deviceId;

    public StoreForSellBean() {
    }

    public StoreForSellBean(Boolean onUse, String productKey, String deviceId) {
        this.onUse = onUse;
        this.productKey = productKey;
        this.deviceId = deviceId;
    }

    public Boolean getOnUse() {
        return onUse;
    }

    public void setOnUse(Boolean onUse) {
        this.onUse = onUse;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
