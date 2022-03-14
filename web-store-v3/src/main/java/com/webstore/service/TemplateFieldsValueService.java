package com.webstore.service;

import com.webstore.domain.TemplateFieldsValue;
import com.webstore.repository.TemplateFieldsValueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplateFieldsValueService {

    @Autowired private TemplateFieldsValueRepo fieldsValueRepo;


    public TemplateFieldsValue add(TemplateFieldsValue item) {
        return fieldsValueRepo.save(item);
    }

    public TemplateFieldsValue find(TemplateFieldsValue item) {
        return fieldsValueRepo.findOne(item.getId());
    }

    public TemplateFieldsValue find(long id) {
        return fieldsValueRepo.findOne(id);
    }

    public void delete(TemplateFieldsValue item) {
        fieldsValueRepo.delete(item);
    }

    public TemplateFieldsValue update(TemplateFieldsValue item) {
        return fieldsValueRepo.save(item);
    }
}
