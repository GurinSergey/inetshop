package com.webstore.core.dao;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.ProductGroup;

import java.util.List;

/**
 * Created by PDiachuk on 23.08.2016.
 */
public interface ProductGroupDao extends Crud<ProductGroup> {
    ProductGroup findById(int id);
    List<ProductGroup> getProductGroupList();
}
