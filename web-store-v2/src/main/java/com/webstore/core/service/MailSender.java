package com.webstore.core.service;

import java.io.File;
import java.util.List;

/**
 * Created by MZlenko on 29.07.2016.
 */
public interface MailSender {
    public boolean sendEmail(String to, String from, String subject, String bodyText);
    public boolean sendEmail(String to, String from, String subject, String bodyText, File file);
    public boolean sendEmail(String to, String from, String subject, String bodyText, List<File> files);
}
