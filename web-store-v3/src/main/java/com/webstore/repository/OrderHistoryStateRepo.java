package com.webstore.repository;

import com.webstore.domain.Order;
import com.webstore.domain.OrderHistoryStateList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderHistoryStateRepo extends JpaRepository<OrderHistoryStateList, Long> {
    public List<OrderHistoryStateList> findAllByOrder(Order order);

}

