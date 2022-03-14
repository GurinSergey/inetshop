package com.webstore.repository;

import com.webstore.domain.Order;
import com.webstore.domain.Product;
import com.webstore.domain.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Andoliny on 06.08.2017.
 */
public interface OrderDetailsRepo extends JpaRepository<OrderDetails, Long> {
    public List<OrderDetails> findAllByOrder(Order order);
    public OrderDetails findByOrderAndProduct(Order order, Product product);
}
