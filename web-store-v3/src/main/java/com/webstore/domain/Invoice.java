package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.webstore.domain.enums.InvoiceState;
import com.webstore.domain.enums.InvoiceType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "etc", name = "invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

//    @Column(name = "version")
//    @Version int version;

    @Column(name = "num_doc")
    private String numDoc;

    //LAO how to best use enum in JPA Hybernate link in comment  UserState
    @Enumerated
    @Column(name = "invoice_type",columnDefinition = "smallint")
    private InvoiceType invoiceType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "invoice_date", nullable = false)
    private Date invoiceDate = new Date();


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contractor_id")
    @JsonManagedReference(value = "contractor_")
    private Contractor contractor;

    @Column(name = "sum")
    private double sum;

    @OneToMany(mappedBy = "invoice",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonBackReference(value = "invoicePart")
    private List<InvoicePart> invoicePart = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_id")
    @JsonManagedReference(value = "warehouse_")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "oper_id")
    private User oper;

    //LAO how to best use enum in JPA Hybernate link in comment  UserState
    @Enumerated
    @Column(name = "state", columnDefinition = "int")
    private InvoiceState state = InvoiceState.NEW;
         /*   invoice.setSum(order.getSum());
            invoice.setOrder(order);
            invoice.setWarehouse(warehouse);
            invoice.setOper(user);
            invoice.setState(InvoiceState.WAIT);
            invoice.setInvoiceType(InvoiceType.SALES);*/
    //инвойс по расходу в номере накладной будет содержать ид заказа
    //invoice.setNumDoc(order.getOrderId().toString());


    public Invoice(String numDoc, InvoiceType invoiceType, double sum, Order order, Warehouse warehouse, User oper, InvoiceState state) {
        this.numDoc = numDoc;
        this.invoiceType = invoiceType;
        this.sum = sum;
        this.order = order;
        this.warehouse = warehouse;
        this.oper = oper;
        this.state = state;
    }


    public Invoice(InvoiceType invoiceType,Order order, Warehouse warehouse, User oper, InvoiceState state) {
        this.numDoc = order.getOrderId().toString();
        this.invoiceType = invoiceType;
        this.sum = order.getSum();
        this.order = order;
        this.warehouse = warehouse;
        this.oper = oper;
        this.state = state;
    }

    public Invoice() {
    }


    public User getOper() {
        return oper;
    }

    public void setOper(User oper) {
        this.oper = oper;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }


    public List<InvoicePart> getInvoicePart() {
        return invoicePart;
    }

    public void setInvoicePart(InvoicePart invoicePart) {
        this.invoicePart.add(invoicePart);
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public InvoiceState getState() {
        return state;
    }

    public void setState(InvoiceState state) {
        this.state = state;
    }
}
