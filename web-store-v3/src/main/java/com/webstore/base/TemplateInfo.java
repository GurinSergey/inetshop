package com.webstore.base;

public class TemplateInfo {
    private Integer templateId;
    private String templateName;

    public TemplateInfo(Integer templateId, String templateName) {
        this.templateId = templateId;
        this.templateName = templateName;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public String getTemplateName() {
        return templateName;
    }
}
