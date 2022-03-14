package com.webstore.core.entities.json;

import com.webstore.core.entities.OrderDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andoliny on 06.08.2017.
 */
public class OrderDetailsJson {

    private int orderId;
    private ProductJson product;
    private double price;
    private int quantity;
    private double totalPrice;

    public OrderDetailsJson() {
    }

    public OrderDetailsJson(OrderDetails orderDetails){
        this.orderId = orderDetails.getOrder().getOrder_id();
        this.product = new ProductJson(orderDetails.getProduct());
        this.price = orderDetails.getPrice();
        this.quantity = orderDetails.getQuantity();
        this.totalPrice = orderDetails.getTotalPrice();
    }

    public static List<OrderDetailsJson> createOrderDetailsJsonList(List<OrderDetails> orderDetails){
        List<OrderDetailsJson> orderDetailsJsonList = new ArrayList<>();
        if(orderDetails != null){
            for(OrderDetails details: orderDetails){
                orderDetailsJsonList.add(new OrderDetailsJson(details));
            }
        }
        return orderDetailsJsonList;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public ProductJson getProduct() {
        return product;
    }

    public void setProduct(ProductJson product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "OrderDetailsJson{" +
                "orderId=" + orderId +
                ", product=" + product +
                ", price=" + price +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
