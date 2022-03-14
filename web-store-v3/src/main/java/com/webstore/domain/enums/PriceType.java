package com.webstore.domain.enums;

public enum PriceType {
    SALES(0),
    PURCHASE(1),
    OTHER(3),
    FUTURE_PRICE(4);


    private int value;

    private PriceType(int value) {
        this.value = value;
    }
}
