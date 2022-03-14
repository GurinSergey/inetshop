package com.webstore.base;

import com.webstore.domain.FavoriteProduct;

import java.util.List;

public class FavoriteStorage {
    List<FavoriteProduct> products;

    public FavoriteStorage(List<FavoriteProduct> products) {
        this.products = products;
    }
}
