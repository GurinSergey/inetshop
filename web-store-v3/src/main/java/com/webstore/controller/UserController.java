package com.webstore.controller;

import com.webstore.base.BaseProperties;
import com.webstore.base.ChangeEmailForm;
import com.webstore.base.exception.FindUserRoleException;
import com.webstore.base.ThreadMailSend;
import com.webstore.domain.*;
import com.webstore.domain.enums.ResultCode;
import com.webstore.responses.ComplexObject;
import com.webstore.responses.Response;
import com.webstore.security.services.TokenAuthService;
import com.webstore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
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
    private MailService mailService;

    @Qualifier("mailSenderOld")
    @Autowired
    private MailSender mailSender;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getProfile() {

        User user = tokenAuthService.getCurrentUser();
        return Response.ok(user);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value = {"/updateUserProfile"}, method = RequestMethod.POST)
    public Response updateUserProfile(@RequestBody User user) {

        User userBefore = tokenAuthService.getCurrentUser();

        userBefore.setPersonName(user.getPersonName());
        userBefore.setSex(user.getSex());
        userBefore.setBirthdate(user.getBirthdate());

        //обновляем пользователя
        userService.update(userBefore);

        return Response.ok(userBefore);
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value = {"/sendConfirmEmail"}, method = RequestMethod.POST)
    public Response sendConfirmEmail() {

        User user = tokenAuthService.getCurrentUser();

        String[] post = user.getUserName().split("@");
        String guid = UUID.randomUUID().toString() + "-" + post[0];
        ConfirmList confirmList = new ConfirmList(guid, user.getUserName());
        mailService.addConfirmList(confirmList);
        System.out.println(Locale.getDefault());
        EmailNotification emailNotification = new EmailNotification(user.getUserName(),
                messageSource.getMessage("user.confirm.mail.title", null, Locale.getDefault()),
                String.format(messageSource.getMessage(
                        "user.confirm.mail.ground", null, Locale.getDefault()),
                        BaseProperties.getServerIP(),"",
                     /*   BaseProperties.getServerPort(),*/
                        guid
                ).getBytes(StandardCharsets.UTF_8));
        emailNotificationService.update(emailNotification);

        return Response.ok(ResultCode.SUCCESSFULL,0);
    }


    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getUsers() {
        return Response.ok(userService.getAll());
    }

    @GetMapping(value = "/getById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getUserDetail(@RequestParam(value = "id") int id) {
        return Response.ok(userService.find(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST/*, produces = MediaType.APPLICATION_JSON_VALUE*/)
    @ResponseBody
    public Response updateUser(@RequestBody User user) {
        User userBefore = userService.find(user.getId());
        if (userBefore == null) {
            return Response.ok(ResultCode.NOT_FOUND, user);
        }

        //Это не надо менять, по другому обновить пользователя не дает
        // так как мы в обновленных данных не передаем пароль и получаем ошибку
       //обновляем полученного пользователя

        //ПАШТЕТ! НЕ ТРОГАЙ ЭТОТ КОНТРОЛЛЕР!!!! ХВАТИТ ЕГО ЛОМАТЬ!!!!!

        userBefore.setBirthdate(user.getBirthdate());
        userBefore.setMailConfirm(user.getMailConfirm());
        userBefore.setPersonName(user.getPersonName());
        userBefore.setSex(user.getSex());
        userBefore.setState(user.getState());


        Set<UserRole> userRoles = new HashSet<UserRole>();

        //добавляем роли
       for (UserRole userRole : user.getAllRoles()) {
            try {
                userRoles.add(roleService.findUserRoleByName(userRole.getRoleName()));
            } catch (FindUserRoleException e) {
                return null;
            }
        }

        userBefore.setUserRoles(userRoles);

        //обновляем пользователя
        userService.update(userBefore);

        return Response.ok(userBefore);
    }




    @RequestMapping(value = "/checkPass", method = RequestMethod.POST/*, produces = MediaType.APPLICATION_JSON_VALUE*/)
    @ResponseBody
    public Response checkPass(@RequestParam String pass, HttpServletRequest request) {

        User user = tokenAuthService.getCurrentUser();

        if (!userService.checkEqualsPassword(user.getPassword(), pass)) {
            return Response.error("Incorrect password", HttpStatus.UNAUTHORIZED);
        }

        String guid = UUID.randomUUID().toString() + "-" + user.getId() * 0.25;

        ForgotList forgotList = new ForgotList(guid, user.getUserName());

        mailService.addForgotList(forgotList);

        return Response.ok(ResultCode.CONTINUE, new ComplexObject(guid));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/checkEmail", method = RequestMethod.POST)
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
        mailService.addConfirmList(confirmList);

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

        user.setUserName(changeEmailForm.getNewEmail());
        user.setPassword(changeEmailForm.getPassword());

        userService.add(user);
        mailService.deleteForgotList(forgotList);

        return Response.ok(0);
    }

    private boolean notValidateBody(String newEmail, ConfirmList confirmList, ForgotList forgotList) {
        if (forgotList == null || confirmList == null || !newEmail.equals(confirmList.getEmail())) {
            return true;
        }
        return false;
    }
}
