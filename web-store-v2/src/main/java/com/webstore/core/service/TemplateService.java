package com.webstore.core.service;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Template;
import com.webstore.core.entities.TemplateFields;

import java.util.List;
import java.util.Set;

/**
 * Created by MZlenko on 23.08.2016.
 */
public interface TemplateService  extends Crud<Template>{
    List<Template> getAllTemplates();
    Set<TemplateFields> getFieldsByTemplateId(int id);
    void deleteField(Template template, int field_id);
    void deleteFieldValue(Template template, int field_value_id);
    List<Template> findListTemplatesByCatalogId(int catalog_id);
}
