package com.webstore.service;


import com.webstore.domain.EmailNotification;
import com.webstore.repository.EmailNotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailNotificationService {

    @Autowired
    public EmailNotificationRepo emailNotificationRepo;

    public EmailNotification update(EmailNotification emailNotification) {

        return  emailNotificationRepo.save(emailNotification);
    }


    public List<EmailNotification> findAllCurrentNotification (){
        return emailNotificationRepo.findAllCurrentNotification();
    }
}
