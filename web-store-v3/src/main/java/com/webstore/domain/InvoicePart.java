package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.webstore.domain.enums.InvoiceState;

import javax.persistence.*;

@Entity
@Table(schema = "etc", name = "invoice_part")
public class InvoicePart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "invoice_id")
    @JsonManagedReference(value="invoicePart")
    private Invoice invoice;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "initial_price")
    private double initialPrice;

    @Column(name = "initial_amount")
    private int initialAmount;

    @Column(name = "rest_amount")
    private int restAmount;

    @Column(name = "sales_price")
    private double salesPrice;

    @Enumerated
    @Column(name = "state", columnDefinition = "int")
    private InvoiceState state = InvoiceState.NEW;


    @Column(name = "active")
    private boolean isActive = false;

    public InvoicePart() {
    }

    public InvoicePart(Invoice invoice,Product product, double initialPrice, int initialAmount) {

        this.invoice = invoice;
        this.product = product;
        this.initialPrice = initialPrice;
        this.initialAmount = initialAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(double initialPrice) {
        this.initialPrice = initialPrice;
    }

    public int getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(int initialAmount) {
        this.initialAmount = initialAmount;
    }

    public int getRestAmount() {
        return restAmount;
    }

    public void setRestAmount(int restAmount) {
        this.restAmount = restAmount;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public InvoiceState getState() {
        return state;
    }

    public void setState(InvoiceState state) {
        this.state = state;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
