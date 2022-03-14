package com.webstore.core.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PDiachuk on 22.08.2016.
 */
@Entity
@Table(name = "product_group")
public class ProductGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    @JsonManagedReference
    private List<Catalog> catalog = new ArrayList<Catalog>() ;

    @Column(name = "title")
    private String title;

    public ProductGroup(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Catalog> getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog.add(catalog);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
