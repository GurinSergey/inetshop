package com.webstore.core.service;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Order;
import com.webstore.core.entities.json.OrderJson;

import java.util.List;

/**
 * Created by Andoliny on 28.10.2016.
 */
public interface OrderService extends Crud<Order> {
    public List<Order> getAllOrders();
    public List<Order> getNewOrders();
    public List<Order> getCompleteOrders();
    public List<OrderJson> getNewOrdersJson();
    public List<OrderJson> getCompleteOrdersJson();
}
