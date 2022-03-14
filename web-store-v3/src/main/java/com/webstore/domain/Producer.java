package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Created by DVaschenko on 22.07.2016.
 */
@Entity
@Table(schema = "etc", name = "producer")
public class Producer implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;
    private String pathFlag;
    private String description;

    @OneToMany(mappedBy = "producer", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> product;

    public Producer(String name, String country){
        this.name = name;
        this.country = country;
    }

    public Producer(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPathFlag() {
        return pathFlag;
    }

    public void setPathFlag(String pathFlag) {
        this.pathFlag = pathFlag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producer producer = (Producer) o;
        return Objects.equals(getId(), producer.getId()) &&
                Objects.equals(getName(), producer.getName()) &&
                Objects.equals(getCountry(), producer.getCountry()) &&
                Objects.equals(getPathFlag(), producer.getPathFlag()) &&
                Objects.equals(getDescription(), producer.getDescription()) &&
                Objects.equals(getProduct(), producer.getProduct());
    }

    @Override
    public int hashCode() {

       return Objects.hash(getId(), getName(), getCountry(), getPathFlag(), getDescription(), getProduct());
    }
}
