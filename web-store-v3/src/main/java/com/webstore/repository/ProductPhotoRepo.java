package com.webstore.repository;

import com.webstore.domain.ProductPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductPhotoRepo extends JpaRepository<ProductPhoto, Long> {
    List<ProductPhoto> findAllByProductId(long productId);
}
