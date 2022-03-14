package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(schema = "etc", name = "order_history_state")
public class OrderHistoryState implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "state_id")
    private long stateId;

    @Column(name = "state_name")
    private String stateName;

    @Column(name = "state_note")
    private String stateNote;

    @Column(name = "state_code")
    private String stateCode;

    @JsonIgnore
    @OneToMany(mappedBy = "orderHistoryState", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderHistoryStateList> orderHistoryStateList;


    public OrderHistoryState() {
    }

    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateNote() {
        return stateNote;
    }

    public void setStateNote(String stateNote) {
        this.stateNote = stateNote;
    }


    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
}
