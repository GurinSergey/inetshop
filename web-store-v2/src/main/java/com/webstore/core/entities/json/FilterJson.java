package com.webstore.core.entities.json;

/**
 * Created by SGurin on 01.08.2016.
 */
public class FilterJson {
    private int body_id;
    private int year_from;
    private int year_to;
    private int mark_id;
    private int model_id;
    private int modification_id;

    public int getBody_id() {
        return body_id;
    }

    public void setBody_id(int body_id) {
        this.body_id = body_id;
    }

    public int getYear_from() {
        return year_from;
    }

    public void setYear_from(int year_from) {
        this.year_from = year_from;
    }

    public int getYear_to() {
        return year_to;
    }

    public void setYear_to(int year_to) {
        this.year_to = year_to;
    }

    public int getMark_id() {
        return mark_id;
    }

    public void setMark_id(int mark_id) {
        this.mark_id = mark_id;
    }

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

    public int getModification_id() {
        return modification_id;
    }

    public void setModification_id(int modification_id) {
        this.modification_id = modification_id;
    }
}
