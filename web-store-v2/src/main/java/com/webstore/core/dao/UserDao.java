package com.webstore.core.dao;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.User;

import java.util.List;

/**
 * Created by SGurin on 17.06.2016.
 */
public interface UserDao extends Crud<User> {
    public User findUserByEmail(String name);
    public List<User> getAllUser();
}
