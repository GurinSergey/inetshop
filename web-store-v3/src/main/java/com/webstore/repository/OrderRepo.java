package com.webstore.repository;

import com.webstore.domain.Order;
import com.webstore.domain.OrderStateTable;
import com.webstore.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Andoliny on 28.10.2016.
 */
public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findAllByState(OrderStateTable state);

    @Query(value = "select count(o) from Order o where o.client = ?1")
    Long findAllCountByClient(User user);

    @Query(value = "select o from Order o where o.client = ?1")
    List<Order> findAllByClientWithPagination(User user, Pageable pageable);
}
