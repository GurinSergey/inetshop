package com.webstore.core.service;

import com.webstore.core.entities.ConfirmList;
import com.webstore.core.entities.ForgotList;
import com.webstore.core.entities.PostMail;

/**
 * Created by ALisimenko on 02.08.2016.
 */
public interface MailService {
    PostMail findUserPostMailByEmail(String domen);

    ConfirmList findConfirmByGUID(String guid);

    ConfirmList add(ConfirmList item);

    ForgotList findForgotByGUID(String guid);

    ForgotList add(ForgotList item);

    boolean delete(ForgotList item);
}
