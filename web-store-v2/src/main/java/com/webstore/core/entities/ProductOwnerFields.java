package com.webstore.core.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by DVaschenko on 22.09.2016.
 */
@Entity
public class ProductOwnerFields {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @JsonBackReference(value="user-fields")
    private Product product;

    private String customValue;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "templateField_id")
    private TemplateFields templateFields;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fieldsValue_id")
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
