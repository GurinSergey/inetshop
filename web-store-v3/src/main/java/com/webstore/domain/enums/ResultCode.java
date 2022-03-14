package com.webstore.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResultCode {
    SUCCESSFULL("0"),
    FOUND("1"),
    CONTINUE("10"),
    COMPLETE("20"),
    READ_ONLY("30"),
    BAD_DATA("EXP-1"),
    NOT_FOUND("DAT-1"),
    NO_REST("REST-1"),
    STATE_FORBIDDEN("STATE-1");


    private String resultCode;

    ResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    @JsonValue
    public String toValue() {
      return this.resultCode;
    }
}
