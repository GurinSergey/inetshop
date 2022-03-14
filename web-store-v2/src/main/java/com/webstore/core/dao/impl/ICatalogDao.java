package com.webstore.core.dao.impl;

import com.webstore.core.dao.CatalogDao;
import com.webstore.core.entities.Catalog;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by DVaschenko on 12.07.2016.
 */
@Repository
public class ICatalogDao implements CatalogDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Catalog add(Catalog catalog) {
        return em.merge(catalog);
    }

    @Override
    public Catalog find(Catalog item) {
        return null;
    }

    @Override
    @Transactional
    public boolean delete(Catalog item) {
        return false;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        try {
            em.remove(em.find(Catalog.class, id));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public Catalog update(Catalog catalog) {
        return em.merge(catalog);
    }

    @Override
    public Catalog find(int id) {
        return em.find(Catalog.class, id);
    }

    @Override
    public Catalog findById(int id) {
        return null;
    }

    @Override
    public List<Catalog> getCatalogList() {
        return em.createQuery("select c from Catalog c where c.parent is null").getResultList();
    }

    @Override
    public List<Catalog> getAllCatalogList() {
        return em.createQuery("select c from Catalog c").getResultList();
    }

    @Override
    public List<Catalog> getGroupByIdList(int groupId) {
        return em.createQuery("select c from Catalog c where c.parent is null and c.productGroup.id =?1")
                .setParameter(1, groupId).getResultList();
    }

    @Override
    public List<Catalog> getItemsOfGroup(int groupId) {
        return em.createQuery("select c from Catalog c where c.parent is not null and c.productGroup.id =?1")
                .setParameter(1, groupId).getResultList();
    }
}
