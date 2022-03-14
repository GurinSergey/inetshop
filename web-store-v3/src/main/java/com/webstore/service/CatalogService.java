package com.webstore.service;

import com.webstore.repository.CatalogRepo;
import com.webstore.domain.Catalog;
import com.webstore.domain.ProductGroup;
import com.webstore.domain.json.CatalogJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CatalogService {

    @Autowired
    private CatalogRepo catalogRepo;

    public Catalog add(Catalog catalog) {
        Catalog parent;
        Catalog child = catalogRepo.save(catalog);

        if(catalog.getParent() != null) {
            parent = catalogRepo.findOne(child.getParent().getId());
            parent.getListItem().add(child);
            catalogRepo.save(parent);
        }

        return child;
    }

    public Catalog find(int id) {
        return catalogRepo.findOne(id);
    }

    public List<Catalog> findByParentIsNull() {
        return catalogRepo.findAllByParentIsNullOrderByOrder();
    }

    public void delete(Catalog item) {
        catalogRepo.delete(item);
    }

    public void delete(int item) {
        catalogRepo.delete(item);
    }

    public List<Catalog> getCatalogList() {
        return catalogRepo.findAllByParentIsNullOrderByOrder();
    }

    public List<CatalogJson> getCatalogListJson() {
        List<CatalogJson> catalogs = new ArrayList<>();

        for (Catalog catalog : getCatalogList()) {
            catalogs.add(new CatalogJson(catalog));
        }
        return catalogs;
    }

    public List<Catalog> getAllCatalogList() {
        return catalogRepo.findAll();
    }

}