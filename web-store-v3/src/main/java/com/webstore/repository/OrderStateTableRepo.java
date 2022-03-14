package com.webstore.repository;

import com.webstore.domain.OrderStateTable;
import com.webstore.domain.enums.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Andoliny on 31.10.2016.
 */
public interface OrderStateTableRepo extends JpaRepository<OrderStateTable, Integer>{
    OrderStateTable findByState(String stateName);
}
