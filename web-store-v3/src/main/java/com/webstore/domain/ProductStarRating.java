package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(schema = "etc", name = "product_rating_star")
public class ProductStarRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    @JsonIgnore
    private long id;

    @Column(name = "cnt")
    private int cnt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_stat_id")
    @JsonIgnore
    private ProductStatistics statistics;

    public ProductStarRating() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public void addCnt() {
        this.cnt++;
    }

    public ProductStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(ProductStatistics statistics) {
        this.statistics = statistics;
    }
}
