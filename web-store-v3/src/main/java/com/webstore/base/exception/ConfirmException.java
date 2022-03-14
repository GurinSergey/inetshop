package com.webstore.base.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by Funki on 19.08.2016.
 */
public class ConfirmException extends UsernameNotFoundException {

    public ConfirmException(String msg) {
        super(msg);
    }
}
