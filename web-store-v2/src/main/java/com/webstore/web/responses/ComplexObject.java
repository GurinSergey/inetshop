package com.webstore.web.responses;

import java.io.Serializable;

public class ComplexObject implements Serializable {
    private String GUID;
    private String email;

    public String getEmail() {
        return email;
    }

    public ComplexObject setEmail(String email) {
        this.email = email;
        return this;
    }

    public ComplexObject(String GUID) {
        this.GUID = GUID;
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }
}
