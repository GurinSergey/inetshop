package com.webstore.core.service.impl;

import com.webstore.core.service.MailSender;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MZlenko on 29.07.2016.
 */

public class IMailSender implements MailSender {
    private Session session;
    private List<File> attachments = new ArrayList<>();

    public void setSession(Session session) {
        this.session = session;
    }

    private Boolean send(String to, String from, String subject, String bodyText) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(session.getProperty("mailusername"), from));
            message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(getContentMimePart(bodyText));

            for (File attachment : attachments) {
                message.setContent(getAttachmentMimePart(attachment));
            }

            Transport.send(message);
            attachments = new ArrayList<>();

            System.out.println("Message has been successfully sent to " + to);
            return true;

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Something has gone wrong! Message was not sent" + to);
            return false;
        }
    }

    private Multipart getContentMimePart(String content) throws MessagingException {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();

        mimeBodyPart.setContent(content, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();

        multipart.addBodyPart(mimeBodyPart);
        return multipart;

    }

    private Multipart getAttachmentMimePart(File file) throws MessagingException {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);

        mimeBodyPart.setDataHandler(new DataHandler(source));
        mimeBodyPart.setFileName(file.getName());
        multipart.addBodyPart(mimeBodyPart);
        return multipart;

    }

    @Override
    public boolean sendEmail(String to, String from, String subject, String bodyText) {
        return send(to, from, subject, bodyText);
    }

    @Override
    public boolean sendEmail(String to, String from, String subject, String bodyText, File file) {
        attachments.add(file);
        return sendEmail(to, from, subject, bodyText);
    }

    @Override
    public boolean sendEmail(String to, String from, String subject, String bodyText, List<File> files) {
        attachments.addAll(files);
        return sendEmail(to, from, subject, bodyText);

    }
}