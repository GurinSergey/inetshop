package com.webstore.domain.json;

import com.webstore.domain.enums.DeliveryKind;
import com.webstore.domain.enums.PaymentMethod;

/**
 * Created by SGurin on 02.03.2018.
 */
public class BasketJson {
    private String fio;
    private String mobile;
    private String email;
    private DeliveryKind deliveryKind;
    private String address;
    private String note;
    private PaymentMethod paymentMethod;
    private boolean callBack;

    public BasketJson() {
    }

    public String getFio() {
        return fio;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public DeliveryKind getDeliveryKind() {
        return deliveryKind;
    }

    public String getAddress() {
        return address;
    }

    public String getNote() {
        return note;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public boolean isCallBack() {
        return callBack;
    }
}

