package com.webstore.domain.enums;

public enum InvoiceType {
    PURCHASE(0),
    SALES(1);

    private int value;

    private InvoiceType(int value) {
        this.value = value;
    }
}
