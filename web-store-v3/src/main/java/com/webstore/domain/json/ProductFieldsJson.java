package com.webstore.domain.json;

import com.webstore.domain.TemplateFieldsValue;

public class ProductFieldsJson {
    private int id;
    private String name;
    private TemplateFieldsValue data;

    public ProductFieldsJson(int id, String name, TemplateFieldsValue data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }

    public ProductFieldsJson() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TemplateFieldsValue getData() {
        return data;
    }

    public void setData(TemplateFieldsValue data) {
        this.data = data;
    }
}
