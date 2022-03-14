package com.webstore.core.dao;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Order;
import com.webstore.core.entities.OrderStateTable;

import java.util.List;

/**
 * Created by Andoliny on 28.10.2016.
 */
public interface OrderDao extends Crud<Order> {
    public List<Order> getAllOrders();
    public List<Order> getOrdersByState(OrderStateTable state);
}
