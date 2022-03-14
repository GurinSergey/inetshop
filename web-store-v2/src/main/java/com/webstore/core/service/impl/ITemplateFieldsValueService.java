package com.webstore.core.service.impl;

import com.webstore.core.dao.TemplateFieldsValueDao;
import com.webstore.core.entities.TemplateFieldsValue;
import com.webstore.core.service.TemplateFieldsValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DVaschenko on 22.09.2016.
 */
@Service
public class ITemplateFieldsValueService implements TemplateFieldsValueService {
    @Autowired
    private TemplateFieldsValueDao fieldsValueDao;

    @Override
    public TemplateFieldsValue add(TemplateFieldsValue item) {
        return fieldsValueDao.add(item);
    }

    @Override
    public TemplateFieldsValue find(TemplateFieldsValue item) {
        return fieldsValueDao.add(item);
    }

    @Override
    public TemplateFieldsValue find(int id) {
        return fieldsValueDao.find(id);
    }

    @Override
    public boolean delete(TemplateFieldsValue item) {
        return fieldsValueDao.delete(item);
    }

    @Override
    public boolean delete(int id) {
        return fieldsValueDao.delete(id);
    }

    @Override
    public TemplateFieldsValue update(TemplateFieldsValue item) {
        return fieldsValueDao.update(item);
    }
}
