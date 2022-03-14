package com.webstore.core.dao;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Template;

import java.util.List;

/**
 * Created by MZlenko on 23.08.2016.
 */
public interface TemplateDao extends Crud<Template> {
    List<Template> getAllTemplates();
    void deleteField(Template template, int field_id);
    void deleteFieldValue(Template template, int field_value_id);
    List<Template> findListTemplatesByCatalogId(int catalog_id);
}
