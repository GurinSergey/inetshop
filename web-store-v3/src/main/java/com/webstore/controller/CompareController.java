package com.webstore.controller;

import com.webstore.domain.ProductOwnerFields;
import com.webstore.domain.TemplateFields;
import com.webstore.responses.Response;
import com.webstore.service.ProductOwnerFieldsService;
import com.webstore.service.TemplateFieldsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(value = "/compare")
public class CompareController {

    @Autowired
    private TemplateFieldsService templateFieldsService;

    @Autowired
    private ProductOwnerFieldsService productOwnerFieldsService;

    @GetMapping("/")
    @ResponseBody
    public Response prepareCompareObject(@RequestParam(value = "productsId") Long[] productsId) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        List<TemplateFields> templateFields = templateFieldsService.getFieldsByTemplateId(productsId);

        List<String> productFieldsValue = null;
        for (TemplateFields templateField : templateFields) {
            productFieldsValue = new ArrayList<>();

            for (Long productId : productsId) {
                productFieldsValue.add(productOwnerFieldsService.getProductFieldsByProductIdAndFieldId(productId, templateField.getId()));
            }

            result.put(templateField.getName(), productFieldsValue);
        }

        return Response.ok(result);
    }
}
