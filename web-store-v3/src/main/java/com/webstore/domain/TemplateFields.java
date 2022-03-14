package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by MZlenko on 23.08.2016.
 */

@Entity
@Table(schema = "etc", name = "template_fields")
public class TemplateFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id")
    @JsonBackReference
    private Template template;

    @Column(name = "order_field")
    private int order = 0;

    @Column(name = "name")
    private String name;

    @Column(name = "visible")
    private int visible = 0;


    @OneToMany(mappedBy = "templateFields", fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval=true)
    @JsonManagedReference
    @OrderBy(value = "id")
    private Set<TemplateFieldsValue> templateFieldsValues = new HashSet<TemplateFieldsValue>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public Set<TemplateFieldsValue> getTemplateFieldsValues() {
        return templateFieldsValues;
    }

    public void addTemplateFieldsValues(TemplateFieldsValue templateFieldsValues) {
        this.templateFieldsValues.add(templateFieldsValues);
        templateFieldsValues.setTemplateFields(this);
    }


    public TemplateFields() {
    }

    public TemplateFields(int id) {
        this.id = id;
    }

    public TemplateFields(String name, int visible) {
        this.name = name;
        this.visible = visible;
    }

}
