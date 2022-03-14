package com.webstore.service;

import com.webstore.repository.ProductGroupRepo;
import com.webstore.domain.ProductGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by PDiachuk on 23.08.2016.
 */
@Service
public class ProductGroupService {
    @Autowired
    private ProductGroupRepo productGroupRepo;

    public List<ProductGroup> getProductGroupList() {
        return productGroupRepo.findAll();
    }

    public ProductGroup save(ProductGroup productGroup) {
        return productGroupRepo.save(productGroup);
    }

    public ProductGroup find(int id) {
        return productGroupRepo.findOne(id);
    }

    public void delete(ProductGroup productGroup) {
        productGroupRepo.delete(productGroup);
    }

}
