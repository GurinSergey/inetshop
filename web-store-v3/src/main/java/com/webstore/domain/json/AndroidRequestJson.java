package com.webstore.domain.json;

import java.util.List;

/**
 * Created by Andoliny on 09.11.2016.
 */
public class AndroidRequestJson {
    private AndroidAuthData androidAuthData;
    private List<Long> idOrdersList;

    public AndroidRequestJson() {
    }

    public AndroidRequestJson(AndroidAuthData androidAuthData, List<Long> idOrdersList) {
        this.androidAuthData = androidAuthData;
        this.idOrdersList = idOrdersList;
    }

    public AndroidAuthData getAndroidAuthData() {
        return androidAuthData;
    }

    public void setAndroidAuthData(AndroidAuthData androidAuthData) {
        this.androidAuthData = androidAuthData;
    }

    public List<Long> getIdOrdersList() {
        return idOrdersList;
    }

    public void setIdOrdersList(List<Long> idOrdersList) {
        this.idOrdersList = idOrdersList;
    }

    @Override
    public String toString() {
        return "AndroidRequestJson{" +
                "androidAuthData=" + androidAuthData +
                ", idOrdersList=" + idOrdersList +
                '}';
    }
}
