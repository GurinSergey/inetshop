package com.webstore.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.webstore.domain.AddressGeneral;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class NovaResponse implements Serializable {
    private Boolean success;
    private List<AddressGeneral> data = new ArrayList<AddressGeneral>();

    public NovaResponse() {
    }


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<AddressGeneral> getData() {
        return data;
    }

    public void setData(List<AddressGeneral> data) {
        this.data = data;
    }
}
