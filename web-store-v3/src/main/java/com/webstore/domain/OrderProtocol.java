package com.webstore.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webstore.util.Utils;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(schema = "etc", name = "order_protocol")
public class OrderProtocol {

    @Id
    @Column(name = "order_id")
    private Long orderId;

    @Lob
    @Column(name = "log_message", columnDefinition = "CLOB")
    private byte[] logMessage;

    @Transient
    private StringBuilder tempMessage;

    @Transient
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");

    public OrderProtocol() {
    }

    public OrderProtocol(Long orderId) {
        this.orderId = orderId;
        tempMessage = new StringBuilder("").append("\n");

    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getLogMessage() {
        return new String(this.logMessage, StandardCharsets.UTF_8);
    }

    @JsonIgnore
    public byte[] getLogMessageByte() {
        return logMessage;
    }

    public void setLogMessage(byte[] logMessage) {
        this.logMessage = logMessage;
    }

    public OrderProtocol addLineProtocol(String line) {
        this.tempMessage.append(format.format(new Date()));
        this.tempMessage.append("  :").append(line).append('\n');
        return this;

    }

    public void confirm() {
        this.logMessage = this.tempMessage.toString().getBytes(StandardCharsets.UTF_8);
    }

}
