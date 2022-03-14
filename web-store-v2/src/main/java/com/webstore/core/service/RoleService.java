package com.webstore.core.service;

import com.webstore.core.base.exception.FindUserRoleException;
import com.webstore.core.entities.UserRole;

/**
 * Created by Funki on 18.08.2016.
 */
public interface RoleService {

    public UserRole findUserRoleByName(String name) throws FindUserRoleException;
}
