package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by MZlenko on 23.08.2016.
 */

@Entity
@Table(schema = "etc", name = "template_fields_value")
public class TemplateFieldsValue{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "template_fields_id")
    @JsonBackReference
    private TemplateFields templateFields;

    @Column(name = "value")
    private String value;

    public TemplateFieldsValue() {
    }

    public TemplateFieldsValue(String value) {
        this.value = value;
    }

    public TemplateFieldsValue(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
