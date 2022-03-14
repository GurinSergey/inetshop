package com.webstore.repository;

import com.webstore.domain.OrderProtocol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProtocolRepo extends JpaRepository<OrderProtocol,Long> {
      //  OrderProtocol findByOrderId(Long id);
}
