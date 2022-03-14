package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(schema = "etc", name = "product_watch_link")
public class WatchProduct implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long watchId;


    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName="id")
    private Product product;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "watch_date",insertable = false)
    private Date watchDate ;

    public Date getWatchDate() {
        return watchDate;
    }

    public WatchProduct(Product product, User user) {
        this.product = product;
        this.user = user;
    }

    public void setWatchDate(Date watchDate) {
        this.watchDate = watchDate;
    }

    public long getWatchId() {
        return watchId;
    }

    public void setWatchId(long watchid) {
        this.watchId = watchid;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
