package com.webstore.base;

import com.webstore.session.attribute.CompareProduct;

/**
 * Created by SGurin on 03.11.2016.
 */
public class ComplexCompareStorage {
    public CompareProduct compareProduct;
    public String note;
    public int count;

    public ComplexCompareStorage() {
    }

    public ComplexCompareStorage(CompareProduct compareProduct, String note) {
        this.compareProduct = compareProduct;
        this.note = note;
        this.count = compareProduct.count();
    }

    public CompareProduct getCompareProduct() {
        return compareProduct;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCompareProduct(CompareProduct compareProduct) {
        this.compareProduct = compareProduct;
    }
}
