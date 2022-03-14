package com.webstore.core.service.impl;

import com.webstore.core.dao.ProductGroupDao;
import com.webstore.core.entities.ProductGroup;
import com.webstore.core.service.ProductGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by PDiachuk on 23.08.2016.
 */
@Service
public class IProductGroupService implements ProductGroupService {
    @Autowired
    private ProductGroupDao productGroupDao;

    @Override
    public List<ProductGroup> getProductGroupList() {
        return productGroupDao.getProductGroupList();
    }


    @Override
    public ProductGroup add(ProductGroup productGroup) {
        return productGroupDao.add(productGroup);
    }

    @Override
    public ProductGroup find(ProductGroup productGroup) {
        return null;
    }

    @Override
    public ProductGroup find(int id) {
        return productGroupDao.find(id);
    }

    @Override
    public boolean delete(ProductGroup productGroup) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public ProductGroup update(ProductGroup productGroup) {
        return productGroupDao.update(productGroup);
    }
}
