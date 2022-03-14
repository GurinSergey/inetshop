package com.webstore.core.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SGurin on 11.07.2016.
 */
@Entity
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "mark")
    @JsonBackReference
    private List<Model> model = new ArrayList<Model>();

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

    public Mark() {
    }

    public Mark(int id) {
        this.id = id;
    }

    public Mark(String name) {
        this.name = name;
    }

    public List<Model> getModel() {
        return model;
    }

    public void setModel(Model mdl) {
        model.add(mdl);
        mdl.setMark(this);
    }
}
