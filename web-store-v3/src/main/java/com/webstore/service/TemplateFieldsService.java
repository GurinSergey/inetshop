package com.webstore.service;

import com.webstore.domain.Template;
import com.webstore.domain.TemplateFields;
import com.webstore.repository.TemplateFieldsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateFieldsService {

    @Autowired
    private TemplateFieldsRepo templateFieldsRepo;

    public List<TemplateFields> getFieldsByTemplateId (Long[] productsId) {
        return templateFieldsRepo.getAllByTemplateOrderByOrderAsc(productsId);
    }
}
