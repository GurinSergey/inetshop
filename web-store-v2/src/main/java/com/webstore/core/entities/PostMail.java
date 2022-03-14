package com.webstore.core.entities;

import javax.persistence.*;

/**
 * Created by ALisimenko on 02.08.2016.
 */
@Entity
@Table(name = "postmail")
public class PostMail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "domen")
    private String domen;
    @Column(name = "postname")
    private String postName;
    @Column(name = "postlink")
    private String postLink;

    public PostMail() {
    }

    public PostMail(String domen, String postName, String postLink) {
        this.domen = domen;
        this.postName = postName;
        this.postLink = postLink;
    }

    public Long getId() {
        return id;
    }

    public String getDomen() {
        return domen;
    }

    public void setDomen(String domen) {
        this.domen = domen;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostLink() {
        return postLink;
    }

    public void setPostLink(String postLink) {
        this.postLink = postLink;
    }
}
