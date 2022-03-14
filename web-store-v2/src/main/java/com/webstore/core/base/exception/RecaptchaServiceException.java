package com.webstore.core.base.exception;

/**
 * Created by ALisimenko on 23.08.2016.
 */
public class RecaptchaServiceException  extends RuntimeException {
    public RecaptchaServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
