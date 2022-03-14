package com.webstore.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by DVaschenko on 22.07.2016.
 */
@Entity
public class Producer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
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

        if (id != producer.id) return false;
        if (name != null ? !name.equals(producer.name) : producer.name != null) return false;
        return !(country != null ? !country.equals(producer.country) : producer.country != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }
}
