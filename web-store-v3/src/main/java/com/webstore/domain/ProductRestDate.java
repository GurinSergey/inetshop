package com.webstore.domain;


import com.webstore.base.RestProductId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@IdClass(RestProductId.class)
@Table(schema = "etc", name = "rest_date")
/*@SQLInsert( sql="INSERT INTO rest_date(plan_rest, purchase, rest,  sales ,  " +
        " product_id, rest_date, warehouse_id) VALUES(?,?,?,?,?,?,?)")
@SQLUpdate( sql="UPDATE rest_date SET plan_rest = :plan_rest ,purchase = :purchase, rest = :rest , sales = :sales " +
        "WHERE product_id=:product_id and rest_date = :rest_date and warehouse_id=:warehouse_id")*/

@NamedStoredProcedureQuery(name = "ProductRestDate.addPurchase", procedureName = "add_purchase",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_rest_date", type = Date.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_product_id", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_warehouse_id", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_purchase", type = Long.class)
        })

public class ProductRestDate implements Serializable {
    /*  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
      @DateTimeFormat(pattern = "dd.MM.yyyy")*/
    @Column(name = "rest_date", columnDefinition = "DATE")
    @Temporal(TemporalType.DATE)
    @Id
    private Date restDate = new Date();

    @Column(name = "rest")
    private int rest;
    @Column(name = "plan_rest")
    private int planRest;
    @Column(name = "purchase")
    private int purchase;
    @Column(name = "sales")
    private int sales;
    @Id
    @Column(name = "product_id")
    private long productId;
    @Id
    @Column(name = "warehouse_id")
    private long warehouseId;

    public ProductRestDate() {
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

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(long warehouseId) {
        this.warehouseId = warehouseId;
    }
}
