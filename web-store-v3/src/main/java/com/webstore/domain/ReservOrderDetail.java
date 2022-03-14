package com.webstore.domain;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "reserv_order")
public class ReservOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id")
    private  Long orderId;
    @Column(name = "product_id")
    private   Long productId;
    @Column(name = "quantity")
    private int quantity;

    @Column(name = "date", columnDefinition="DATE")
    @Temporal(TemporalType.DATE)
    private Date date = new Date();

    @Column(name = "warehouse_id")
    private int warehouseId;

    @Column(name = "invoice_part")
    private Long invoicePartId;

    @Column(name = "order_detail_id")
    private  Long orderDetailId;

    @Column(name = "type") //0 open 1 closed
    private int type;

    public ReservOrderDetail() {
    }

    public ReservOrderDetail(Long id, Long orderId, Long productId, int quantity, Date date, int warehouseId, Long invoicePartId) {
        this.orderDetailId = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.date = date;
        this.warehouseId = warehouseId;
        this.invoicePartId = invoicePartId;


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getInvoicePartId() {
        return invoicePartId;
    }

    public void setInvoicePartId(Long invoicePartId) {
        this.invoicePartId = invoicePartId;
    }

    public Long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
