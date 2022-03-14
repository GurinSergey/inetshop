package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "etc", name = "contractor")
public class Contractor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "main_phone")
    private String mainPhone;
    @Column(name = "secondary_phone")
    private String secondaryPhone;
    @Column(name = "main_email")
    private String mainEmail;
    @Column(name = "secondary_email")
    private String secondaryEmail;
    @Column(name = "note")
    private String note;
    @Column(name = "edrpou_inn")
    private String edrpouInn;
    @Column(name = "mfo")
    private String mfo;
    @Column(name = "account")
    private String account;

    @OneToMany(mappedBy = "contractor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference(value = "contractor_")
    private List<Invoice> invoiceList = new ArrayList<Invoice>();


    public Contractor() {
    }

    public Contractor(String name, String address, String mainPhone, String secondaryPhone, String mainEmail, String secondaryEmail, String note) {
        this.name = name;
        this.address = address;
        this.mainPhone = mainPhone;
        this.secondaryPhone = secondaryPhone;
        this.mainEmail = mainEmail;
        this.secondaryEmail = secondaryEmail;
        this.note = note;
    }

    public String getEdrpouInn() {
        return edrpouInn;
    }

    public void setEdrpouInn(String edrpouInn) {
        this.edrpouInn = edrpouInn;
    }

    public String getMfo() {
        return mfo;
    }

    public void setMfo(String mfo) {
        this.mfo = mfo;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMainPhone() {
        return mainPhone;
    }

    public void setMainPhone(String mainPhone) {
        this.mainPhone = mainPhone;
    }

    public String getSecondaryPhone() {
        return secondaryPhone;
    }

    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

    public String getMainEmail() {
        return mainEmail;
    }

    public void setMainEmail(String mainEmail) {
        this.mainEmail = mainEmail;
    }

    public String getSecondaryEmail() {
        return secondaryEmail;
    }

    public void setSecondaryEmail(String secondaryEmail) {
        this.secondaryEmail = secondaryEmail;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Invoice> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(Invoice invoiceList) {
        this.invoiceList.add(invoiceList);
    }
}
