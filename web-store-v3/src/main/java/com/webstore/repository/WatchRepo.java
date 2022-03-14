package com.webstore.repository;


import com.webstore.domain.Product;

import com.webstore.domain.User;
import com.webstore.domain.WatchProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface WatchRepo extends JpaRepository<WatchProduct, Long> {

    @Query("Select wp.product from WatchProduct wp where wp.user<= ?1 group by wp.product.id order by wp.watchDate")
    Set<Product> findAllWatchProductByUser(User user);
}
