package com.webstore.session.attribute;

import com.webstore.domain.Product;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ALisimenko on 02.03.2018.
 */
public class WatchHistory {
    Set<Product> productList = new HashSet<>();


    public WatchHistory() {
    }

    public WatchHistory(Set<Product> productList) {
        this.productList = productList;
    }

    public Set<Product> getProductList() {
        return productList;
    }

    public void addProduct(Product product) {
//            product.clearComment();
            productList.add(product);
    }

    public void setProductList(Set<Product> productList) {
        this.productList = productList;
    }



    public int count(){
        return productList.size();
    }


}
