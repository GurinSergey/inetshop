package com.webstore.core.dao.impl;

import com.webstore.core.dao.MarkDao;
import com.webstore.core.entities.Mark;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by SGurin on 11.07.2016.
 */

@Repository
public class IMarkDao implements MarkDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Mark> getAllMarks() {
        try {
            Query query = em.createQuery("SELECT m FROM Mark m");
            return (List<Mark>) query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        try {
            em.remove(em.find(Mark.class, id));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public Mark add(Mark mark) {
        return em.merge(mark);
    }

    @Override
    public Mark find(Mark item) {
        return null;
    }

    @Override
    public Mark find(int id) {
        return em.find(Mark.class, id);
    }

    @Override
    @Transactional
    public boolean delete(Mark item) {
        return false;
    }

    @Override
    @Transactional
    public Mark update(Mark item) {
        return null;
    }
}