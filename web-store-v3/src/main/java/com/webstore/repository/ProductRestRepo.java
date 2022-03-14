package com.webstore.repository;

import com.webstore.domain.json.ProductRest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.OrderBy;
import java.util.Date;
import java.util.List;

public interface ProductRestRepo extends JpaRepository<ProductRest, Long> {
    List<ProductRest> getCurrentProductRestAll(@Param("rest_date") Date restDate,
                                               @Param("warehouse_id") long warehouseId);
}
