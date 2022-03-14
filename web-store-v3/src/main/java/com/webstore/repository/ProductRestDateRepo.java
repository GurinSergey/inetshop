package com.webstore.repository;

import com.webstore.domain.ProductRestDate;
import com.webstore.domain.json.ProductRest;
import org.hibernate.annotations.SQLInsert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;
import java.util.Date;
import java.util.List;

public interface ProductRestDateRepo extends JpaRepository<ProductRestDate, Long> {
    /*  @Modifying
      @Query( nativeQuery = true, value = "INSERT INTO rest_date (rest_date, product_id,warehouse_id,rest,plan_rest,purchase) " +
              "VALUES (:rest_date,:product_id,:warehouse_id," +
              "etc.getPreviousRest(:product_id,:warehouse_id,:rest_date)+:purchase," +
              "etc.getPreviousPlanRest(:product_id,:warehouse_id,:rest_date)+:purchase," +
              ":purchase)" +
              " ON DUPLICATE KEY UPDATE rest = rest + :purchase,plan_rest =plan_rest + :purchase, " +
              "purchase=purchase+:purchase ")
      void addPurchase(@Param("rest_date") Date restDate,
                     @Param("product_id") long productId,
                     @Param("warehouse_id") long warehouseId,
                     @Param("purchase") long purchase);
  */
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO vrest_date (rest_date, product_id,warehouse_id,rest,plan_rest,sales) " +
            "VALUES (:rest_date,:product_id,:warehouse_id," +
            "etc.getRest(:product_id,:warehouse_id,:rest_date)-:sales," +
            "etc.getPlanRest(:product_id,:warehouse_id,:rest_date)," +
            ":sales)" +
            " ON DUPLICATE KEY UPDATE rest = rest - :sales, sales=sales+:sales")
    void addSales(@Param("rest_date") Date restDate,
                  @Param("product_id") long productId,
                  @Param("warehouse_id") long warehouseId,
                  @Param("sales") long sales);

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO vrest_date (rest_date, product_id,warehouse_id,rest,plan_rest) " +
            "VALUES (:rest_date,:product_id,:warehouse_id," +
            "etc.getRest(:product_id,:warehouse_id,:rest_date)," +
            "etc.getPlanRest(:product_id,:warehouse_id,:rest_date)-:sales)" +
            " ON DUPLICATE KEY UPDATE plan_rest = plan_rest - :sales")
    void addPlanSales(@Param("rest_date") Date restDate,
                      @Param("product_id") long productId,
                      @Param("warehouse_id") long warehouseId,
                      @Param("sales") long sales);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE vrest_date set plan_rest = plan_rest + :sales " +
            "where product_id = :product_id and rest_date >= :rest_date " +
            "and warehouse_id = :warehouse_id")
    void removePlanSales(@Param("rest_date") Date restDate,
                         @Param("product_id") long productId,
                         @Param("warehouse_id") long warehouseId,
                         @Param("sales") long sales);

    @Procedure("add_purchase")
    void addPurchase(@Param("p_rest_date") Date rest_date,
                     @Param("p_product_id") Long product_id,
                     @Param("p_warehouse_id") long warehouse_id,
                     @Param("p_purchase") long purchase);

    @Query(nativeQuery = true, value = "select getAmountActPart(:p_product_id,:p_order_id) from dual")
    int getAmountActPart(@Param("p_product_id") Long productId,@Param("p_order_id") Long orderId);


}
