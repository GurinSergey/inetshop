package com.webstore.core.dao.impl;

import com.webstore.core.dao.TemplateDao;
import com.webstore.core.entities.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

/**
 * Created by MZlenko on 23.08.2016.
 */

@Repository
public class ITemplateDao implements TemplateDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Template add(Template item) {
        return em.merge(item);
    }

    @Override
    public Template find(Template item) {
        return null;
    }

    @Override
    public Template find(int id) {
        return em.find(Template.class, id);
    }

    @Override
    @Transactional
    public boolean delete(Template item) {
        return false;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        em.remove(em.find(Template.class, id));
        return true;
    }

    @Override
    @Transactional
    public Template update(Template item) {
        return em.merge(item);
    }

    @Override
    public List<Template> getAllTemplates() {
        return em.createQuery("select t from Template t where t.state = 0")
                .getResultList();
    }

    @Override
    public void deleteField(Template template, int field_id) {
        if (template == null) return;

        for (TemplateFields templateField : template.getTemplateFields()) {
            if (templateField.getId() == field_id) {
                template.getTemplateFields().remove(templateField);
                break;
            }
        }

    }

    @Override
    public void deleteFieldValue(Template template, int field_value_id) {
        if (template == null) return;

        for (TemplateFields templateField : template.getTemplateFields()) {
            for (TemplateFieldsValue templateFieldsValue : templateField.getTemplateFieldsValues()) {
                if (templateFieldsValue.getId() == field_value_id) {
                    templateField.getTemplateFieldsValues().remove(templateFieldsValue);
                    break;
                }

            }
        }

    }

    @Override
    public List<Template> findListTemplatesByCatalogId(int catalog_id) {
        try {
            Query query = em.createQuery("select t from Template t where t.catalog.id = ?1").setParameter(1, catalog_id);
            return (List<Template>) query.getResultList();
        } catch (NoResultException ex) {
            return null;
        }
    }
}