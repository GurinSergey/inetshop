package com.webstore.core.dao.impl;

import com.webstore.core.dao.ProductGroupDao;
import com.webstore.core.entities.ProductGroup;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by PDiachuk on 23.08.2016.
 */
@Repository
public class IProductGroupDao implements ProductGroupDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public ProductGroup findById(int id) {
        return null;
    }

    @Override
    public List<ProductGroup> getProductGroupList() {
        return em.createQuery("select c from ProductGroup c ").getResultList();
    }

    @Override
    @Transactional
    public ProductGroup add(ProductGroup productGroup) {
        return em.merge(productGroup);
    }

    @Override
    public ProductGroup find(ProductGroup item) {
        return null;
    }

    @Override
    public ProductGroup find(int id) {
        return em.find(ProductGroup.class, id);
    }

    @Override
    public boolean delete(ProductGroup item) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public ProductGroup update(ProductGroup productGroup) {
        return em.merge(productGroup);
    }
}

