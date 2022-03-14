package com.webstore.controller;

import com.webstore.domain.User;
import com.webstore.security.services.TokenAuthService;
import com.webstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;

/**
 * Created by DVaschenko on 22.08.2016.
 */
@RestController
public class ProfileRestController {

    @Autowired
    private TokenAuthService tokenAuthService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/saveCommon", method = RequestMethod.POST)
    @ResponseBody
    public boolean saveCommon(
            @RequestParam("personName") String personName,
            @RequestParam("birthDate") String birthDate,
            @RequestParam("sex") String sex
    ) throws Exception {
        User user = tokenAuthService.getPrincipal().getUser();
        user.setPersonName(personName);
        user.setBirthdate(new SimpleDateFormat("yyyy-MM-dd").parse(birthDate));
        user.setSex(sex);

        userService.update(user);

        return true;
    }

}
