package com.webstore.core.dao.impl;

import com.webstore.core.dao.ModificationDao;
import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Modification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by SGurin on 02.08.2016.
 */
@Repository
public class IModificationDao implements ModificationDao, Crud<Modification> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Modification> getAllModifications() {
        try {
            Query query = em.createQuery("SELECT m FROM Modification m");
            return (List<Modification>) query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public Modification add(Modification modification) {
        return em.merge(modification);
    }

    @Override
    public Modification find(Modification modification) {
        return null;
    }

    @Override
    public Modification find(int id) {
        return em.find(Modification.class, id);
    }

    @Override
    @Transactional
    public boolean delete(Modification modification) {
        return false;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        try {
            em.remove(find(id));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Modification update(Modification modification) {
        return null;
    }
}
