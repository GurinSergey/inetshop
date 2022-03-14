package com.webstore.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.webstore.base.TemplateInfo;
import com.webstore.repository.ChosenProductRepo;
import com.webstore.repository.ProductPhotoRepo;
import com.webstore.repository.ProductRatingRepo;
import com.webstore.repository.ProductRepo;
import com.webstore.util.Utils;
import com.webstore.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.webstore.util.Utils.checkExistsCommonCatalog;
import static com.webstore.util.Utils.getUUID;

@Service
public class ProductService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ProductRatingRepo ratingRepo;
    @Autowired
    private Environment environment;
    @Autowired
    private ChosenProductRepo chosenProductRepo;
    @Autowired
    private ProductPhotoRepo productPhotoRepo;

    private boolean isEmpryStr(String filterStr){
        return(filterStr == null || filterStr.equals(""));
    }

    private String getQueryStr(String filterStr){
        String headSqlStr = "select p.* from product p\n" +
                "  left join product_statistics ps\n" +
                "    on p.id = ps.product_id\n" +
                "where p.template_id = :templateId";
        if(isEmpryStr(filterStr))
            return headSqlStr;

        return headSqlStr.concat(Arrays.stream(filterStr.split(";"))
                .map(s -> " and exists(select 1 from product_owner_fields f where f.product_id = p.id"
                        .concat(" and f.template_field_id = ".concat(s.split("=")[0]))
                        .concat(" and f.fields_value_id in (".concat(s.split("=")[1])
                                .concat("))"))
                )
                .collect(Collectors.joining()));
    }

    private String getSortStr(String sortDirection, String sortProperty){
        return " order by ".concat(sortProperty).concat(" ").concat(sortDirection);
    }

    public List<Product> findAllByFilterParams(int id, int offset, int limit, String sortDirection, String sortProperty, String filter){
        String queryStr = getQueryStr(filter);
        queryStr += getSortStr(sortDirection, sortProperty);

        Query query = entityManager.createNativeQuery(queryStr, Product.class);
        query.setParameter("templateId", id);
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    public Product add(Product product) {
        return productRepo.save(product);
    }

    public Product find(Long id) {
        return productRepo.findOne(id);
    }

    public Product find(String ident) {
        return productRepo.findByLatIdent(ident);
    }

    public void delete(Product item) {
        productRepo.delete(item);
    }

    public void delete(Long id) {
        productRepo.delete(id);
    }

    public Product update(Product item) {
        return productRepo.save(item);
    }

    public Product findByCode(String code) {
        return productRepo.findByCode(code);
    }

    public List<ProductPhoto> getProductPhotos(long productId) {
        return productPhotoRepo.findAllByProductId(productId);
    }

    public List<Product> getProducts() {
        return productRepo.findAll();
    }

    public List<Product> getAll() {
        return productRepo.findAll();
    }

    public Sort getSort(String sort_dir, String sort_prop) {
        if (sort_dir.equals("ASC")) {
            return new Sort(Sort.Direction.ASC, sort_prop);
        }

        return new Sort(Sort.Direction.DESC, sort_prop);
    }

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

            app.setMark(new Mark(applicability.get("mark").asLong()));
            app.setModel(new Model(applicability.get("model").asLong()));
            app.setModification(new Modification(applicability.get("modification").asInt()));
            app.setGeneral(applicability.get("general").asBoolean());

            product.setApplicability(app);
        }
    }

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

    public List<Product> productSorted(List<Product> list, Comparator<Product>... characteristics) {
        for (int i = characteristics.length - 1; i >= 0; i--) {
            Collections.sort(list, characteristics[i]);
        }
        return list;
    }

    public List<Product> getChosenProductsByUserId(Long userId) {
        List<ChosenProduct> chosenProducts = chosenProductRepo.findAllByUserId(userId);
        List<Long> productIdList = chosenProducts.stream().map(chosenProduct -> chosenProduct.getId()).collect(Collectors.toList());
        return productRepo.findAll(productIdList);
    }

    public String mergeChosenProducts(Long userID, Long productId) {
        ChosenProduct product = chosenProductRepo.findByUserIdAndProductId(userID, productId);
        if (product != null) {
            chosenProductRepo.delete(product);
            return "delete";
        }
        chosenProductRepo.save(new ChosenProduct(userID, productId));
        return "add";
    }

    public boolean isChosenProduct(Long userID, Long productId) {
        return chosenProductRepo.findByUserIdAndProductId(userID, productId) != null ? true : false;
    }

    public void setTickIsChosen(Long userId, Product product) {
        if (isChosenProduct(userId, product.getId())) product.setChosen(true);
    }

    public void setTickIsChosen(Long userId, List<Product> products) {
        for (Product product : products) {
            setTickIsChosen(userId, product);
        }
    }

    public List<Product> getCountPackageByTemplateId(int id){
        return productRepo.findAllCountByTemplateId(id);
    }

    public List<Product> getPackageProductsByTemplateId(int id, int offset, int limit, Sort sort){
        return productRepo.findAllByTemplateId(id, new PageRequest(offset, limit, sort));
    }

    private ExampleMatcher getExampleMatcher() {
        return ExampleMatcher.matchingAny()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("isExists", "price", "statistics");
    }

    public int getCountProductTemplatesByFilterCond(String filter_str) {
        return productRepo.findAllCountProductTemplatesByFilterCond(filter_str).size();
    }

    public List<TemplateInfo> getAllProductTemplatesByFilterCond(int offset_template, int limit_template, String filter_str) {
        return productRepo.findAllProductTemplatesByFilterCondWithPagination(filter_str, new PageRequest(offset_template, limit_template, Sort.Direction.ASC, "template.id"));
    }

    public List<Product> getAllProductByTemplateIdAndFilterCond(int limit_products, int templateId, String filter_string) {
        return productRepo.findAllProductByTemplateIdAndFilterCondWithPagination(templateId, filter_string, new PageRequest(0, limit_products, Sort.Direction.DESC, "id"));
    }

    public List<Product> getPackageProducts(int offset, int limit, Sort sort, String filter_str) {
        ExampleMatcher matcher = getExampleMatcher();

        Product product = new Product();
        product.setCode(filter_str);
        product.setTitle(filter_str);
        product.setDescription(filter_str);

        Example<Product> example = Example.of(product, matcher);

        Page<Product> products = productRepo.findAll(example, new PageRequest(offset, limit, sort));

        return products.getContent();
    }

    public List<Product> getPackageProducts(Long[] productsId) {
        return productRepo.getPackageProducts(productsId);
    }

    public List<Product> getByMatchers(Product product) {
        SortOrder sortOrder = SortOrder.ASCENDING;
        String sortField = "id";
        ExampleMatcher matcher = getExampleMatcher();

        Example<Product> example = Example.of(product, matcher);

        Page<Product> products = productRepo.findAll(example,
                new PageRequest(0, 50, sortOrder == SortOrder.ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));

        return products.getContent();
    }

    public List<Product> getByMatchers(String match) {
        ExampleMatcher matcher = getExampleMatcher();

        Product product = new Product();
        product.setCode(match);
        product.setTitle(match);

        Example<Product> example = Example.of(product, matcher);

        List<Product> products = productRepo.findAll(example);

        return products;
    }
}