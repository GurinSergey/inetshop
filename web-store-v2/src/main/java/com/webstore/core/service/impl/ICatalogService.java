package com.webstore.core.service.impl;

import com.webstore.core.dao.CatalogDao;
import com.webstore.core.entities.Catalog;
import com.webstore.core.entities.json.CatalogJson;
import com.webstore.core.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DVaschenko on 12.07.2016.
 */
@Service
public class ICatalogService implements CatalogService {

    @Autowired
    private CatalogDao catalogDao;

    @Override
    public Catalog add(Catalog catalog) {
        Catalog parent;
        Catalog child = catalogDao.add(catalog);

        if(catalog.getParent() != null) {
            parent = catalogDao.find(child.getParent().getId());
            parent.getListItem().add(child);
            catalogDao.update(parent);
        }

        return child;
    }

    @Override
    public Catalog find(Catalog item) {
        return null;
    }

    @Override
    public Catalog find(int id) {
        return catalogDao.find(id);
    }

    @Override
    public boolean delete(Catalog item) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return catalogDao.delete(id);
    }

    @Override
    public Catalog update(Catalog item) {
        return null;
    }

    @Override
    public List<Catalog> getCatalogList() {
        return catalogDao.getCatalogList();
    }

    @Override
    public List<CatalogJson> getCatalogListJson() {
        List<CatalogJson> catalogs = new ArrayList<>();

        for (Catalog catalog : getCatalogList()) {
            catalogs.add(new CatalogJson(catalog));
        }
        return catalogs;
    }

    @Override
    public List<Catalog> getAllCatalogList() {
        return catalogDao.getAllCatalogList();
    }

    @Override
    public List<Catalog> getGroupByIdList(int idGroup) {
        return catalogDao.getGroupByIdList(idGroup);
    }

    @Override
    public List<Catalog> getItemsOfGroup(int idGroup) {
        return catalogDao.getItemsOfGroup(idGroup);
    }
}