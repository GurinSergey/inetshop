package com.webstore.domain.enums;

public enum InvoiceState {
    NEW(0, "Отложено"),
    WAIT(1, "Открыто"),
    CLOSED(2, "Закрыто");

    int index;
    String value;

    private InvoiceState(int index, String value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }
}
