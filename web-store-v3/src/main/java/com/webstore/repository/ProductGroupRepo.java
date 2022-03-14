package com.webstore.repository;

import com.webstore.domain.ProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by PDiachuk on 23.08.2016.
 */
public interface ProductGroupRepo extends JpaRepository<ProductGroup, Integer> {
}
