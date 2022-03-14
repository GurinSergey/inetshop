package com.webstore.core.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.OrderDetails;

import java.util.List;

/**
 * Created by Andoliny on 06.08.2017.
 */
public interface OrderDetailsService extends Crud<OrderDetails> {
    public List<OrderDetails> getOrderDetails(int orderId);
    public OrderDetails getItem(int orderId, String productName);
    void updateInOrder(int orderId, JsonNode orderDetails);
}
