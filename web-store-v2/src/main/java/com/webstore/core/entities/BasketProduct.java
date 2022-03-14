package com.webstore.core.entities;

public class BasketProduct {
    private int id;
    private String title;
    private String code;
    private double price;
    private String description;
    private int cnt;

    public BasketProduct(int id, String title, String code, double price, String description, int cnt) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.price = price;
        this.description = description;
        this.cnt = cnt;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getCnt() {
        return cnt;
    }
}
