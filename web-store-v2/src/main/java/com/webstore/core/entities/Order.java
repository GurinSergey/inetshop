package com.webstore.core.entities;

import com.webstore.core.entities.enums.OrderState;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andoliny on 28.10.2016.
 */
@Entity
@Table(name = "order_head")
public class Order {

    //ИД заказа, уникальный
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private int order_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client")
    private User client;

    @Column(name = "customer_name")
    private String customerName;

 //   @ManyToOne(fetch = FetchType.EAGER)
 //   @JoinColumn(name = "state")
 //LAO how to best use enum in JPA Hybernate link in comment  UserState
    @Enumerated
    @Column(name = "state",columnDefinition = "int")
    private OrderState state = OrderState.NEW;

/*    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "product_id")
    @JsonManagedReference
    private Product product;*/

 /*   @Column(name = "price")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private double price;*/


/*    @Column(name = "count")
    private int count;*/

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false)
    private Date date = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "delivery_date", nullable = false)
    private Date delivery_date;

    @Column(name = "sum")
    private double sum;

    @Column(name = "phone")
    private String phone;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<OrderDetails> orderDetails =new HashSet<>();


    public Order() {
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(Date delivery_date) {
        this.delivery_date = delivery_date;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails.add(orderDetails);
    }

    public Order(User client, String customerName, OrderState state, Date date, Date delivery_date, double sum, String phone, OrderDetails orderDetails) {
        this.client = client;
        this.customerName = customerName;
        this.state = state;
        this.date = date;
        this.delivery_date = delivery_date;
        this.sum = sum;
        this.phone = phone;
        this.orderDetails.add(orderDetails);
    }

    public Order(User client, String customerName, Date delivery_date, String phone) {
        this.client = client;
        this.customerName = customerName;
        this.delivery_date = delivery_date;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Order{" +
                "order_id=" + order_id +
                ", client=" + client +
                ", customerName='" + customerName + '\'' +
                ", state=" + state +
                ", date=" + date +
                ", delivery_date=" + delivery_date +
                ", sum=" + sum +
                ", phone='" + phone + '\'' +
                ", orderDetails=" + orderDetails +
                '}';
    }
}
