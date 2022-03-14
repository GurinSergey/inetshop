package com.webstore.core.session.attribute;

import com.webstore.core.entities.BasketProduct;
import com.webstore.core.entities.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by DVaschenko on 20.07.2016.
 */
@Component
public class Basket {
    private List<BasketProduct> basketProductList = new ArrayList<>();

    public List<BasketProduct> getProductList() {
        return basketProductList;
    }

    public void addProduct(Product product) {
        if(!existsProduct(product)){
            BasketProduct basketProduct = new BasketProduct(product.getId(), product.getTitle(), product.getCode(), product.getPrice(), product.getDescription(), 1);

            basketProductList.add(basketProduct);
        }
    }

    public void setProductList(List<BasketProduct> productList) {
        this.basketProductList = productList;
    }

    private boolean existsProduct(Product product) {
        for (BasketProduct item : basketProductList) {
            if(item.getCode().equals(product.getCode()))
                return true;
        }
        return false;
    }

    public int count(){
        return basketProductList.size();
    }

    public void deleteByCode(String code) {
        Iterator<BasketProduct> iter = basketProductList.iterator();

        while (iter.hasNext()){
            if(iter.next().getCode().equals(code)){
                iter.remove();
            }
        }
    }
}