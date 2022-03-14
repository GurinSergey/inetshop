package com.webstore.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.webstore.base.exception.RecaptchaServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Collection;


/**
 * Created by ALisimenko on 23.08.2016.
 */

@Service
public class RecaptchaService {

    private static class RecaptchaResponse {
        @JsonProperty("success")
        private boolean success;

        @JsonProperty("challenge_ts")
        private Timestamp challengeTS;

        @JsonProperty("hostname")
        private String hostname;

        @JsonProperty("error-codes")
        private Collection<String> errorCodes;


        @Override
        public String toString() {
            return "RecaptchaResponse{" +
                    "success=" + success +
                    ", challenge_ts=" + challengeTS +
                    ", hostname=" + hostname +
                    ", errorCodes=" + errorCodes +
                    '}';
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(RecaptchaService.class);
    private final RestTemplate restTemplate;

    private String recaptchaUrl="https://www.google.com/recaptcha/api/siteverify";


    private String recaptchaSecretKey="6LfGNSgTAAAAAE-nauzzgMz8ZC6OvpMR8UNUE6al";

    @Autowired
    public RecaptchaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isResponseValid(String remoteIp, String response,HttpServletRequest request) {
        LOGGER.debug("Validating captcha response for remoteIp={}, response={}", remoteIp, response);
        RecaptchaResponse recaptchaResponse;
        try {
            recaptchaResponse = restTemplate.postForEntity(
                    recaptchaUrl, createBody(recaptchaSecretKey, getRemoteIp(request), request.getParameter("g-recaptcha-response")), RecaptchaResponse.class)
                    .getBody();
        } catch (RestClientException e) {
            throw new RecaptchaServiceException("Recaptcha API not available exception", e);
        }
        if (recaptchaResponse.success) {
            return true;
        } else {
            LOGGER.debug("Unsuccessful recaptchaResponse={}", recaptchaResponse);
            return false;
        }
    }

    private MultiValueMap<String, String> createBody(String secret, String remoteIp, String response) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("secret", secret);
        form.add("remoteip", remoteIp);
        form.add("response", response);
        return form;
    }

    private String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }




}
