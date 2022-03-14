package com.webstore.security.config;

public class TokenConfig {
    private int expirationTimeMins = 30000;

    private String[] keys;

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    public int getExpirationTimeMins() {
        return expirationTimeMins;
    }

    public void setExpirationTimeMins(int expirationTimeMins) {
        this.expirationTimeMins = expirationTimeMins;
    }
}
