package com.webstore.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by DVaschenko on 20.07.2016.
 */
@Entity
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;

    private boolean visible = true;
    private boolean isExists = true;

    @Transient
    private boolean isChosen = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producer_id")
    private Producer producer;

    private String code;
    private double price;

    @Column(length = 65535, columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id")
    private Template template;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference(value = "user-fields")
    private List<ProductOwnerFields> fields = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference(value = "user-product")
    private Set<ProductPhoto> photos = new HashSet<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<OrderDetails> orderDetails;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference(value = "user-applicabilities")
    private Set<Applicability> applicabilities = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    @Fetch(value = FetchMode.SELECT)
    private List<Comment> comments = new ArrayList<>();

    public Product() {
    }

    public Product(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComment(Comment comment) {
        comments.add(comment);
        comment.setProduct(this);
    }

    public void clearComment() {
        comments = new ArrayList<>();
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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