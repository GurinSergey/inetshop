package com.webstore.core.entities.enums;

/**
 * Created by ALisimenko on 27.02.2018.
 * The best way to map an Enum Type with JPA and Hibernate
 * https://vladmihalcea.com/the-best-way-to-map-an-enum-type-with-jpa-and-hibernate/
 */
public enum UserState {
    ACTIVE(0),
    BLOCKED(1),
    INACTIVE(2);

    private int value;

    private UserState(int value) {
        this.value = value;
    }
}
