package com.webstore.core.service;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.User;

import java.util.List;

/**
 * Created by SGurin on 19.07.2016.
 */
public interface UserService extends Crud<User> {

    public User findUserByEmail(String email);

    public List<User> getAllUser();

    public User find(int id);

    public String getPassword(User user);

    public Boolean checkEqualsPassword(String oldPassword,String newPassword);
}
