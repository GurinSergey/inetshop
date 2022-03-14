package com.webstore.core.base;

import com.webstore.core.session.attribute.Basket;

/**
 * Created by ALisimenko on 27.09.2016.
 */
public class ComplexBasketStorage {
    public Basket basket;
    public String note;
    public int count;

    public ComplexBasketStorage() {
    }

    public ComplexBasketStorage(Basket basket, String note) {
        this.setBasket(basket);
        this.note = note;
    }

    public Basket getBasket() {
        return basket;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
        this.count = basket.count();
    }
}
