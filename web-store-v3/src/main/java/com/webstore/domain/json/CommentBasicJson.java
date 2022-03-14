package com.webstore.domain.json;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class CommentBasicJson {
    private Long commentId;
    private Boolean existsChild;
    private String text;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy hh:mm")
    private Date date = new Date();

    private long productId;
    private String latIdent;
    private String productPhotoUrl;
    private String productTitle;
    private String productCode;
    private Long userId;
    private String author;

    public CommentBasicJson() {
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Boolean getExistsChild() {
        return existsChild;
    }

    public void setExistsChild(Boolean existsChild) {
        this.existsChild = existsChild;
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

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getLatIdent() {
        return latIdent;
    }

    public void setLatIdent(String latIdent) {
        this.latIdent = latIdent;
    }

    public String getProductPhotoUrl() {
        return productPhotoUrl;
    }

    public void setProductPhotoUrl(String productPhotoUrl) {
        this.productPhotoUrl = productPhotoUrl;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
