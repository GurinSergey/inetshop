package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(schema = "etc", name = "scheduler_protocol")
public class SchedulerProtocol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "text", columnDefinition = "CLOB")
    private byte[] logMessage;

    @Transient
    private StringBuilder tempMessage;

    @Transient
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");


    public SchedulerProtocol() {
    }

    public SchedulerProtocol(String name) {
        this.name = name;
        this.tempMessage = new StringBuilder("").append("\n");

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StringBuilder getTempMessage() {
        return tempMessage;
    }

    public void setTempMessage(StringBuilder tempMessage) {
        this.tempMessage = tempMessage;
    }

    public SimpleDateFormat getFormat() {
        return format;
    }

    public void setFormat(SimpleDateFormat format) {
        this.format = format;
    }

    public String getLogMessage() {
        return new String(this.logMessage, StandardCharsets.UTF_8);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public byte[] getLogMessageByte() {
        return logMessage;
    }

    public void setLogMessage(byte[] logMessage) {
        this.logMessage = logMessage;
    }

    public SchedulerProtocol addLineProtocol(String line) {
        this.tempMessage.append(format.format(new Date()));
        this.tempMessage.append("  :").append(line).append('\n');
        return this;

    }

    public void confirm() {
        this.logMessage = this.tempMessage.toString().getBytes(StandardCharsets.UTF_8);
    }

}
