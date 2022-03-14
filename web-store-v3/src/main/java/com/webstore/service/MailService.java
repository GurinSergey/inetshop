package com.webstore.service;

import com.webstore.repository.ConfirmListRepo;
import com.webstore.repository.ForgotListRepo;
import com.webstore.repository.MailRepo;
import com.webstore.domain.ConfirmList;
import com.webstore.domain.ForgotList;
import com.webstore.domain.PostMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ALisimenko on 02.08.2016.
 */
@Service
public class MailService {
    @Autowired private MailRepo mailRepo;
    @Autowired private ConfirmListRepo confirmListRepo;
    @Autowired private ForgotListRepo forgotListRepo;

    public PostMail findUserPostMailByEmail(String domen) {
        return  mailRepo.findByDomen(domen);
    }

    public ConfirmList findConfirmByGUID(String guid) {
        return confirmListRepo.findByGuid(guid);
    }

    public ConfirmList addConfirmList(ConfirmList confirmList) {
        return confirmListRepo.save(confirmList);
    }

    public ForgotList addForgotList(ForgotList forgotList) {
        return forgotListRepo.save(forgotList);
    }

    public ForgotList findForgotByGUID(String guid) {
        return forgotListRepo.findByGuid(guid);
    }

    public void deleteForgotList(ForgotList forgotList) {
        forgotListRepo.delete(forgotList);
    }

    public void deleteConfirmList(ConfirmList confirmList) {
        confirmListRepo.delete(confirmList);
    }
}
