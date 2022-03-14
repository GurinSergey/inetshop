package com.webstore.core.service;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Catalog;
import com.webstore.core.entities.json.CatalogJson;

import java.util.List;

/**
 * Created by DVaschenko on 12.07.2016.
 */
public interface CatalogService extends Crud<Catalog> {
    List<Catalog> getCatalogList();
    List<CatalogJson> getCatalogListJson();
    List<Catalog> getAllCatalogList();
    List<Catalog> getGroupByIdList(int idGroup);
    List<Catalog> getItemsOfGroup(int idGroup);
}