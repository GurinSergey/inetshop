package com.webstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class NewMailSender {

    @Autowired
    private JavaMailSender mailSenderNew;

    @Autowired
    private Environment env;

    private Boolean send(String to, String from, String subject, String bodyText) {

        final SimpleMailMessage email = constructEmailMessage(to, subject, bodyText);
        try {
            mailSenderNew.send(email);

        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public boolean sendEmail(String to, String from, String subject, String bodyText) {
        return send(to, from, subject, bodyText);
    }


    private SimpleMailMessage constructEmailMessage(String to, String subject, String body) {
        String recipientAddress = to;
        String confirmationUrl = body;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(confirmationUrl);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

}
