package com.webstore.core.service.impl;


import com.webstore.core.base.exception.FindUserRoleException;
import com.webstore.core.dao.RoleDao;
import com.webstore.core.entities.UserRole;
import com.webstore.core.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Funki on 18.08.2016.
 */
@Service
public class IRoleService implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public UserRole findUserRoleByName(String name) throws FindUserRoleException {
        return roleDao.findUserRoleByName(name);
    }

}
