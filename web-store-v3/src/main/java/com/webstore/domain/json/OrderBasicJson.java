package com.webstore.domain.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrderBasicJson implements Serializable {
    private Long orderId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy hh:mm")
    private Date orderDate = new Date();

    private double sum;
    private String orderState;
    private List<String> photoUrls;
    private boolean flag = false;

    public OrderBasicJson() {
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
