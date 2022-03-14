package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(schema = "etc", name = "product_rating")
public class ProductRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private long id;

    @Column(name = "positive_cnt")
    private int positiveCnt;

    @Column(name = "negative_cnt")
    private int negativeCnt;

    @Transient
    private int allCnt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_stat_id", nullable = false)
    @JsonIgnore
    private ProductStatistics statistics;

    public ProductRating() {
    }

    public ProductRating(ProductStatistics statistics) {
        this.statistics = statistics;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPositiveCnt() {
        return positiveCnt;
    }

    public void setPositiveCnt(int positiveCnt) {
        this.positiveCnt = positiveCnt;
    }

    public int getNegativeCnt() {
        return negativeCnt;
    }

    public void setNegativeCnt(int nagativeCnt) {
        this.negativeCnt = nagativeCnt;
    }

    public int getAllCnt() {
        return positiveCnt + negativeCnt;
    }

    public void setAllCnt(int allCnt) {
        this.allCnt = positiveCnt + negativeCnt;
    }

    public ProductStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(ProductStatistics statistics) {
        this.statistics = statistics;
    }
}
