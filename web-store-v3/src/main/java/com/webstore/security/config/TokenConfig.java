package com.webstore.security.config;

public class TokenConfig {

    public static final String[] keys = new String[]{"Xa8fHLEP9leri2N", "Xa8fHLEP9leri2N", "DFg1wc3CbfPMo7V", "wrFJS9Gu4dizAna",
            "FdNvZGhs5b3Jxmv", "Xw8Fld2NWaTDxVi", "S6Elo9DGdCKTP0Y", "01CA9WlzEh8myri",
            "5KkQtUHSRPy1wg0", "cO5a21P7dp4zYNu"};

    private int expirationTimeMins = 30000;

    public String[] getKeys() {
        return keys;
    }

    public int getExpirationTimeMins() {
        return expirationTimeMins;
    }

    public void setExpirationTimeMins(int expirationTimeMins) {
        this.expirationTimeMins = expirationTimeMins;
    }


}
