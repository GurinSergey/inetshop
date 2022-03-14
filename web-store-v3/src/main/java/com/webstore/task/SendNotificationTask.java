package com.webstore.task;

import com.webstore.base.BaseProperties;
import com.webstore.domain.EmailNotification;
import com.webstore.service.EmailNotificationService;
import com.webstore.service.MailSender;
import com.webstore.service.NewMailSender;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;


public class SendNotificationTask {

    public void send() {

        NewMailSender mailSender = BeanUtil.getBean(NewMailSender.class);
        EmailNotificationService emailNotificationService = BeanUtil.getBean(EmailNotificationService.class);

        List<EmailNotification> emailNotificationList = emailNotificationService.findAllCurrentNotification();
        for (EmailNotification emailNotification : emailNotificationList) {
            final Boolean issend = mailSender.sendEmail(
                    emailNotification.getEmail()
                    , BaseProperties.getMailName()
                    , emailNotification.getHead()
                    ,new String(emailNotification.getText(), StandardCharsets.UTF_8)
            );

            if (issend) {
                emailNotification.setSendstamp(new Date());
                emailNotification.setIssend("X");
                emailNotificationService.update(emailNotification);
            }
        }

    }
}
