package com.webstore.core.entities.json;

/**
 * Created by Andoliny on 10.11.2016.
 */
public class AndroidAuthData {
    private String login;
    private String password;

    public AndroidAuthData() {
    }

    public AndroidAuthData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AndroidAuthData{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
