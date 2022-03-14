package com.webstore.domain.enums;

public enum DeliveryKind {
    SELF(0),
    NOVA_POSHTA(1);

    private int value;

    private DeliveryKind(int value) {
        this.value = value;
    }
}
