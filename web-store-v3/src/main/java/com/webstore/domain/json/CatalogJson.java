package com.webstore.domain.json;

import com.webstore.domain.Catalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CatalogJson implements Serializable {
    private long id;
    private String value;
    private long parent_id;
    private List<CatalogJson> children = new ArrayList<>();

    public CatalogJson() {
    }

    public CatalogJson(Catalog catalog) {
        id = catalog.getId();
        value = catalog.getTitle();
        parent_id = getParentId(catalog.getParent());

        for (Catalog child : catalog.getListItem()) {
            children.add(new CatalogJson(child));
        }
    }

    private long getParentId(Catalog catalog) {
        try {
            return catalog.getId();
        } catch (NullPointerException e) {
            //TODO Make logging
        }
        return -1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getParent_id() {
        return parent_id;
    }

    public void setParent_id(long parent_id) {
        this.parent_id = parent_id;
    }

    public List<CatalogJson> getChildren() {
        return children;
    }

    public void setChildren(List<CatalogJson> children) {
        this.children = children;
    }
}