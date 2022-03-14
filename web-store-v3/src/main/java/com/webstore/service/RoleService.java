package com.webstore.service;


import com.webstore.base.exception.FindUserRoleException;
import com.webstore.repository.RoleRepo;
import com.webstore.domain.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired private RoleRepo roleRepo;

    public UserRole findUserRoleByName(String name) throws FindUserRoleException {
        UserRole role = roleRepo.findByRoleName(name);
        if (role == null){
           return this.findUserRoleByName("ROLE_USER");
        }
        return role;
    }

}
