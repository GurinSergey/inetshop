package com.webstore.core.service.impl;


import com.webstore.core.dao.UserDao;
import com.webstore.core.service.UserService;
import com.webstore.core.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by SGurin on 17.06.2016.
 */
@Service
public class IUserService implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findUserByEmail(String email) {

        return userDao.findUserByEmail(email);
    }

    @Override
    public List<User> getAllUser() {
        return userDao.getAllUser();
    }


    @Override
    public User add(User user) {
        String bcCryptPassword = getPassword(user);
        user.setPassword(bcCryptPassword);
        user.setSex("M");
        return userDao.add(user);
    }

    @Override
    public String getPassword(User user) {
        return passwordEncoder.encode(user.getPassword());
    }

    @Override
    public User find(User item) {
        return null;
    }

    @Override
    public User find(int id) {
        return userDao.find(id);
    }

    @Override
    public boolean delete(User item) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public User update(User user) {
        return userDao.update(user);
    }

    @Override
    public Boolean checkEqualsPassword(String password,String checkPassword){

    //    System.out.println(password);
      //  System.out.println(passwordEncoder.encode(checkPassword));
     //   System.out.println(passwordEncoder.encode("18521258"));
        return passwordEncoder.matches(checkPassword,password);

       // return password.equals(passwordEncoder.encode(checkPassword));

    }
}
