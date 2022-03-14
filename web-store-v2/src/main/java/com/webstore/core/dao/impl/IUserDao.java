package com.webstore.core.dao.impl;

import com.webstore.core.dao.UserDao;
import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by SGurin on 17.06.2016.
 */
@Repository
public class IUserDao implements UserDao, Crud<User> {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public User add(User user) {
        return em.merge(user);
    }

    @Override
    public User find(User item) {
        return null;
    }

    @Override
    public User find(int id) {
        return em.find(User.class, id);
    }

    @Override
    @Transactional
    public boolean delete(User item) {
        return false;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return false;
    }

    @Override
    @Transactional
    public User update(User user) {
        return em.merge(user);
    }

    @Override
    public User findUserByEmail(String email) {
        try {
            Query query = em.createQuery("select u from User u where u.username = ?1").setParameter(1, email);
            return (User) query.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<User> getAllUser() {
        try {
            Query query = em.createQuery("select u from User u");
            return (List<User>) query.getResultList();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
