package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DVaschenko on 12.07.2016.
 */
@Entity
@Table(schema = "etc", name = "catalog")
public class Catalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    @JsonBackReference
    private Catalog parent;

    @OneToMany(fetch = FetchType.EAGER)
    @OrderColumn(name = "listitem_order")
    @JoinColumn(name = "parent_id")
    @OrderBy(value = "order")
    @JsonManagedReference
    private List<Catalog> listItem;

    @OneToOne(mappedBy = "catalog", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Template template;

    @Column(name = "listitem_order")
    private int order;

    @Column(name = "path")
    private String photoPath;

    @Column(name = "lat_ident")
    private String latIdent;

    @Embeddable
    public static class Builder{
        private String title;
        private Catalog parent;
        private List<Catalog> listItem = new ArrayList<>();
        private List<Product> product = new ArrayList<>();

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

        public Catalog build(){
            return new Catalog(this);
        }
    }

    public Catalog(Builder builder) {
        setTitle(builder.title);
        setParent(builder.parent);
        setListItem(builder.listItem);
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

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getLatIdent() {
        return latIdent;
    }

    public void setLatIdent(String latIdent) {
        this.latIdent = latIdent;
    }
}