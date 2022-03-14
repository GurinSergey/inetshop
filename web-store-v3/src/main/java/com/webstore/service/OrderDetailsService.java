package com.webstore.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.webstore.domain.Order;
import com.webstore.domain.Product;
import com.webstore.repository.OrderDetailsRepo;
import com.webstore.domain.OrderDetails;
import com.webstore.repository.OrderRepo;
import com.webstore.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Andoliny on 06.08.2017.
 */
@Service
public class OrderDetailsService {
    @Autowired private OrderDetailsRepo orderDetailsRepo;
    @Autowired private OrderRepo orderRepo;
    @Autowired private ProductRepo productRepo;

    public OrderDetails add(OrderDetails orderDetails) {
        return orderDetailsRepo.save(orderDetails);
    }

    public List<OrderDetails> find(Order order) {
        return orderDetailsRepo.findAllByOrder(order);
    }

    public void delete(OrderDetails orderDetails) {
        orderDetailsRepo.delete(orderDetails);
    }

    public OrderDetails update(OrderDetails orderDetails) {
        return orderDetailsRepo.save(orderDetails);
    }

    public OrderDetails getItem(Order order, Product product) {
        return  orderDetailsRepo.findByOrderAndProduct(order, product);
    }

    public void updateInOrder(Long orderId, JsonNode detailsNodes) {
        if (!(detailsNodes.isNull() || detailsNodes.size() == 0)){
            JsonNode nodeChanged = detailsNodes.path("changedItems");
            JsonNode nodeDeleted = detailsNodes.path("deletedItems");

            for (final JsonNode changed : nodeChanged) {
                OrderDetails orderDetails;
                Long productId = changed.path("productId").asLong();
                if (productId != 0){
                    Order order = orderRepo.findOne(orderId);
                    Product product = productRepo.findOne(productId);
                    orderDetails = getItem(order, product);
                }
                else continue;

                orderDetails.setQuantity(changed.path("quantity").asInt());

                update(orderDetails);
            }
            for (final JsonNode deleted : nodeDeleted) {
                OrderDetails orderDetails;
                Long productId = deleted.path("productId").asLong();
                if (productId != 0){
                    Order order = orderRepo.findOne(orderId);
                    Product product = productRepo.findOne(productId);
                    orderDetails = getItem(order, product);
                }
                else continue;

                delete(orderDetails);
            }
        }
    }
}
