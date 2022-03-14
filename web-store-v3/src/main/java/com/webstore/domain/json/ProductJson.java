package com.webstore.domain.json;

import com.webstore.domain.Product;

public class ProductJson {
    private Long id;
    private String title;
    private String producer;
    private String description;

    public ProductJson(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.producer = product.getProducer().getName();
        this.description = product.getDescription();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
