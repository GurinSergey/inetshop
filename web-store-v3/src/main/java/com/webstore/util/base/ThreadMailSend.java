package com.webstore.util.base;

import com.webstore.service.MailSender;

/**
 * Created by ALisimenko on 09.08.2016.
 */
public class ThreadMailSend implements Runnable {
    String email;
    String guid;
    String title;
    String ground;

    private MailSender mailSender;

    public  void  setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public ThreadMailSend(String email, String guid, String title, String ground, MailSender mailSender) {
        this.email = email;
        this.guid = guid;
        this.title = title;
        this.ground = ground;
        this.mailSender = mailSender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Override
    public void run() {
        mailSender.sendEmail(email
                , "tolcom.ua.staff@gmail.com"
                , title
                , ground
        );
    }
}