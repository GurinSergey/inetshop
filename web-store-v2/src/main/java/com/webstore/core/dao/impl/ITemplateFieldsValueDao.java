package com.webstore.core.dao.impl;

import com.webstore.core.dao.TemplateFieldsValueDao;
import com.webstore.core.entities.TemplateFieldsValue;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Created by DVaschenko on 22.09.2016.
 */
@Repository
public class ITemplateFieldsValueDao implements TemplateFieldsValueDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public TemplateFieldsValue add(TemplateFieldsValue item) {
        return null;
    }

    @Override
    public TemplateFieldsValue find(TemplateFieldsValue item) {
        return null;
    }

    @Override
    public TemplateFieldsValue find(int id) {
        return entityManager.find(TemplateFieldsValue.class, id);
    }

    @Override
    @Transactional
    public boolean delete(TemplateFieldsValue item) {
        return false;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return false;
    }

    @Override
    @Transactional
    public TemplateFieldsValue update(TemplateFieldsValue item) {
        return null;
    }
}
