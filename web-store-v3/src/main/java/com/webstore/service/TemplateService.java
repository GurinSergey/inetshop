package com.webstore.service;

import com.webstore.domain.Catalog;
import com.webstore.domain.TemplateFieldsValue;
import com.webstore.repository.TemplateRepo;
import com.webstore.domain.Template;
import com.webstore.domain.TemplateFields;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by MZlenko on 23.08.2016.
 */
@Service
public class TemplateService {
    @Autowired

    private TemplateRepo templateRepo;

    public Template add(Template item) {
        return templateRepo.save(item);

    }

    public Template find(Template item) {
        return templateRepo.findOne(item.getId());
    }

    public Template find(int id) {
        return templateRepo.findOne(id);
    }

    public void delete(Template item) {
        templateRepo.delete(item);
    }

    public void delete(int id) {
        templateRepo.delete(id);
    }

    public Template update(Template item) {
        return templateRepo.save(item);
    }

    public List<Template> getAll() {
        return templateRepo.findAll();
    }

    public Set<TemplateFields> getFieldsByTemplateId(int id) {
        Template template = find(id);

        if (template == null)
            return new HashSet<>();

        return template.getTemplateFields();
    }

    public void deleteField(Template template, int field_id) {
        if (template == null) return;
        for (TemplateFields templateField : template.getTemplateFields()) {
            if (templateField.getId() == field_id) {
                template.getTemplateFields().remove(templateField);
                break;
            }
        }
    }

    public void deleteFieldValue(Template template, long field_value_id) {
        if (template == null) return;

        for (TemplateFields templateField : template.getTemplateFields()) {
            for (TemplateFieldsValue templateFieldsValue : templateField.getTemplateFieldsValues()) {
                if (templateFieldsValue.getId() == field_value_id) {
                    templateField.getTemplateFieldsValues().remove(templateFieldsValue);
                    break;
                }

            }
        }
    }

    public List<Template> findListTemplatesByCatalogId(Catalog catalog) {
        return templateRepo.findAllByCatalog(catalog);
    }
}
