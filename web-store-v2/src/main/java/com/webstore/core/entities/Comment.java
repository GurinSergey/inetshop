package com.webstore.core.entities;

import com.fasterxml.jackson.annotation.*;
import com.webstore.core.base.jsonView.View;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.webstore.core.util.Const.DATE_FORMAT_NORMAL;

/**
 * Created by DVaschenko on 08.08.2016.
 */
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonView(value = View.ForCommentView.class)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Comment parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy
    @JsonView(value = View.ForCommentView.class)
    private List<Comment> children = new ArrayList<>();

    @Column(name = "text", nullable = false, length = 65535, columnDefinition = "TEXT")
    @JsonView(value = View.ForCommentView.class)
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT_NORMAL)
    @JsonView(value = View.ForCommentView.class)
    private Date date = new Date();

    private int plus = 0;
    private int minus = 0;

    @JsonView(value = View.ForCommentView.class)
    private String author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonView(value = View.ForCommentView.class)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    public Comment() {
    }

    public Comment(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public List<Comment> getChildren() {
        return children;
    }

    public void setChildren(List<Comment> children) {
        this.children = children;
    }

    public String getText() {
        return text;
    }

    public void setText(String commentText) {
        this.text = commentText;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date creationDate) {
        date = creationDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public int getPlus() {
        return plus;
    }

    public void setPlus(int plus) {
        this.plus = plus;
    }

    public int getMinus() {
        return minus;
    }

    public void setMinus(int minus) {
        this.minus = minus;
    }
}
