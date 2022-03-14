package com.webstore.repository;

import com.webstore.domain.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepo  extends JpaRepository<Warehouse, Long> {
}
