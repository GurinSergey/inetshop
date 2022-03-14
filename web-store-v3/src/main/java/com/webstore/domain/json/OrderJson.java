package com.webstore.domain.json;

import com.webstore.domain.Order;
import com.webstore.util.Utils;
import com.webstore.util.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andoliny on 08.10.2016.
 */
public class OrderJson {
    private long idOrder;
    private String orderDate;
    private String deliveryDate;
    private long idClient;
    private String clientName;
    private String productName;
    private double price;
    private int count;
    private double sum;
    private String phoneNamber;
    private String state;

    public OrderJson() {
    }

    public OrderJson(int idOrder){
        this.idOrder = idOrder;
    }

    public OrderJson(Order order) {
        this.idOrder = order.getOrderId();
        this.orderDate = Utils.getDateByFormat(order.getOrderDate(), Const.DATE_FORMAT_SHORT);
        this.idClient = order.getClient().getId();
        this.clientName = order.getCustomerName();
        this.sum = order.getSum();
        this.phoneNamber = order.getPhone();
        this.deliveryDate = Utils.getDateByFormat(order.getDeliveryDate(), Const.DATE_FORMAT_SHORT);
        this.state = order.getState().name();
    }



    public static List<OrderJson> createOrderJsonList(List<Order> orders){
        List<OrderJson> orderJsons = new ArrayList<>();
        if(orders != null){
            for(Order order: orders){
                orderJsons.add(new OrderJson(order));
            }
        }

        return orderJsons;
    }

    @Override
    public String toString() {
        return "OrderJson{" +
                "idOrder=" + idOrder +
                ", orderDate='" + orderDate + '\'' +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", idClient=" + idClient +
                ", clientName='" + clientName + '\'' +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", sum=" + sum +
                ", phoneNamber='" + phoneNamber + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(long idOrder) {
        this.idOrder = idOrder;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getPhoneNamber() {
        return phoneNamber;
    }

    public long getIdClient() {
        return idClient;
    }

    public void setIdClient(long idClient) {
        this.idClient = idClient;
    }

    public void setPhoneNamber(String phoneNamber) {
        this.phoneNamber = phoneNamber;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}