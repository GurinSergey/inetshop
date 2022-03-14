package com.webstore.core.service.impl;

import com.webstore.core.dao.MailDao;
import com.webstore.core.entities.ConfirmList;
import com.webstore.core.entities.ForgotList;
import com.webstore.core.entities.PostMail;
import com.webstore.core.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ALisimenko on 02.08.2016.
 */
@Service
public class IMailService implements MailService{
    @Autowired
    private MailDao mailDao;
    @Override
    public PostMail findUserPostMailByEmail(String domen) {
        return  mailDao.findUserPostMailByEmail(domen);
    }

    @Override
    public ConfirmList findConfirmByGUID(String guid) {
        return mailDao.findConfirmByGUID(guid);
    }

    @Override
    public ConfirmList add(ConfirmList confirmList) {
        return mailDao.add(confirmList);
    }

    @Override
    public ForgotList add(ForgotList forgotList) {
        return mailDao.add(forgotList);
    }

    @Override
    public ForgotList findForgotByGUID(String guid) {
        return mailDao.findForgotByGUID(guid);
    }

    @Override
    public boolean delete(ForgotList item) {
        return mailDao.delete(item);
    }
}
