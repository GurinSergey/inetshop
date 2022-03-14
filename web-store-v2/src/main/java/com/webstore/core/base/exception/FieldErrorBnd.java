package com.webstore.core.base.exception;

public class FieldErrorBnd {
    private String field;

    private String message;

    public FieldErrorBnd(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

}
