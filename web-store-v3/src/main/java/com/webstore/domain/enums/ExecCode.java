package com.webstore.domain.enums;

public enum ExecCode {
    SUCCESSFULL(0),
    ERROR(1);

    private int value;

    private ExecCode(int value) {
        this.value = value;
    }
}
