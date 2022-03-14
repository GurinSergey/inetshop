package com.webstore.core.base;

/**
 * Created by Funki on 11.08.2016.
 */
public class BaseProperties {
    private static final String serverIP ;
    private static final int serverPort ;
    private static final String mailName ;

    static {
        serverIP="localhost";
        mailName="tolcom.ua.staff@gmail.com";
        serverPort=8065;
    }

    public static String getMailName() {
        return mailName;
    }

    public static String getServerIP() {
        return serverIP;
    }

    public static int getServerPort() {
        return serverPort;
    }
}
