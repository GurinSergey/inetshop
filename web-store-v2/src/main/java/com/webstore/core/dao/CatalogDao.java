package com.webstore.core.dao;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Catalog;

import java.util.List;

/**
 * Created by DVaschenko on 12.07.2016.
 */
public interface CatalogDao extends Crud<Catalog> {
    Catalog findById(int id);
    List<Catalog> getCatalogList();
    List<Catalog> getAllCatalogList();
    List<Catalog> getGroupByIdList(int groupId);
    List<Catalog> getItemsOfGroup(int groupId);
}
