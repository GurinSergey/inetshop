package com.webstore.domain.enums;

public enum PaymentMethod {
    CASH(0),
    ELECTRONIC(1),
    CREDIT(2),
    VISA_MASTERCARD(3),
    PRIVAT24(4);

    private int value;

    private PaymentMethod(int value) {
        this.value = value;
    }
}
