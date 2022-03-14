package com.webstore.core.dao.impl;

import com.webstore.core.base.exception.FindUserRoleException;
import com.webstore.core.dao.RoleDao;
import com.webstore.core.entities.UserRole;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 * Created by Funki on 18.08.2016.
 */
@Repository
public class IRoleDao implements RoleDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public UserRole findUserRoleByName(String name) throws FindUserRoleException {
        try {
            Query query = em.createQuery("Select u from UserRole u where u.roleName = :name ", UserRole.class).setParameter("name", name);
            return (UserRole) query.getSingleResult();
        } catch (NoResultException ex) {
            return this.findUserRoleByName("ROLE_USER");
        } catch (Exception | StackOverflowError e) {
            throw new FindUserRoleException("Роль не найдена");
        }
    }

    @Override
    @Transactional
    public UserRole add(UserRole item) {
        return em.merge(item);
    }

    @Override
    public UserRole find(UserRole item) {
        return null;
    }

    @Override
    public UserRole find(int id) {
        return null;
    }

    @Override
    @Transactional
    public boolean delete(UserRole item) {
        return false;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return false;
    }

    @Override
    @Transactional
    public UserRole update(UserRole item) {
        return null;
    }
}

