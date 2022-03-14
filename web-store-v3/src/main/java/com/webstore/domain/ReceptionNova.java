package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webstore.domain.enums.TypeReception;

import javax.persistence.*;

@Entity
@Table(name = "nova_poshta_schedule")
public class ReceptionNova {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "type")
    private TypeReception typeReception;
 /*   @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "adress_id")
    private AddressGeneral addressGeneral;*/
    @Column(name = "monday")
    private String monday;
    @Column(name = "tuesday")
    private String tuesday;
    @Column(name = "wednesday")
    private String wednesday;
    @Column(name = "thursday")
    private String thursday;
    @Column(name = "friday")
    private String friday;
    @Column(name = "saturday")
    private String saturday;
    @Column(name = "sunday")
    private String sunday;

    public ReceptionNova() {
    }

    public ReceptionNova(TypeReception typeReception, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday) {
        this.typeReception = typeReception;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

 /*   public AddressGeneral getAddressGeneral() {
        return addressGeneral;
    }

    public void setAddressGeneral(AddressGeneral addressGeneral) {
        this.addressGeneral = addressGeneral;
    }
*/
    public TypeReception getTypeReception() {
        return typeReception;
    }

    public void setTypeReception(TypeReception typeReception) {
        this.typeReception = typeReception;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public String getSaturday() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public String getSunday() {
        return sunday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }
}

