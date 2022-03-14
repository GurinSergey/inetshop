package com.webstore.controller;

import com.webstore.base.TemplateInfo;
import com.webstore.domain.Product;
import com.webstore.responses.Response;
import com.webstore.service.ProductService;
import com.webstore.util.Transcriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/search")
public class SearchController {
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    @ResponseBody
    public Response getBatchProducts(
            @RequestParam(value = "templates_offset") Integer templates_offset,
            @RequestParam(value = "templates_limit") Integer templates_limit,
            @RequestParam(value = "products_limit") Integer products_limit,
            @RequestParam(value = "filter_string") String filter_string) {
        HashMap<String, Object> results = new HashMap<>();

        List<TemplateInfo> uniqueProductTemplates = productService.getAllProductTemplatesByFilterCond(templates_offset, templates_limit, filter_string);

        List<Object> searchProductsInfo = new ArrayList<>();
        Integer totalCnt = 0;

        for (int i = 0; i < uniqueProductTemplates.size(); i++) {
            Integer templateId = uniqueProductTemplates.get(i).getTemplateId();
            String templateName = uniqueProductTemplates.get(i).getTemplateName();

            HashMap<String, Object> productsByTemplate = new HashMap<>();

            productsByTemplate.put("templateId", templateId);
            productsByTemplate.put("templateName", templateName);

            List<Product> productsByTemplateId = productService.getAllProductByTemplateIdAndFilterCond(products_limit, templateId, filter_string);
            totalCnt += productsByTemplateId.size();
            productsByTemplate.put("results", productsByTemplateId);

            searchProductsInfo.add(productsByTemplate);
        }

        results.put("allTemplatesCnt", productService.getCountProductTemplatesByFilterCond(filter_string));
        results.put("searchProductsCnt", totalCnt);
        results.put("results", searchProductsInfo);

        return Response.ok(results);
    }
}
