package com.webstore.controller;

import com.webstore.base.BaseProperties;
import com.webstore.base.ChangePassForm;
import com.webstore.base.ThreadMailSend;
import com.webstore.base.exception.ConfirmException;
import com.webstore.base.exception.RegisterExeption;
import com.webstore.base.exception.ValidationError;
import com.webstore.domain.*;
import com.webstore.domain.enums.ResultCode;
import com.webstore.responses.Response;
import com.webstore.security.services.TokenAuthService;
import com.webstore.validator.CustomRegisterValidator;
import com.webstore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by DVaschenko on 22.07.2016.
 */
@Controller
public class AccessController {

    @Autowired
    private CustomRegisterValidator customRegisterValidator;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MailService mailService;

    @Qualifier("mailSenderOld")
    @Autowired
    private MailSender mailSender;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private RecaptchaService recaptchaService;

    @Autowired
    private TokenAuthService tokenAuthService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailNotificationService emailNotificationService;


    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    Response login(@RequestBody User user, HttpServletRequest request) {
        return tokenAuthService.addAuthentication(user, request);
    }

    @ResponseBody
    @RequestMapping(value = "/logoutMe", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public Response logoutMe(HttpServletRequest request) {

       /*При логауте хотелось бы делать невалидной подпись джвттокена, но че то оно так не работает
          на форумах пишут что для такого надо делать блеклист и проверять при авторизации на наличие записей
          tokenAuthService.logout(request);*/

        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                cookie.setMaxAge(0);
            }
        }
        return Response.ok(null);
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    Response changePassword(@RequestBody String newPassword, HttpServletRequest request) {
        User associate = tokenAuthService.getCurrentUser();

        associate.setPassword(newPassword);
        userService.add(associate);
        SecurityContextHolder.getContext().setAuthentication(null);
        return Response.ok(null);
    }

