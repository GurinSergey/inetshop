package com.webstore.domain.json;

import java.util.List;

/**
 * Created by Andoliny on 09.11.2016.
 */
public class SmsInfoJson {
    private List<OrderJson> ordersJson;

    private String errorMessage="";

    public SmsInfoJson() {
    }

    public SmsInfoJson(List<OrderJson> ordersJson, String errorMessage) {
        this.ordersJson = ordersJson;
        this.errorMessage = errorMessage;
    }

    public List<OrderJson> getOrdersJson() {
        return ordersJson;
    }

    public void setOrdersJson(List<OrderJson> ordersJson) {
        this.ordersJson = ordersJson;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "SmsInfoJson{" +
                "ordersJson=" + ordersJson +
                ", errorMessage=" + errorMessage +
                '}';
    }
}
