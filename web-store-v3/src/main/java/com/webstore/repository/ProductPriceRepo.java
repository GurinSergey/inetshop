package com.webstore.repository;

import com.webstore.domain.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPriceRepo extends JpaRepository<ProductPrice, Long> {
}
