package com.webstore.base.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ValidationError implements Serializable {
    private List<FieldErrorBnd> fieldErrors = new ArrayList<>();

    public ValidationError() {

    }

    public void addFieldError(String path, String message) {
        FieldErrorBnd error = new FieldErrorBnd(path, message);
        fieldErrors.add(error);
    }

    public List<FieldErrorBnd> getFieldErrors() {
        return fieldErrors;
    }
}
