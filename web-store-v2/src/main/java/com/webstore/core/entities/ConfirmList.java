package com.webstore.core.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ALisimenko on 01.08.2016.
 */

@Entity
@Table(name = "confirmlist")
public class ConfirmList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "GUID")
    private String guid;

    @Column(name = "email")
    private String email;

    @Column(name = "datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetime = new Date();

    public ConfirmList() {
    }

    public ConfirmList(String guid, String email) {
        this.guid = guid;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
