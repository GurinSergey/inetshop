package com.webstore.domain;

import com.fasterxml.jackson.annotation.*;
import com.webstore.domain.enums.*;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.*;

/**
 * Created by Andoliny on 28.10.2016.
 *
 */
@Entity
@Table(schema = "etc", name = "order_head")
public class Order {

    //ИД заказа, уникальный
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client")
    private User client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "oper_id")
    private User oper;

    @Column(name = "customer_name")
    private String customerName;

    //LAO how to best use enum in JPA Hybernate link in comment  UserState
    @Enumerated
    @Column(name = "state", columnDefinition = "int")
    private OrderState state = OrderState.NEW;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false)
    private Date orderDate = new Date();

    //  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm" ,timezone = "Europe/Kiev")
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "delivery_date", nullable = true)
    private Date deliveryDate;

    @Column(name = "sum")
    private double sum;

    //LAO how to best use enum in JPA Hybernate link in comment  UserState
    @Enumerated
    @Column(name = "delivery_kind", columnDefinition = "int")
    private DeliveryKind deliveryKind;

    //LAO how to best use enum in JPA Hybernate link in comment  UserState
    @Enumerated
    @Column(name = "payment_method", columnDefinition = "int")
    private PaymentMethod paymentMethod;

    @Column(name = "order_note")
    private String note;

    @Column(name = "phone")
    private String phone;

    @Column(name = "call_back")
    private String callBack;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Enumerated
    @Column(name = "period_time", columnDefinition = "int")
    private PeriodTime periodTime;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<OrderDetails> orderDetails = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderHistoryStateList> orderHistoryStateLists = new ArrayList<>();

    public Order(Sort.Direction asc, String price) {
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public User getOper() {
        return oper;
    }

    public void setOper(User oper) {
        this.oper = oper;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getPhone() {
        return phone;
    }

    public DeliveryKind getDeliveryKind() {
        return deliveryKind;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDeliveryKind(DeliveryKind deliveryKind) {
        this.deliveryKind = deliveryKind;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Set<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails.add(orderDetails);
    }

    public String getCallBack() {
        return callBack;
    }

    public void setCallBack(String callBack) {
        this.callBack = callBack;
    }

    public PeriodTime getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(PeriodTime periodTime) {
       // this.periodTime = periodTime;
        setPeriodTimeOnly(periodTime);

        Calendar cal = Calendar.getInstance();
        cal.setTime(this.deliveryDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.HOUR,periodTime.getHours());

        this.deliveryDate = cal.getTime();
    }


    public void setPeriodTimeOnly(PeriodTime periodTime){
        this.periodTime = periodTime;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;


    }

    public List<OrderHistoryStateList> getOrderHistoryStateLists() {
        return orderHistoryStateLists;
    }

    public void setOrderHistoryStateLists(OrderHistoryStateList orderHistoryStateLists) {
        this.orderHistoryStateLists.add(orderHistoryStateLists);
    }

    public Order() {
    }

    public Order(User client, String customerName, Date deliveryDate, String phone) {
        this.client = client;
        this.customerName = customerName;
        this.deliveryDate = deliveryDate;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", client=" + client +
                ", customerName='" + customerName + '\'' +
                ", state=" + state +
                ", date=" + orderDate +
                ", delivery_date=" + deliveryDate +
                ", sum=" + sum +
                ", phone='" + phone + '\'' +
                ", orderDetails=" + orderDetails +
                '}';
    }
}
