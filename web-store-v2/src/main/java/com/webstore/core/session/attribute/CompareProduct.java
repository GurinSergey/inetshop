package com.webstore.core.session.attribute;

import com.webstore.core.entities.Product;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by SGurin on 03.11.2016.
 */
@Component
public class CompareProduct {
    Map<Integer, List<Product>> productMap = new HashMap<>();

    public Map<Integer, List<Product>> getProductMap() {
        return productMap;
    }

    public void mergeProduct(final Product product) {
        if (!existsProduct(product)) {
            List<Product> products = productMap.get(product.getTemplate().getId());
            if (products == null) products = new ArrayList<>();
            product.clearComment();
            products.add(product);
            productMap.put(product.getTemplate().getId(), products);
        } else {
            deleteByValue(product);
        }
    }

    public boolean existsProduct(Product product) {
        for (List<Product> products : productMap.values()) {
            for (Product pr : products) {
                if (pr.equals(product))
                    return true;
            }
        }
        return false;
    }

    public void deleteByValue(Product product) {
        Iterator<List<Product>> it = productMap.values().iterator();

        while (it.hasNext()) {
            List<Product> listProducts = it.next();

            if (listProducts.contains(product)) {
                if (listProducts.size() == 1) it.remove();
                else listProducts.remove(product);
            }
        }
    }

    public int count() {
        int total = 0;
        for (List<Product> list : productMap.values()) {
            total += list.size();
        }
        return total;
    }
}
