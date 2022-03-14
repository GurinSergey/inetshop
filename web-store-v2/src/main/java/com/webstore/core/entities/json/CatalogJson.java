package com.webstore.core.entities.json;

import com.webstore.core.entities.Catalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DVaschenko on 13.07.2016.
 */
public class CatalogJson implements Serializable {
    private int id;
    private String title;
    private int parent_id;
    private boolean is_span = false;
    private List<CatalogJson> listItem = new ArrayList<>();

    public CatalogJson(Catalog catalog) {
        id = catalog.getId();
        title = catalog.getTitle();
        parent_id = getParentId(catalog.getParent());

        for (Catalog child : catalog.getListItem()) {
            listItem.add(new CatalogJson(child));
        }

        if(catalog.getListItem().size() == 0)
            is_span = true;
    }

    private int getParentId(Catalog catalog) {
        try {
            return catalog.getId();
        } catch (NullPointerException e) {
            //TODO Make logging
        }
        return -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public List<CatalogJson> getListItem() {
        return listItem;
    }

    public void setListItem(List<CatalogJson> listItem) {
        this.listItem = listItem;
    }

    public boolean isIs_span() {
        return is_span;
    }

    public void setIs_span(boolean is_span) {
        this.is_span = is_span;
    }
}