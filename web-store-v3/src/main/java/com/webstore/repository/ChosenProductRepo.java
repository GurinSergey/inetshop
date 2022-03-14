package com.webstore.repository;

import com.webstore.domain.ChosenProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChosenProductRepo extends JpaRepository<ChosenProduct, Long> {
    ChosenProduct findByUserIdAndProductId(Long userId, Long productId);
    List<ChosenProduct> findAllByUserId(Long userId);
}
