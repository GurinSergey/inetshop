package com.webstore.core.dao;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Product;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by DVaschenko on 20.07.2016.
 */
public interface ProductDao extends Crud<Product> {
    Product findByCode(String code);
    List<Product> getProducts();
    List<Product> getAllProducts();

    EntityManager getEntityManager();

    public String mergeChosenProducts(int userID, int productId);
    public List<Product> getChosenProductsByUserId(int userId);

    public boolean isChosenProduct(int userID, int productId);
}