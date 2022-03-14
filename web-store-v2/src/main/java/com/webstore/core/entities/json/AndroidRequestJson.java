package com.webstore.core.entities.json;

import java.util.List;

/**
 * Created by Andoliny on 09.11.2016.
 */
public class AndroidRequestJson {
    private AndroidAuthData androidAuthData;
    private List<Integer> idOrdersList;

    public AndroidRequestJson() {
    }

    public AndroidRequestJson(AndroidAuthData androidAuthData, List<Integer> idOrdersList) {
        this.androidAuthData = androidAuthData;
        this.idOrdersList = idOrdersList;
    }

    public AndroidAuthData getAndroidAuthData() {
        return androidAuthData;
    }

    public void setAndroidAuthData(AndroidAuthData androidAuthData) {
        this.androidAuthData = androidAuthData;
    }

    public List<Integer> getIdOrdersList() {
        return idOrdersList;
    }

    public void setIdOrdersList(List<Integer> idOrdersList) {
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
