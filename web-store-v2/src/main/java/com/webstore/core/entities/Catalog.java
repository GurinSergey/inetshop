package com.webstore.core.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DVaschenko on 12.07.2016.
 */
@Entity
@Table(name = "catalog")
public class Catalog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    @JsonBackReference(value="catalog-parent")
    private Catalog parent;

    @OneToMany(fetch = FetchType.EAGER)
    @OrderColumn(name = "listitem_order")
    @JoinColumn(name = "parent_id")
    @JsonManagedReference(value="catalog-parent")
    private List<Catalog> listItem = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")

    @JsonBackReference
    private ProductGroup productGroup;

    @Embeddable
    public static class Builder{
        private String title;
        private Catalog parent;
        private List<Catalog> listItem = new ArrayList<>();
        private List<Product> product = new ArrayList<>();
        private ProductGroup productGroup;

        public Builder(){}

        public Builder setTitle(String title){
            this.title = title;
            return this;
        }

        public Builder setParent(Catalog parent){
            this.parent = parent;
            return this;
        }

        public Builder setItems(List<Catalog> items){
            listItem = items;
            return this;
        }

        public Builder setProduct(List<Product> product){
            this.product = product;
            return this;
        }

        public Builder setGroup(ProductGroup productGroup){
            this.productGroup = productGroup;
            return this;
        }

        public Catalog build(){
            return new Catalog(this);
        }
    }

    public Catalog(Builder builder) {
        setTitle(builder.title);
        setParent(builder.parent);
        setListItem(builder.listItem);
        //setProduct(builder.product);
        setProductGroup(builder.productGroup);
    }

    public Catalog(){}

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

    public Catalog getParent() {
        return parent;
    }

    public void setParent(Catalog parent) {
        this.parent = parent;
    }

    public List<Catalog> getListItem() {
        return listItem;
    }

    public void setListItem(List<Catalog> listItem) {
        this.listItem = listItem;
    }

    /*public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }*/

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }
}