    @ResponseBody
    @RequestMapping(value = {"/registry"}, method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public Response setRegister(@RequestBody @Valid User user, BindingResult binding, HttpServletRequest request) {

        try {


            customRegisterValidator.validate(user, binding);

            if (binding.hasErrors()) {

                return Response.ok(ResultCode.BAD_DATA, processFieldErrors(binding.getFieldErrors()));
            }

            user.setUserRole(roleService.findUserRoleByName("ROLE_USER"));
            userService.add(user);

  /*          HttpSession session = request.getSession(false);
            if (session != null) {
                request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", "CONFIRM_MAIL");
            }
*/
            String[] post = user.getUserName().split("@");
            PostMail postMail = mailService.findUserPostMailByEmail(post[1]);

            String guid = UUID.randomUUID().toString() + "-" + post[0];
            ConfirmList confirmList = new ConfirmList(guid, user.getUserName());
            mailService.addConfirmList(confirmList);

            EmailNotification emailNotification = new EmailNotification(user.getUserName(),
                    messageSource.getMessage("user.confirm.mail.title", null, Locale.getDefault()),
                    String.format(messageSource.getMessage(
                            "user.confirm.mail.ground", null, Locale.getDefault()),
                            BaseProperties.getServerIP(), "",
                            //  BaseProperties.getServerPort(),
                            guid
                    ).getBytes(StandardCharsets.UTF_8));
            emailNotificationService.update(emailNotification);


            return Response.ok(user);

        } catch (RegisterExeption e) {
            return Response.error("Произошла ошибка регистрации. Пожалуйста, повторите попытку еще раз");
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/confirm_mail"}, method = RequestMethod.GET)
    public Response setConfirm(Model model, @RequestParam(value = "guid") String guid) {
        if (guid == null) {
            return Response.ok(ResultCode.BAD_DATA, (Object) "Данные скомпрометированы");
        }
        ConfirmList confirmList = mailService.findConfirmByGUID(guid);
        if (confirmList == null) {
            return Response.ok(ResultCode.NOT_FOUND, -101);
        }
        User user = userService.findUserByEmail(confirmList.getEmail());
        if (user != null && user.getMailConfirm() == null) {
            user.setMailConfirm("X");
            userService.update(user);
        } else {
            return Response.ok(ResultCode.NOT_FOUND, -101);

        }

        mailService.deleteConfirmList(confirmList);

        return Response.ok(ResultCode.SUCCESSFULL, 0);
    }

    @ResponseBody
    @RequestMapping(value = {"/sendpass"}, method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public Response sendPass(@RequestBody String email) {

        if (email == null) {
            return Response.ok(ResultCode.BAD_DATA, (Object) "почта не введена");
        }

        User user = userService.findUserByEmail(email);
        if (user == null)
            return Response.ok(ResultCode.NOT_FOUND, -101);

        String guid = UUID.randomUUID().toString() + "-" + user.getId() * 0.25;

        ForgotList forgotList = new ForgotList(guid, email);

        mailService.addForgotList(forgotList);

        Thread threadMailSend = new Thread(
                new ThreadMailSend(
                        email,
                        guid,
                        messageSource.getMessage("user.password.recover.title", null, Locale.getDefault()),
                        String.format(messageSource.getMessage("user.password.recover.ground", null, Locale.getDefault()),
                                BaseProperties.getServerIP(),"",
                                /*BaseProperties.getServerPort(),*/
                                guid
                        ),

                        mailSender)
        );

        threadMailSend.start();

        return Response.ok(email);
    }


    @ResponseBody
    @RequestMapping(value = {"/changepass"}, method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public Response changePass(@RequestBody ChangePassForm changePassForm) {
        Response response;

        response = validatePassForm(changePassForm);

        if (response.getResultCode() != ResultCode.SUCCESSFULL)
            return response;

        ForgotList forgotList = mailService.findForgotByGUID(changePassForm.getGuid());

        if (forgotList == null) {
            response.setResultCode(ResultCode.NOT_FOUND);
            return response;
        }

        User user = userService.findUserByEmail(forgotList.getEmail());

        if (user == null) {
            response.setResultCode(ResultCode.NOT_FOUND);
            return response;
        }

        user.setPassword(changePassForm.getPassword());
        user.setConfirmPassword(changePassForm.getConfirmPassword());

        userService.add(user);
        mailService.deleteForgotList(forgotList);

        return response;
    }

    private Response validatePassForm(@RequestBody ChangePassForm changePassForm) {


        if (changePassForm.getGuid() == null) {
            return Response.ok(ResultCode.NOT_FOUND, -101);
        }
        if (customRegisterValidator.checkMaxSymbol(changePassForm.getPassword())) {
            return Response.ok(ResultCode.BAD_DATA, (Object) messageSource.getMessage("user.password.check", null, Locale.getDefault()));
        }
        if (!customRegisterValidator.checkEqualsPassword(changePassForm.getPassword(), changePassForm.getConfirmPassword())) {
            return Response.ok(ResultCode.BAD_DATA, (Object) messageSource.getMessage("user.password.missMatch", null, Locale.getDefault()));
        }

        return Response.ok(ResultCode.SUCCESSFULL, 0);


    }


    private void setCookie(HttpServletResponse response, String cookieName, String cookieValue, int cookieTime) {
        Cookie cookie = new Cookie(cookieName, cookieValue); //bake cookie
        cookie.setMaxAge(cookieTime); //set expire time to 1000 sec
        response.addCookie(cookie); //put cookie in response
    }

    private boolean isNotConfirm(HttpServletRequest request) {
        Exception exception = (Exception) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        if (exception != null) {
            if (exception.getClass().equals(ConfirmException.class)) {
                return true;
            }
        }
        return false;
    }


    private ValidationError processFieldErrors(List<FieldError> fieldErrors) {
        ValidationError errBnd = new ValidationError();

        for (FieldError fieldError : fieldErrors) {
            String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
            errBnd.addFieldError(fieldError.getField(), localizedErrorMessage);
        }

        return errBnd;
    }

    private String resolveLocalizedErrorMessage(FieldError fieldError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);

        //If the message was not found, return the most accurate field error code instead.
        //You can remove this check if you prefer to get the default error message.
        if (localizedErrorMessage.equals(fieldError.getDefaultMessage())) {
            String[] fieldErrorCodes = fieldError.getCodes();
            localizedErrorMessage = fieldErrorCodes[0];
        }

        return localizedErrorMessage;
    }


    //for 403 access denied page
  /*  @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView accessDenied() {

        ModelAndView model = new ModelAndView();

        //check if user is login
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            model.addObject("name", userDetails.getUsername());
        }

        model.setViewName("403");
        return model;

    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout, HttpServletRequest request) {


        ModelAndView model = new ModelAndView();
        if (error != null) {
            if (isNotConfirm(request)) {
                Exception exception = (Exception) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
                model.addObject("error", exception.getMessage());
                model.addObject("email", "");
                model.setViewName("confirmmail");
                return model;
            } else {
                model.addObject("error", messageSource.getMessage("user.password.email.incorrect", null, Locale.getDefault()));
            }
        }

        model.setViewName("login");

        return model;
    }

    @RequestMapping(value = {"/register"}, method = RequestMethod.GET)
    public ModelAndView getRegister() {

        ModelAndView model = new ModelAndView();
        model.setViewName("register");
        model.addObject("user", new User());
        return model;
    }

    @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
    public String setRegister(@ModelAttribute("user") @Valid User user, BindingResult binding, Model model, HttpServletRequest request) {
        try {
            customRegisterValidator.validate(user, binding);

            if (binding.hasErrors()) {
                model.addAttribute("user", user);
                return "register";
            }

            user.setUserRole(roleService.findUserRoleByName("ROLE_USER"));
            userService.add(user);

            HttpSession session = request.getSession(false);
            if (session != null) {
                request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", "CONFIRM_MAIL");
            }
            return "forward:/confirm";

        } catch (RegisterExeption e) {
            model.addAttribute("registerError", "Произошла ошибка регистрации. Пожалуйста, повторите попытку еще раз");
            return "register";
        }
    }

    @RequestMapping(value = {"/confirm"}, method = {RequestMethod.POST})
    public String getConfirm(Model model, @RequestParam(value = "username") String email, HttpServletRequest request, RedirectAttributes redirectAttributes, HttpServletResponse response) {

        if (request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION") == null && (isNotConfirm(request))) {
            return "redirect:/login";
        }

        User user = userService.findUserByEmail(email);

        if (user != null && user.getMailConfirm() == null) {
            String[] post = email.split("@");
            PostMail postMail = mailService.findUserPostMailByEmail(post[1]);

            String guid = UUID.randomUUID().toString() + "-" + post[0];

            ConfirmList confirmList = new ConfirmList(guid, email);
            mailService.add(confirmList);
            Thread threadMailSend = new Thread(
                    new ThreadMailSend(
                            email,
                            guid,
                            messageSource.getMessage("user.confirm.mail.title", null, Locale.getDefault()),
                            String.format(messageSource.getMessage("user.confirm.mail.ground", null, Locale.getDefault()),
                                    BaseProperties.getServerIP(),
                                    BaseProperties.getServerPort(),
                                    guid
                            ),
                            mailSender)
            );
            threadMailSend.start();

            System.out.println("Отправлено");
            setCookie(response, "CONFIRMEMAIL", "WhatsUPPP", 1000);
            redirectAttributes.addFlashAttribute("title", messageSource.getMessage("user.confirm2.mail.title", null, Locale.getDefault()));
            redirectAttributes.addFlashAttribute("ground", messageSource.getMessage("user.confirm2.mail.ground", null, Locale.getDefault()));

            if (postMail != null) {
                redirectAttributes.addFlashAttribute("postService", postMail.getPostLink());
                redirectAttributes.addFlashAttribute("postName", messageSource.getMessage("user.confirm.mail.enterin", null, Locale.getDefault())
                        + " " + postMail.getPostName());
            }
            request.getSession().removeAttribute("SECURITY_CONFIRM_MAIL");
        }
        return "redirect:/login";
    }


    @RequestMapping(value = {"/confirm_mail"}, method = RequestMethod.GET)
    public String setConfirm(Model model, @RequestParam(value = "guid") String guid, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        if (guid == null) {
            return "redirect:/login";
        }
        ConfirmList confirmList = mailService.findConfirmByGUID(guid);
        User user = userService.findUserByEmail(confirmList.getEmail());
        if (user != null && user.getMailConfirm() == null) {
            user.setMailConfirm("X");
            userService.update(user);

            setCookie(response, "CONFIRMEMAIL", "WhatsUPPP", 1000);

            redirectAttributes.addFlashAttribute("title", messageSource.getMessage("user.registry.succesfully.title", null, Locale.getDefault()));
            redirectAttributes.addFlashAttribute("ground", messageSource.getMessage("user.registry.succesfully.ground", null, Locale.getDefault()));
        }
        return "redirect:/login";
    }

    @RequestMapping(value = {"/forgotpass"}, method = RequestMethod.GET)
    public String forgetPass(Model model) {
        return "/forgotpass";
    }

    @RequestMapping(value = {"/sendpass"}, method = RequestMethod.POST)
    public String sendPass(Model model, @RequestParam(value = "username") String email, RedirectAttributes redirectAttributes, HttpServletResponse response, HttpServletRequest request) {

        if (email == null) {
            model.addAttribute("error", "Invalid email");
            return "/forgotpass";
        }
        // get reCAPTCHA request

        if (!recaptchaService.isResponseValid(null, null, request)) {
            model.addAttribute("error", "Вы не прошли антибот проверку, пожалуйста попробуйте еще раз");
            return "/forgotpass";
        }


        User user = userService.findUserByEmail(email);
        if (user != null) {
            String guid = UUID.randomUUID().toString() + "-" + user.getId() * 0.25;

            ForgotList forgotList = new ForgotList(guid, email);

            mailService.add(forgotList);

            Thread threadMailSend = new Thread(
                    new ThreadMailSend(
                            email,
                            guid,
                            messageSource.getMessage("user.password.recover.title", null, Locale.getDefault()),
                            String.format(messageSource.getMessage("user.password.recover.ground", null, Locale.getDefault()),
                                    BaseProperties.getServerIP(),
                                    BaseProperties.getServerPort(),
                                    guid
                            ),

                            mailSender)
            );

            threadMailSend.start();

            setCookie(response, "CONFIRMEMAIL", "WhatsUPPP", 1000);
            redirectAttributes.addFlashAttribute("title", messageSource.getMessage("user.password.recover2.title", null, Locale.getDefault()));
            redirectAttributes.addFlashAttribute("ground", messageSource.getMessage("user.password.recover2.ground", null, Locale.getDefault()));
        }
        return "redirect:/login";
    }

    @RequestMapping(value = {"/recoverpass"}, method = RequestMethod.GET)
    public String recoverPass(Model model, @RequestParam(value = "guid") String guid, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        if (guid == null) {
            return "redirect:/login";
        }
        ForgotList forgotList = mailService.findForgotByGUID(guid);
        User user = userService.findUserByEmail(forgotList.getEmail());
        if (user != null) {
            model.addAttribute("token", guid);
        }
        return "recoverpass";
    }

    @RequestMapping(value = {"/changepass"}, method = RequestMethod.POST)
    public String changePass(Model model,
                             @RequestParam(value = "password") String password,
                             @RequestParam(value = "confirmPassword") String confirmPassword,
                             @RequestParam(value = "token") String guid
    ) {


        if (guid == null) {
            return "redirect:/login";
        }


        ForgotList forgotList = mailService.findForgotByGUID(guid);
        User user = userService.findUserByEmail(forgotList.getEmail());

        if (customRegisterValidator.checkMaxSymbol(password)) {
            model.addAttribute("errpassword", messageSource.getMessage("user.password.check", null, Locale.getDefault()));
        }
        if (!customRegisterValidator.checkEqualsPassword(password, confirmPassword)) {
            model.addAttribute("errconfirmpassword", messageSource.getMessage("user.password.missMatch", null, Locale.getDefault()));

        }
        if ((model.containsAttribute("errpassword")) || (model.containsAttribute("errconfirmpassword"))) {
            model.addAttribute("token", guid);
            return "recoverpass";
        }

        if (user != null) {
            user.setPassword(password);
            user.setConfirmPassword(confirmPassword);
        }

        userService.add(user);

        return "redirect:/login";
    }*/


}
