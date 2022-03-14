package com.webstore.core.entities.enums;

/**
 * Created by ALisimenko on 27.02.2018.
 */
public enum OrderState {
    NEW(0),
    IN_PROCESS(1),
    CLOSED(2),
    REJECTED(3);

    private int value;

    private OrderState(int value) {
        this.value = value;
    }

}
