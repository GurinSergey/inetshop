package com.webstore.repository;

import com.webstore.domain.ProductRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRatingRepo extends JpaRepository<ProductRating, Long> {
//    @Query("select r from ProductRating r where r.product_id = ?1")
//    ProductRating findByProductId(long productId);
}
