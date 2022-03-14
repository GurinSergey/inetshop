package com.webstore.repository;

import com.webstore.domain.Catalog;
import com.webstore.domain.ProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatalogRepo extends JpaRepository<Catalog, Integer> {
    List<Catalog> findAllByParentIsNullOrderByOrder();

}
