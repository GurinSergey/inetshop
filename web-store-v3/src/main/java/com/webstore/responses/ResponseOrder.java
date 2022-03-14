package com.webstore.responses;

import com.webstore.domain.enums.OrderState;

import java.io.Serializable;
import java.util.List;

public class ResponseOrder implements Serializable {
    private Long orderId;
    private OrderState state;
    private String operPersonName;
    private List<Response> responseOrderDetailsList;

    public ResponseOrder() {
    }

    public ResponseOrder(Long orderId, OrderState state, String operPersonName, List<Response> responseOrderDetailsList) {
        this.orderId = orderId;
        this.state = state;
        this.operPersonName = operPersonName;
        this.responseOrderDetailsList = responseOrderDetailsList;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public String getOperPersonName() {
        return operPersonName;
    }

    public void setOperPersonName(String operPersonName) {
        this.operPersonName = operPersonName;
    }

    public List<Response> getResponseOrderDetailsList() {
        return responseOrderDetailsList;
    }

    public void setResponseOrderDetailsList(List<Response> responseOrderDetailsList) {
        this.responseOrderDetailsList = responseOrderDetailsList;
    }
}
