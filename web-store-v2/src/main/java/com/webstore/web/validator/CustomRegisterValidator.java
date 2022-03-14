package com.webstore.web.validator;

import com.webstore.core.service.impl.IUserService;
import com.webstore.core.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.*;

/**
 * Created by SGurin on 12.07.2016.
 */
@Component
public class CustomRegisterValidator implements Validator {
    @Autowired
    private IUserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isInstance(User.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        String username = user.getUsername();
        String personName = user.getPersonName();
        String password = user.getPassword();
        String confirmPassword = user.getConfirmPassword();


        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "user.email.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "personName", "user.name.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "user.password.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "user.confirmPassword.empty");

        if (checkMaxSymbol(password)) {
            errors.rejectValue("password", "user.password.check");
        }

        if (!checkEqualsPassword(password,confirmPassword)) {
            errors.rejectValue("confirmPassword", "user.password.missMatch");
        }

        if (userService.findUserByEmail(username) != null) {
            errors.rejectValue("username", "user.email.duplicate");
        }
    }

    public Boolean checkMaxSymbol(String password){
        return password.length() < 7;
    }
    public Boolean checkEqualsPassword(String password,String confirmPassword){
        return password.equals(confirmPassword);

    }
}