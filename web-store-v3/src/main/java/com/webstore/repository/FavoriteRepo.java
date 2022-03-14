package com.webstore.repository;

import com.webstore.domain.FavoriteProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteRepo extends JpaRepository<FavoriteProduct, Long> {
    @Query(nativeQuery = true, value = "select f.* from product_favorite_link f where f.user_id = ?1")
    List<FavoriteProduct> findByUserOrderByFavoriteDate(long userId);
}
