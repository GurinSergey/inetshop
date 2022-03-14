package com.webstore.web.responses;

import com.webstore.core.entities.enums.ResultCode;
import org.springframework.http.HttpStatus;

import javax.persistence.Enumerated;

public class Response {
    private HttpStatus result;
    private ResultCode resultCode;

    private Object payload;
    private String securityToken;

    public static Response ok(Object payload){
        Response response = new Response();
        response.setPayload(payload);
        response.setResult(HttpStatus.OK);
        response.setResultCode(ResultCode.SUCCESSFULL);
        return response;
    }

    public static Response ok(Object payload, String securityToken){
        Response response = new Response();
        response.setPayload(payload);
        response.setResult(HttpStatus.OK);
        response.setSecurityToken(securityToken);
        return response;
    }

    public static Response ok(ResultCode resultCode, Object payload){
        Response response = new Response();
        response.setPayload(payload);
        response.setResult(HttpStatus.OK);
        response.setResultCode(resultCode);
        return response;
    }


    public static Response error(String errorMessage) {
        return error(errorMessage, HttpStatus.I_AM_A_TEAPOT);
    }

    public static Response error(String errorMessage, HttpStatus httpStatus) {
        Response response = new Response();
        response.setPayload(errorMessage);
        response.setResult(httpStatus);

        return response;
    }

    private static long getSpentTime(long requestStartTime){
        if(requestStartTime > 0){
            return System.currentTimeMillis() - requestStartTime;
        }
        return 0;
    }

    private Response setPayload(Object payload) {
        this.payload = payload;
        return this;
    }

    public HttpStatus getResult() {
        return result;
    }

    public void setResult(HttpStatus result) {
        this.result = result;
    }

    public Object getPayload() {
        return payload;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
