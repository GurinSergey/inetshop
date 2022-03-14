package com.webstore.web.controller;

import com.webstore.core.base.ChangeEmailForm;
import com.webstore.core.base.ThreadMailSend;
import com.webstore.core.base.exception.FindUserRoleException;
import com.webstore.core.entities.*;
import com.webstore.core.entities.enums.ResultCode;
import com.webstore.core.service.MailSender;
import com.webstore.core.service.RoleService;
import com.webstore.core.service.UserService;
import com.webstore.core.service.impl.IMailService;
import com.webstore.security.services.TokenAuthService;
import com.webstore.web.responses.ComplexObject;
import com.webstore.web.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TokenAuthService tokenAuthService;

    @Autowired
    private IMailService mailService;

    @Qualifier("mailSender")
    @Autowired
    private MailSender mailSender;

    @Autowired
    private MessageSource messageSource;

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getProfile() {

        User user = tokenAuthService.getPrincipal();

        return Response.ok(user);
    }


    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getUsers() {
        return Response.ok(userService.getAllUser());
    }

    @GetMapping(value = "/getById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getUserDetail(@RequestParam(value = "id") int id) {
        return Response.ok(userService.find(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST/*, produces = MediaType.APPLICATION_JSON_VALUE*/)
    @ResponseBody
    public Response updateUser(@RequestBody User userUP) {

        User user = userService.findUserByEmail(userUP.getUsername());

        if ((user.getId() == 0) && (user == null)) {
            return Response.ok(ResultCode.NOT_FOUND, userUP);
        }

        //обновляем полученного пользователя
        user.setBirthdate(userUP.getBirthdate());
        user.setMailConfirm(userUP.getMailConfirm());
        user.setPersonName(userUP.getPersonName());
        user.setSex(userUP.getSex());
        user.setState(userUP.getState());

        Set<UserRole> userRoles = new HashSet<UserRole>();

        //добавляем роли
        for (UserRole userRole : userUP.getAllRoles()) {
            try {
                userRoles.add(roleService.findUserRoleByName(userRole.getRoleName()));
            } catch (FindUserRoleException e) {
                return null;
            }
        }

        user.setUserRoles(userRoles);

        //обновляем пользователя
        userService.update(user);

        return Response.ok(user);
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/checkPass", method = RequestMethod.POST/*, produces = MediaType.APPLICATION_JSON_VALUE*/)
    @ResponseBody
    public Response checkPass(@RequestParam(value = "pass") String pass, HttpServletRequest request) {

        User user = tokenAuthService.getPrincipal();

        if (!userService.checkEqualsPassword(user.getPassword(), pass)) {
            return Response.error("unauthorized", HttpStatus.UNAUTHORIZED);
        }

        String guid = UUID.randomUUID().toString() + "-" + user.getId() * 0.25;

        ForgotList forgotList = new ForgotList(guid, user.getUsername());

        mailService.add(forgotList);

        return Response.ok(ResultCode.CONTINUE, new ComplexObject(guid));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/checkEmail", method = RequestMethod.POST/*, produces = MediaType.APPLICATION_JSON_VALUE*/)
    @ResponseBody
    public Response checkEmail(@RequestParam(value = "email") String email, HttpServletRequest request) {

        String textBody;

        if (userService.findUserByEmail(email) != null) {
            return Response.ok(ResultCode.FOUND);
        }
        ;

        String[] post = email.split("@");
        PostMail postMail = mailService.findUserPostMailByEmail(post[1]);

        String guid = UUID.randomUUID().toString() + "-" + post[0];

        ConfirmList confirmList = new ConfirmList(guid, email);
        mailService.add(confirmList);

        Thread threadMailSend = new Thread(
                new ThreadMailSend(
                        email,
                        guid,
                        messageSource.getMessage("user.confirm3.mail.title", null, Locale.getDefault()),
                        String.format(messageSource.getMessage("user.confirm3.mail.ground", null, Locale.getDefault()),
                                guid
                        ),
                        mailSender)
        );
        threadMailSend.start();

        return Response.ok(ResultCode.COMPLETE, new ComplexObject("").setEmail(email));
    }

    //(public guid: string, public newEmail: string,public guidEmail :string )
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value = {"/changeEmail"}, method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public Response changeEmail(@RequestBody ChangeEmailForm changeEmailForm) {


        ForgotList forgotList = mailService.findForgotByGUID(changeEmailForm.getGuid());
        ConfirmList confirmList = mailService.findConfirmByGUID(changeEmailForm.getGuidEmail());


        if (notValidateBody(changeEmailForm.getNewEmail(), confirmList, forgotList))
            return Response.ok(ResultCode.NOT_FOUND);


        User user = userService.findUserByEmail(forgotList.getEmail());
        if (user == null) return Response.ok(ResultCode.NOT_FOUND);

        user.setUsername(changeEmailForm.getNewEmail());
        user.setPassword(changeEmailForm.getPassword());

        userService.add(user);
        mailService.delete(forgotList);

        return Response.ok(0);
    }

    private boolean notValidateBody(String newEmail, ConfirmList confirmList, ForgotList forgotList) {
        if (forgotList == null || confirmList == null || !newEmail.equals(confirmList.getEmail())) {
            return true;
        }
        return false;
    }


}
