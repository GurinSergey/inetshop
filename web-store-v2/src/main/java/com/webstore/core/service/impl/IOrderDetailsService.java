package com.webstore.core.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.webstore.core.dao.OrderDetailsDao;
import com.webstore.core.entities.OrderDetails;
import com.webstore.core.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Andoliny on 06.08.2017.
 */
@Service
public class IOrderDetailsService implements OrderDetailsService {
    @Autowired
    private OrderDetailsDao orderDetailsDao;

    @Override
    public OrderDetails add(OrderDetails orderDetails) {
        return orderDetailsDao.add(orderDetails);
    }

    @Override
    public OrderDetails find(OrderDetails orderDetails) {
        return orderDetailsDao.find(orderDetails);
    }

    @Override
    public OrderDetails find(int orderId) {
        return orderDetailsDao.find(orderId);
    }

    @Override
    public boolean delete(OrderDetails orderDetails) {
        return orderDetailsDao.delete(orderDetails);
    }

    @Override
    public boolean delete(int orderId) {
        return orderDetailsDao.delete(orderId);
    }

    @Override
    public OrderDetails update(OrderDetails orderDetails) {
        return orderDetailsDao.update(orderDetails);
    }


    @Override
    public List<OrderDetails> getOrderDetails(int orderId) {
        return orderDetailsDao.getOrderDetails(orderId);
    }

    @Override
    public OrderDetails getItem(int orderId, String productName) {
        return  orderDetailsDao.getItem(orderId, productName);
    }

    @Override
    public void updateInOrder(int orderId, JsonNode detailsNodes) {
        if (!(detailsNodes.isNull() || detailsNodes.size() == 0)){
            JsonNode nodeChanged = detailsNodes.path("changedItems");
            JsonNode nodeDeleted = detailsNodes.path("deletedItems");

            for (final JsonNode changed : nodeChanged) {
                OrderDetails orderDetails;

                if (changed.path("product").asText().length()>0){
                    orderDetails = getItem(orderId, changed.path("product").asText());
                }
                else continue;

                orderDetails.setQuantity(changed.path("quantity").asInt());

                update(orderDetails);
            }
            for (final JsonNode deleted : nodeDeleted) {
                OrderDetails orderDetails;

                if (deleted.asText().length()>0){
                    orderDetails = getItem(orderId, deleted.asText());
                }
                else continue;

                delete(orderDetails);
            }
        }
    }
}
