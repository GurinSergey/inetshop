package com.webstore.repository;

import com.webstore.base.TemplateInfo;
import com.webstore.domain.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ProductRepo extends JpaRepository<Product, Long> {
    Product findByCode(String code);

    Product findByLatIdent(String ident);

    @Query("Select p from Product p where p.id in ?1")
    List<Product> getPackageProducts (Long[] productId);

    @Query("Select p.template.id from Product p where (p.title like %?1% or p.code like %?1% or p.description like %?1%) group by p.template.id")
    List<Integer> findAllCountProductTemplatesByFilterCond(String filter_str);

    @Query("Select new com.webstore.base.TemplateInfo(p.template.id, p.template.name) from Product p where (p.title like %?1% or p.code like %?1% or p.description like %?1%) group by p.template.id")
    List<TemplateInfo> findAllProductTemplatesByFilterCondWithPagination (String filter_str, Pageable pageable);

    @Query("Select p from Product p where p.template.id = ?1 and (p.title like %?2% or p.code like %?2% or p.description like %?2%)")
    List<Product> findAllProductByTemplateIdAndFilterCondWithPagination (int templateId, String filter_str, Pageable pageable);

    @Query("select p from Product p where p.template.id = ?1")
    List<Product> findAllByTemplateId(int templateId, Pageable pageable);

    @Query("select count(p) from Product p where p.template.id = ?1")
    List<Product> findAllCountByTemplateId(int templateId);

    @Query("Select p from Product p ORDER BY p.statistics.totalPurchases DESC")
    List<Product> getTheBestSellingProducts (Pageable pageable);
}