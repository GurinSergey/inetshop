package com.webstore.domain.enums;

public enum TypeReception {
    RECEPTION(0),
    DELIVERY(1),
    SCHEDULE(2);
    private int value;

    private TypeReception(int value) {
        this.value = value;
    }
}
