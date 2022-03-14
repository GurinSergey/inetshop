package com.webstore.core.entities.enums;

import com.webstore.core.entities.Product;

import java.util.Comparator;

/**
 * Created by SGurin on 19.10.2016.
 */
public enum SortProductCriteria implements Comparator<Product>{

    CHEAP("from cheap to expensive") {
        @Override
        public int compare(Product product, Product product2) {
            return (product.getPrice() < product2.getPrice() ? -1 : (product.getPrice() == product2.getPrice() ? 0 : 1));
        }
    },
    EXPENSIVE("from expensive to cheap") {
        @Override
        public int compare(Product product, Product product2) {
            return (product2.getPrice() < product.getPrice() ? -1 : (product2.getPrice() == product.getPrice() ? 0 : 1));
        }
    },

    NEW("novelty") {
        @Override
        public int compare(Product product, Product product2) {
            return (product2.getId() < product.getId() ? -1 : (product2.getId() == product.getId() ? 0 : 1));
        }
    },

    RATING("by rating") {
        @Override
        public int compare(Product product, Product product2) {
            return 0;
        }
    };

    public String getDescription() {
        return description;
    }

    SortProductCriteria(String description) {
        this.description = description;
    }

    String description;
}
