package com.webstore.core.dao.impl;

import com.webstore.core.dao.ProducerDao;
import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Producer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by DVaschenko on 22.07.2016.
 */
@Repository
public class IProducerDao implements ProducerDao, Crud<Producer> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Producer findByName(String name) {
        return entityManager
                .createQuery("select p from Producer p where p.name = :name", Producer.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    @Override
    public List<Producer> getProducers() {
        return entityManager
                .createQuery("select p from Producer p", Producer.class)
                .getResultList();
    }

    @Override
    @Transactional
    public Producer add(Producer producer) {
        return entityManager.merge(producer);
    }

    @Override
    public Producer find(Producer item) {
        return null;
    }

    @Override
    public Producer find(int id) {
        return entityManager.find(Producer.class, id);
    }

    @Override
    @Transactional
    public boolean delete(Producer item) {
        entityManager.remove(item);
        return true;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        entityManager.remove(id);
        return true;
    }

    @Override
    @Transactional
    public Producer update(Producer item) {
        return entityManager.merge(item);
    }
}
