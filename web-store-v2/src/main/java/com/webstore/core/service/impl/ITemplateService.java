package com.webstore.core.service.impl;

import com.webstore.core.dao.TemplateDao;
import com.webstore.core.entities.Template;
import com.webstore.core.entities.TemplateFields;
import com.webstore.core.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by MZlenko on 23.08.2016.
 */
@Service
public class ITemplateService implements TemplateService {
    @Autowired
    private TemplateDao templateDao;

    @Override
    public Template add(Template item) {
        return  templateDao.add(item);
    }

    @Override
    public Template find(Template item) {
        return templateDao.find(item);
    }

    @Override
    public Template find(int id) {
        return templateDao.find(id);
    }

    @Override
    public boolean delete(Template item) {
        return templateDao.delete(item);
    }

    @Override
    public boolean delete(int id) {
        return templateDao.delete(id);
    }

    @Override
    public Template update(Template item) {
        return templateDao.update(item);
    }

    @Override
    public List<Template> getAllTemplates() {
        return templateDao.getAllTemplates();
    }

    @Override
    public Set<TemplateFields> getFieldsByTemplateId(int id) {
        Template template = find(id);

        if (template == null)
            return new HashSet<>();

        return template.getTemplateFields();
    }

    @Override
    public void deleteField(Template template, int field_id) {
        templateDao.deleteField(template,  field_id);
    }

    @Override
    public void deleteFieldValue(Template template, int field_value_id) {
        templateDao.deleteFieldValue(template, field_value_id);

    }

    @Override
    public List<Template> findListTemplatesByCatalogId(int catalog_id) {
        return templateDao.findListTemplatesByCatalogId(catalog_id);
    }
}
