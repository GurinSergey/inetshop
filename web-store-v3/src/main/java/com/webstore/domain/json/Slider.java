package com.webstore.domain.json;

public class Slider {
    private String latIdent;
    private String title;
    private String description;
    private String code;
    private String url_image;

    public Slider(String latIdent, String title, String description, String code, String url_image) {
        this.latIdent = latIdent;
        this.title = title;
        this.description = description;
        this.code = code;
        this.url_image = url_image;
    }

    public String getLatIdent() {
        return latIdent;
    }

    public void setLatIdent(String latIdent) {
        this.latIdent = latIdent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }
}
