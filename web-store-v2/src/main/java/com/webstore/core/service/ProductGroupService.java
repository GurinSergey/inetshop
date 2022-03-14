package com.webstore.core.service;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.ProductGroup;

import java.util.List;

/**
 * Created by PDiachuk on 23.08.2016.
 */
public interface ProductGroupService extends Crud<ProductGroup> {
    List<ProductGroup> getProductGroupList();
}
