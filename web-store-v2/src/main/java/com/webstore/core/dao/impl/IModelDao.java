package com.webstore.core.dao.impl;

import com.webstore.core.dao.ModelDao;
import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Model;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by SGurin on 19.07.2016.
 */
@Repository
public class IModelDao implements ModelDao, Crud<Model> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Model> getAllModels() {
        try {
            Query query = em.createQuery("SELECT m FROM Model m");
            return (List<Model>) query.getResultList();
        } catch (Exception ex) {
            return null;
        }
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
    @Transactional
    public Model add(Model model) {
        return em.merge(model);
    }

    @Override
    public Model find(Model item) {
        return null;
    }

    @Override
    public Model find(int id) {
        return em.find(Model.class, id);
    }

    @Override
    @Transactional
    public boolean delete(Model item) {
        return false;
    }

    @Override
    @Transactional
    public Model update(Model item) {
        return null;
    }
}
