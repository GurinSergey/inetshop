package com.webstore.core.dao;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.ConfirmList;
import com.webstore.core.entities.ForgotList;
import com.webstore.core.entities.PostMail;

/**
 * Created by ALisimenko on 02.08.2016.
 */
public interface MailDao extends Crud<ConfirmList> {
     PostMail findUserPostMailByEmail(String email);
     ConfirmList findConfirmByGUID(String guid);
     ForgotList findForgotByGUID(String guid);
     ForgotList add(ForgotList forgotList);
     boolean delete(ForgotList forgotList);
}
