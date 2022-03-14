package com.webstore.repository;

import com.webstore.domain.ReservOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservOrderRepo extends JpaRepository<ReservOrderDetail, Long> {
    List<ReservOrderDetail> findAllByOrderId(Long id);

    @Modifying
    @Query(nativeQuery = true, value = "Update reserv_order r set r.type = 1 where r.order_id = :order_id")
    void closeReserve(@Param("order_id") Long orderId);

    List<ReservOrderDetail> getAllByOrderDetailId(Long orderDetailId);
}

