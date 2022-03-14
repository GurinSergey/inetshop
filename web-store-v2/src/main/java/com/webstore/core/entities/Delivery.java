package com.webstore.core.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by DVaschenko on 09.02.2018.
 */
@Entity
public class Delivery implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "name_delivery")
    private String name;

    public Delivery(){}

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
}
