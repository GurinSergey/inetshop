package com.webstore.service;

import com.webstore.domain.ProductOwnerFields;
import com.webstore.domain.TemplateFields;
import com.webstore.repository.ProductOwnerFieldsRepo;
import com.webstore.repository.TemplateFieldsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductOwnerFieldsService {

    @Autowired
    private ProductOwnerFieldsRepo productOwnerFieldsRepo;

    public String getProductFieldsByProductIdAndFieldId(Long productId, int templateFieldId) {
        return productOwnerFieldsRepo.getAllByProductAndTemplateFields(productId, templateFieldId);
    }

    public int getCntByValueIdAndTemplateId(int templateId, Long valueId){
        return productOwnerFieldsRepo.getCntByValueIdAndTemplateId(templateId, valueId);
    }
}
