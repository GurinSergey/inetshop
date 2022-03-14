package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.webstore.domain.enums.PeriodType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "scheduler")
public class Scheduler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheduler_id")
    private long schedulerId;
    @Column(name = "scheduler_name")
    private String schedulerName;

    private String description;
    private String object;
    private String method;
    @Column(name = "scheduler_type")
    private long schedulerType;
    @Column(name = "period")
    private int period;

    @Column(name = "type_period")
    private PeriodType typePeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss")
    @Column(name = "work_start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date workStartTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss")
    @Column(name = "work_end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date workEndTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss")
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss")
    @Column(name = "next_stamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date nextStamp;

    @Column(name = "service_kind")
    private long serviceKind;

    @Column(name = "param")
    private String param;

    public Scheduler(String schedulerName, String description, String object, String method, long schedulerType, long serviceKind) {
        this.schedulerName = schedulerName;
        this.description = description;
        this.object = object;
        this.method = method;
        this.schedulerType = schedulerType;
        this.serviceKind = serviceKind;

    }

    public Scheduler() {
    }

    public long getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(long schedulerId) {
        this.schedulerId = schedulerId;
    }

    public String getSchedulerName() {
        return schedulerName;
    }

    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public long getSchedulerType() {
        return schedulerType;
    }

    public void setSchedulerType(long schedulerType) {
        this.schedulerType = schedulerType;
    }

    public Date getWorkStartTime() {
        return workStartTime;
    }

    public void setWorkStartTime(Date workStartTime) {
        this.workStartTime = workStartTime;
    }

    public Date getWorkEndTime() {
        return workEndTime;
    }

    public void setWorkEndTime(Date workEndTime) {
        this.workEndTime = workEndTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getNextStamp() {
        return nextStamp;
    }

    public void setNextStamp(Date nextStamp) {
        this.nextStamp = nextStamp;
    }

    public long getServiceKind() {
        return serviceKind;
    }

    public void setServiceKind(long serviceKind) {
        this.serviceKind = serviceKind;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getTypePeriod() {
        return typePeriod.getValue();
    }

    public void setTypePeriod(PeriodType typePeriod) {
        this.typePeriod = typePeriod;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }


    public String[] getParamsArrayString(){
        if (this.param == null) {
            return null;
        }
        return  this.param.split(",");
    }
}
