package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(schema = "etc", name = "product_statistics")
public class ProductStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private long id;

    @Column(name = "total_comments")
    private long totalComments = 0;

    @Column(name = "total_rating", precision = 3, scale = 2)
    private float totalRating = 0;

    @OneToOne(mappedBy = "statistics", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ProductRating comments_statistics = new ProductRating(this);

    @OneToMany(mappedBy = "statistics", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapKeyColumn(name = "star")
    private Map<Integer, ProductStarRating> starStatistics = new HashMap<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @Column(name = "total_purchases")
    private long totalPurchases = 0;

    public ProductStatistics() {}

    public ProductStatistics(Product product) {
        this.product = product;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(long totalComments) {
        this.totalComments = totalComments;
    }

    public float getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(float totalRating) {
        this.totalRating = totalRating;
    }

    public ProductRating getComments_statistics() {
        if(comments_statistics == null)
            setComments_statistics(new ProductRating(this));
        return comments_statistics;
    }

    public void setComments_statistics(ProductRating comments_statistics) {
        this.comments_statistics = comments_statistics;
    }

    public Map<Integer, ProductStarRating> getStarStatistics() {
        return starStatistics;
    }

    public void setStarStatistics(Map<Integer, ProductStarRating> starStatistics) {
        this.starStatistics = starStatistics;
    }

    public void incrementTotalComment(){
        this.totalComments++;
    }

    private void updateStarsCnt(int star){
        Map<Integer, ProductStarRating> ratingStar = getStarStatistics();
        ProductStarRating productStarRating = new ProductStarRating();
        if(ratingStar.containsKey(star))
            productStarRating = ratingStar.get(star);
        productStarRating.addCnt();
        productStarRating.setStatistics(this);
        ratingStar.put(star, productStarRating);
    }

    public void calculateTotalRating(int val){
        updateStarsCnt(val);

        int rating = 0;
        int totalCnt = 0;
        for (Map.Entry<Integer, ProductStarRating> entry : this.starStatistics.entrySet()) {
            rating += entry.getKey() * entry.getValue().getCnt();
            totalCnt += entry.getValue().getCnt();
        }
        setTotalRating(rating / totalCnt);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getTotalPurchases() {
        return totalPurchases;
    }

    public void setTotalPurchases(long totalPurchases) {
        this.totalPurchases = totalPurchases;
    }
}
