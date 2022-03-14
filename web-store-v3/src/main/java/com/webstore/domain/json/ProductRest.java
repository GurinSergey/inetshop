package com.webstore.domain.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.webstore.base.RestProductId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@IdClass(RestProductId.class)
@SqlResultSetMapping(
        name = "productRestMapping",
        classes = {
                @ConstructorResult(
                        targetClass = com.webstore.domain.json.ProductRest.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "code", type = String.class),
                                @ColumnResult(name = "title", type = String.class),
                                @ColumnResult(name = "is_exists", type = Boolean.class),
                                @ColumnResult(name = "price", type = Double.class),
                                @ColumnResult(name = "rest_date", type = Date.class),
                                @ColumnResult(name = "rest", type = Integer.class),
                                @ColumnResult(name = "plan_rest", type = Integer.class),
                                @ColumnResult(name = "purchase", type = Integer.class),
                                @ColumnResult(name = "sales", type = Integer.class),
                                @ColumnResult(name = "warehouse_id", type = Long.class)})})
@NamedNativeQuery(name = "ProductRest.getCurrentProductRestAll",
        query = "SELECT p.id,p.code,p.title,p.is_exists,p.price,t.rest_date,t.rest," +
                "t.plan_rest,t.purchase,t.sales,t.warehouse_id" +
                " FROM rest_date t,product p" +
                " WHERE  t.product_id = p.id" +
                "  and" +
                "    t.warehouse_id = :warehouse_id" +
                "    and" +
                "  t.rest_date = (SELECT MAX(tt.rest_date)" +
                "                                       FROM rest_date tt" +
                "                                       WHERE tt.rest_date <= :rest_date " +
                "                                       AND tt.product_id = t.product_id) order by p.id",
        resultSetMapping = "productRestMapping")
public class ProductRest implements Serializable {
    @Id
    private long productId;
    private String code;
    private String title;
    private boolean isExists;
    private double price;
    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date restDate;
    private int rest;
    private int planRest;
    private int purchase;
    private int sales;
    @Id
    private long warehouseId;

    public ProductRest() {
    }


    public ProductRest(long productId, String code, String title, boolean isExists,
                       double price, Date restDate, int rest, int planRest,
                       int purchase, int sales, long warehouseId) {
        this.productId = productId;
        this.code = code;
        this.title = title;
        this.isExists = isExists;
        this.price = price;
        this.restDate = restDate;
        this.rest = rest;
        this.planRest = planRest;
        this.purchase = purchase;
        this.sales = sales;
        this.warehouseId = warehouseId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public boolean isExists() {
        return isExists;
    }

    public void setExists(boolean exists) {
        isExists = exists;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getRestDate() {
        return restDate;
    }

    public void setRestDate(Date restDate) {
        this.restDate = restDate;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public int getPlanRest() {
        return planRest;
    }

    public void setPlanRest(int planRest) {
        this.planRest = planRest;
    }

    public int getPurchase() {
        return purchase;
    }

    public void setPurchase(int purchase) {
        this.purchase = purchase;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(long warehouseId) {
        this.warehouseId = warehouseId;
    }
}
