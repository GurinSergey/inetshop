package com.webstore.core.entities;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by MZlenko on 23.08.2016.
 */

@Entity
public class Template{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "state")
    private int state;

    @ManyToOne(fetch = FetchType.EAGER)
    private Catalog catalog;

    @OneToMany(mappedBy = "template", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    @OrderBy(value = "order")
    @JsonManagedReference
    private Set<TemplateFields> templateFields = new HashSet<>();


    public Template() {
    }

    public Template(String name, int state) {
        this.name = name;
        this.state = state;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Set<TemplateFields> getTemplateFields() {
        return templateFields;
    }

    public void addTemplateFields(TemplateFields templateFields) {
        if (templateFields.getOrder() == 0) {
            templateFields.setOrder(this.templateFields.size() + 1);
        }
        this.templateFields.add(templateFields);
        templateFields.setTemplate(this);
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
}
