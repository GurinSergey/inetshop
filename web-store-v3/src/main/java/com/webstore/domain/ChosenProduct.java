package com.webstore.domain;

import javax.persistence.*;

@Entity
@Table(schema = "etc", name = "chosen_product")
public class ChosenProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "product_id")
    private Long productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public ChosenProduct(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }

    public ChosenProduct() {
    }
}
