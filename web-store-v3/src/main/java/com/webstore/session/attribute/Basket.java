package com.webstore.session.attribute;

import com.webstore.domain.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by DVaschenko on 20.07.2016.
 */
@Component
public class Basket {
    List<Product> productList = new ArrayList<>();

    public List<Product> getProductList() {
        return productList;
    }

    public void addProduct(Product product) {
        if (!existsProduct(product)) {
            //очистим от комментариев, на всякий случай
//            product.clearComment();

            productList.add(product);
        }
    }

    public Double getTotalSum() {
        double totalSum = 0;
        for (Product product : getProductList()) {
            totalSum += product.getPrice() * product.getCnt();
        }
        return totalSum;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    private boolean existsProduct(Product product) {
        for (Product item : productList) {
            if (item.getCode().equals(product.getCode()))
                return true;
        }
        return false;
    }

    public int count() {
        return productList.size();
    }

    public void deleteByCode(String code) {
        Iterator<Product> iter = productList.iterator();

        while (iter.hasNext()) {
            if (iter.next().getCode().equals(code)) {
                iter.remove();
            }
        }
    }
}