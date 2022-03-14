package com.webstore.core.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * Created by DVaschenko on 20.07.2016.
 */
public interface ProductService extends Crud<Product> {
    Product findByCode(String code);
    List<Product> getProducts();
    List<Product> getAllProducts();
    void deletePhotoById(Product product, int photoId);
    void deleteApplicabilityById(Product product, int appId);
    void mergeFields(Product product, JsonNode fields);
    void mergeApplicability(Product product, JsonNode applicabilities);
    void mergeFiles(Product product,  MultipartFile[] files, String format) throws IOException;
    void sortOwnerFields(Product product);
    public List<Product> productSorted(List<Product> list, Comparator<Product>... characteristics);
    public String mergeChosenProducts(int userID, int productId);
    public List<Product> getChosenProductsByUserId(int userId);
    public boolean isChosenProduct(int userID, int productId);

    public void setTickIsChosen(int userId, Product products);
    public void setTickIsChosen(int userId, List<Product> products);
}