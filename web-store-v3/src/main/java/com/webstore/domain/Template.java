package com.webstore.domain;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by MZlenko on 23.08.2016.
 */

@Entity
@Table(schema = "etc", name = "template")
public class Template{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "state")
    private int state;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id")
    @JsonBackReference
    private Catalog catalog;

    @OneToMany(mappedBy = "template", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    @Fetch(FetchMode.SELECT)
    @OrderBy(value = "order")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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

    public void setTemplateFields(Set<TemplateFields> templateFields) {
        this.templateFields = templateFields;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
}
