package com.webstore.core.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by MZlenko on 23.08.2016.
 */

@Entity
public class TemplateFieldsValue{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "templateFields_id")
    @JsonBackReference
    private TemplateFields templateFields;

    @Column(name = "value")
    private String value;


    public TemplateFieldsValue() {
    }

    public TemplateFieldsValue(String value) {
        this.value = value;
    }

    public TemplateFieldsValue(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TemplateFields getTemplateFields() {
        return templateFields;
    }

    public void setTemplateFields(TemplateFields templateFields) {
        this.templateFields = templateFields;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
