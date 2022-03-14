package com.webstore.core.service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ALisimenko on 23.08.2016.
 */
public interface RecaptchaService {
    boolean isResponseValid(String remoteIp, String response,HttpServletRequest request);
}
