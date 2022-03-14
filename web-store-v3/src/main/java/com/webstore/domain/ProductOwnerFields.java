package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

/**
 * Created by DVaschenko on 22.09.2016.
 */
@Entity
@Table(schema = "etc", name = "product_owner_fields")
public class ProductOwnerFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @JsonBackReference(value="user_fields")
    private Product product;

    @Column(name = "custom_value")
    private String customValue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "template_field_id")
    private TemplateFields templateFields;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fields_value_id")
    private TemplateFieldsValue fieldsValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getCustomValue() {
        return customValue;
    }

    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }

    public TemplateFieldsValue getFieldsValue() {
        return fieldsValue;
    }

    public void setFieldsValue(TemplateFieldsValue fieldsValue) {
        this.fieldsValue = fieldsValue;
    }

    public TemplateFields getTemplateFields() {
        return templateFields;
    }

    public void setTemplateFields(TemplateFields templateFields) {
        this.templateFields = templateFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductOwnerFields that = (ProductOwnerFields) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
