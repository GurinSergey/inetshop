package com.webstore.core.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by DVaschenko on 27.09.2016.
 */
@Entity
public class ProductPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String path;

    private String name;

    private String format;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @JsonBackReference(value="user-product")
    private Product product;

    public ProductPhoto() {
    }

    public ProductPhoto(String path, String name, String format) {
        this.path = path;
        this.name = name;
        this.format = format;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
