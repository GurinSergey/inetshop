package com.webstore.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MySQLException {
    ER_VIEW_CHECK_FAILED(1369);

    private int resultCode;

    MySQLException(int resultCode) {
        this.resultCode = resultCode;
    }

    @JsonValue
    public int toValue() {
        return this.resultCode;
    }
}
