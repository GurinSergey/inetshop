package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Created by DVaschenko on 20.07.2016.
 */
@Entity
@Table(schema = "etc", name = "product")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    private String title;

    @Column(name = "is_exists")
    private boolean isExists = true;

    @Transient
    private boolean isChosen = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producer_id")
    private Producer producer;

    @Column(name = "code")
    private String code;

    @Column(name = "price")
    private double price;

    @Column(length = 65535, columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "template_id")
    private Template template;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user_fields")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ProductOwnerFields> fields = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user-product")
    @JsonIgnore
    @OrderBy("id ASC")
    private Set<ProductPhoto> photos = new HashSet<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<OrderDetails> orderDetails;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user-applicabilities")
    @JsonIgnore
    private Set<Applicability> applicabilities = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference(value = "product_prices")
    private List<ProductPrice> productPrices = new ArrayList<>();

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ProductStatistics statistics;

    @Column(name = "lat_ident")
    private String latIdent;

    @Transient
    private int cnt;

    public Product() {
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getLatIdent() {
        return latIdent;
    }

    public void setLatIdent(String latIdent) {
        this.latIdent = latIdent;
    }

    public Product(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFields(List<ProductOwnerFields> fields) {
        this.fields = fields;
    }

    public void setPhotos(Set<ProductPhoto> photos) {
        this.photos = photos;
    }

    public void setApplicabilities(Set<Applicability> applicabilities) {
        this.applicabilities = applicabilities;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<ProductOwnerFields> getFields() {
        return fields;
    }

    public void setField(ProductOwnerFields field) {
        this.fields.add(field);
        field.setProduct(this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ProductPhoto> getPhotos() {
        return photos;
    }

    public void setPhoto(ProductPhoto photo) {
        this.photos.add(photo);
        photo.setProduct(this);
    }

    public Set<Applicability> getApplicabilities() {
        return applicabilities;
    }

    public void setApplicability(Applicability applicability) {
        this.applicabilities.add(applicability);
        applicability.setProduct(this);
    }

    public void setOrders(OrderDetails orderDetails) {
        this.orderDetails.add(orderDetails);
    }

    public Set<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public boolean isExists() {
        return isExists;
    }

    public void setExists(boolean isExists) {
        this.isExists = isExists;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean isChosen) {
        this.isChosen = isChosen;
    }

    public void setOrderDetails(Set<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public ProductStatistics getStatistics() {
        if(statistics == null)
            statistics = new ProductStatistics(this);
        return statistics;
    }

    public void setStatistics(ProductStatistics statistics) {
        this.statistics = statistics;
    }

    public List<ProductPrice> getProductPrices() {
        return productPrices;
    }

    public void setProductPrices(ProductPrice productPrices) {

        this.productPrices.add(productPrices);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != 0 ? !(id == product.getId()) : product.getId() != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (producer != null ? producer.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
     //LAO хеши из-за этого поля не совпадают   result = 31 * result + (fields != null ? fields.hashCode() : 0);
        return result;
    }
}