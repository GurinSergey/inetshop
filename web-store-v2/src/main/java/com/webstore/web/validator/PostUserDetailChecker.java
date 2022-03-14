package com.webstore.web.validator;

import com.webstore.core.base.exception.ConfirmException;
import com.webstore.core.entities.User;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import java.util.Locale;

/**
 * Created by Funki on 22.08.2016.
 */
public class PostUserDetailChecker implements UserDetailsChecker {

    private MessageSource messageSource;

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    /**Добавляем кастомную ошибку
     * Она будет хранится в сессии, получить ее можно
     * через request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION")
     **/
    @Override
    public void check(UserDetails userDetails) {
        User user = (User) userDetails;
        if (user.getMailConfirm() == null) {
            throw new ConfirmException(messageSource.getMessage("user.notconfirm", null, Locale.getDefault()));
        }
    }
}

