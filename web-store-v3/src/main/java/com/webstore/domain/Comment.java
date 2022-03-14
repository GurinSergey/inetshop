package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.webstore.base.jsonView.View;
import com.webstore.util.Const;

import javax.persistence.*;
import java.util.*;

/**
 * Created by DVaschenko on 08.08.2016.
 */
@Entity
@Table(schema = "etc", name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text", nullable = false, length = 1000, columnDefinition = "TEXT")
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Const.DATE_FORMAT_NORMAL)
    private Date date = new Date();

    @Column(name = "parent_id")
    private long parentId;

    @OneToMany(mappedBy = "parentId", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Comment> children = new ArrayList<>();

    @Column(name = "product_id")
    private long productId;

    @Column(name = "note")
    private long note;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "star_rating")
    private long starRating;

    private String author;

    public Comment() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public List<Comment> getChildren() {
        return children;
    }

    public void setChildren(List<Comment> children) {
        this.children = children;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getNote() {
        return note;
    }

    public void setNote(long note) {
        this.note = note;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public long getStarRating() {
        return starRating;
    }

    public void setStarRating(long starRating) {
        this.starRating = starRating;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", parentId=" + parentId +
                ", children=" + children +
                ", productId=" + productId +
                ", author='" + author + '\'' +
                '}';
    }
}
