package com.webstore.core.dao;

import com.webstore.core.base.exception.FindUserRoleException;
import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.UserRole;

/**
 * Created by Funki on 18.08.2016.
 */
public interface RoleDao extends Crud<UserRole> {
    public UserRole findUserRoleByName(String name) throws FindUserRoleException;
}
