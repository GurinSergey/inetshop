package com.webstore.core.dao.impl;

import com.webstore.core.dao.ProductDao;
import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by DVaschenko on 20.07.2016.
 */
@Repository
public class IProductDao implements ProductDao, Crud<Product> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Product add(Product product) {
        return entityManager.merge(product);
    }

    @Override
    public Product find(Product item) {
        return entityManager.find(Product.class, item);
    }

    @Override
    public Product find(int id) {
        return (Product) entityManager
                .createQuery("select p from Product p where p.id = :id")
                .setParameter("id", id)
                .setMaxResults(1)
                .getSingleResult();
    }

    @Override
    @Transactional
    public boolean delete(Product item) {
        entityManager.remove(item);
        return true;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        entityManager.remove(find(id));
        return true;
    }

    @Override
    @Transactional
    public Product update(Product item) {
        return entityManager.merge(item);
    }

    @Override
    public Product findByCode(String code) {
        return (Product) entityManager
                .createQuery("select p from Product p where p.code = :code")
                .setParameter("code", code)
                .setMaxResults(1)
                .getSingleResult();
    }

    @Override
    public List<Product> getProducts() {
        return entityManager.createQuery("select p from Product p where p.visible = true").getResultList();
    }

    @Override
    public List<Product> getAllProducts() {
        return entityManager.createQuery("select p from Product p").getResultList();
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public List<Product> getChosenProductsByUserId(int userId) {
        return entityManager
                .createNativeQuery("select p.* from product p where p.id in (select cp.productID from chosen_products cp where cp.userId = ?1)", Product.class)
                .setParameter(1, userId)
                .getResultList();
    }

    @Override
    public boolean isChosenProduct(int userId, int productId) {
        return entityManager
                .createNativeQuery("select cp.* from chosen_products cp where cp.userId = ?1 and cp.productId = ?2")
                .setParameter(1, userId)
                .setParameter(2, productId)
                .getResultList()
                .size() > 0;
    }

    @Transactional
    public int deleteChosenProducts(int userID, int productId) {
        return entityManager
                .createNativeQuery("DELETE FROM chosen_products WHERE userID = ?1 and productId = ?2")
                .setParameter(1, userID)
                .setParameter(2, productId)
                .executeUpdate();
    }

    @Transactional
    private boolean addChosenProducts(int userID, int productId) {
        try {
            entityManager
                    .createNativeQuery("INSERT INTO chosen_products VALUES(?,?)")
                    .setParameter(1, userID)
                    .setParameter(2, productId)
                    .executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public String mergeChosenProducts(int userID, int productId) {
        if (deleteChosenProducts(userID, productId) != 0) {
            return "delete";
        }
        addChosenProducts(userID, productId);
        return "add";
    }
}