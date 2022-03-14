package com.webstore.core.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.webstore.core.dao.ProductDao;
import com.webstore.core.entities.*;
import com.webstore.core.service.ProductService;
import com.webstore.core.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.webstore.core.util.Utils.*;

/**
 * Created by DVaschenko on 20.07.2016.
 */
@Service
public class IProductService implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private Environment environment;

    @Override
    public Product add(Product product) {
        return productDao.add(product);
    }

    @Override
    public Product find(Product item) {
        return productDao.find(item);
    }

    @Override
    public Product find(int id) {
        return productDao.find(id);
    }

    @Override
    public boolean delete(Product item) {
        return productDao.delete(item);
    }

    @Override
    public boolean delete(int id) {
        return productDao.delete(id);
    }

    @Override
    public Product update(Product item) {
        return productDao.update(item);
    }

    @Override
    public Product findByCode(String code) {
        return productDao.findByCode(code);
    }

    @Override
    public List<Product> getProducts() {
        return productDao.getProducts();
    }

    @Override
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    @Override
    public void deletePhotoById(Product product, int photoId) {
        if (product != null) {
            for (ProductPhoto photo : product.getPhotos())
                if (photo.getId() == photoId) {
                    product.getPhotos().remove(photo);
                    break;
                }
            update(product);
        }
    }

    @Override
    public void deleteApplicabilityById(Product product, int appId) {
        if (product != null) {
            for (Applicability app : product.getApplicabilities()) {
                if (app.getId() == appId) {
                    product.getApplicabilities().remove(app);
                    break;
                }
            }
            update(product);
        }
    }

    @Override
    public void mergeFields(Product product, JsonNode fields) {
        if (fields.isNull() || fields.size() == 0)
            throw new NullPointerException("Не заполнены поля шаблона");

        for (final JsonNode field : fields) {
            ProductOwnerFields ownerField = new ProductOwnerFields();
            if (field.path("ownerId").asInt() != 0)
                ownerField.setId(field.path("ownerId").asInt());

            if (field.path("id").asInt() == -1) {
                ownerField.setCustomValue(field.path("value").asText());
            } else {
                ownerField.setFieldsValue(new TemplateFieldsValue(field.path("id").asInt()));
            }

            ownerField.setTemplateFields(new TemplateFields(field.path("templateFieldId").asInt()));
            product.setField(ownerField);
        }
    }

    @Override
    public void mergeApplicability(Product product, JsonNode applicabilities) {
        if (applicabilities.isNull() || applicabilities.size() == 0)
            throw new NullPointerException("Не заполнены поля совместимости");

        for (final JsonNode applicability : applicabilities) {
            if (applicability.get("id").asInt() < 0) {
                deleteApplicabilityById(product, Math.abs(applicability.get("id").asInt()));
                continue;
            }

            Applicability app = new Applicability();
            if (applicability.get("id").asInt() > 0)
                app.setId(applicability.get("id").asInt());

            app.setMark(new Mark(applicability.get("mark").asInt()));
            app.setModel(new Model(applicability.get("model").asInt()));
            app.setModification(new Modification(applicability.get("modification").asInt()));
            app.setGeneral(applicability.get("general").asBoolean());

            product.setApplicability(app);
        }
    }

    @Override
    public void mergeFiles(Product product, MultipartFile[] files, String format) throws IOException {
        if (files.length > 0) {
            String pathLocalCatalog = environment.getProperty("resources.images.dir");
            checkExistsCommonCatalog(pathLocalCatalog);

            for (int i = 0; i < files.length; i++) {
                String photoName = getUUID();
                String code = product.getCode();
                if (Utils.saveImageJpg(pathLocalCatalog + code, files[i], photoName)) {
                    product.setPhoto(new ProductPhoto(code, photoName, format));
                }
            }
        }
    }

    @Override
    public void sortOwnerFields(Product product) {
        if (product == null || product.getFields().size() == 0)
            throw new IllegalArgumentException("Данные некорректны");

        Collections.sort(product.getFields(), new Comparator<ProductOwnerFields>() {
            @Override
            public int compare(ProductOwnerFields o1, ProductOwnerFields o2) {
                return o1.getTemplateFields().getOrder() - o2.getTemplateFields().getOrder();
            }
        });
    }

    @Override
    public List<Product> productSorted(List<Product> list, Comparator<Product>... characteristics) {
        for (int i = characteristics.length - 1; i >= 0; i--) {
            Collections.sort(list, characteristics[i]);
        }
        return list;
    }

    @Override
    public List<Product> getChosenProductsByUserId(int userId) {
        return productDao.getChosenProductsByUserId(userId);
    }


    @Override
    public String mergeChosenProducts(int userID, int productId) {
        return productDao.mergeChosenProducts(userID, productId);
    }

    @Override
    public boolean isChosenProduct(int userID, int productId) {
        return productDao.isChosenProduct(userID, productId);
    }

    @Override
    public void setTickIsChosen(int userId, Product product) {
        if (isChosenProduct(userId, product.getId())) product.setChosen(true);
    }

    @Override
    public void setTickIsChosen(int userId, List<Product> products) {
        for (Product product : products) {
            setTickIsChosen(userId, product);
        }
    }

}