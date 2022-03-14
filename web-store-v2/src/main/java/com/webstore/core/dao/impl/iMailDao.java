package com.webstore.core.dao.impl;

import com.webstore.core.dao.MailDao;
import com.webstore.core.entities.ConfirmList;
import com.webstore.core.entities.ForgotList;
import com.webstore.core.entities.PostMail;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Created by ALisimenko on 02.08.2016.
 */
@Repository
public class iMailDao implements MailDao {
    @PersistenceContext
    private EntityManager em;


    @Override
    @Transactional
    public PostMail findUserPostMailByEmail(String domen) {
        //  return null;
        try {
            Query query = em.createQuery("select m from PostMail m where m.domen = ?1").setParameter(1, domen);
            return (PostMail) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }


    @Override
    @Transactional
    public ConfirmList findConfirmByGUID(String guid) {
        try {
            Query query = em.createQuery("select c from ConfirmList c where c.guid = ?1").setParameter(1, guid);
            return (ConfirmList) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }

    }


    @Override
    @Transactional
    public ForgotList findForgotByGUID(String guid) {
        try {

            Query query = em.createQuery("select c from ForgotList c where c.guid = ?1").setParameter(1, guid);
            return (ForgotList) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Transactional
    public ForgotList add(ForgotList item) {
        return em.merge(item);
    }


    @Transactional
    public boolean delete(ForgotList item) {
        try {
            em.remove(item);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public ConfirmList add(ConfirmList item) {
        return em.merge(item);
    }


    @Override
    public ConfirmList find(ConfirmList item) {
        return null;
    }

    @Override
    public ConfirmList find(int id) {
        return null;
    }

    @Override
    public boolean delete(ConfirmList item) {
        return false;
    }


    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public ConfirmList update(ConfirmList item) {
        return null;
    }
}
