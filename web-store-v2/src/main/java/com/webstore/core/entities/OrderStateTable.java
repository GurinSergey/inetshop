package com.webstore.core.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andoliny on 31.10.2016.
 */
@Entity
@Table(name = "order_state")
public class OrderStateTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "state")
    @JsonBackReference
    private List<Order> orders = new ArrayList<Order>() ;

    @Column(name = "state")
    private String state;

    public OrderStateTable() {
    }

    public OrderStateTable(List<Order> orders, String state) {
        this.orders = orders;
        this.state = state;
    }

    public OrderStateTable(String stateName) {
        this.state = stateName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

   public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(Order order) {
        this.orders.add(order);
    }

    public String getState() {
        return state;
    }

    public void setState(String stateName) {
        this.state = stateName;
    }

  /*  @Override
    public String toString() {
        return "OrderStateTable{" +
                "id=" + id +
                ", orders=" + orders +
                ", state='" + state + '\'' +
                '}';
    }*/
}
