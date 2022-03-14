package com.webstore.controller;

import com.webstore.domain.Catalog;
import com.webstore.domain.Template;
import com.webstore.domain.TemplateFields;
import com.webstore.responses.Response;
import com.webstore.service.ProductOwnerFieldsService;
import com.webstore.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(value = "/templates")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private ProductOwnerFieldsService fieldsService;

    @GetMapping(value = "/getAllTemplates")
    @ResponseBody
    public Response getAllTemplates() {
        try {
            return Response.ok(templateService.getAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.error("Произошла ошибка при получении шаблонов. Обратитесь в тех. поддержку");
    }

    @GetMapping(value = "/getCntFilterValue")
    @ResponseBody
    public Response getCntFilterValue(
            @RequestParam("id") int templateId,
            @RequestParam("valueId") Long fieldValueId) {
        return Response.ok(fieldsService.getCntByValueIdAndTemplateId(templateId, fieldValueId));
    }

    @GetMapping(value = "/getTemplateFieldsById")
    @ResponseBody
    public Response getTemlpateFieldsById(@RequestParam int id) {
        try{
            return Response.ok(templateService.find(id).getTemplateFields());
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("Произошла ошибка при получении полей шаблона. Обратитесь в тех. поддержку");
        }
    }

    @GetMapping(value = "/getFieldsByTemplateId")
    @ResponseBody
    public Response getFieldsByTemplateId(@RequestParam int id) {
        try {
            Map<Integer, String> result = new TreeMap<>();
            Template template = templateService.find(id);
            Set<TemplateFields> templateFields = templateService.getFieldsByTemplateId(template.getId());

            result = setToMap(templateFields, result);
            Catalog parent = template.getCatalog().getParent();

            while (parent != null) {
                template = templateService.find(parent.getId());
                templateFields = templateService.getFieldsByTemplateId(template.getId());

                result = setToMap(templateFields, result);

                parent = template.getCatalog().getParent();
            }

            return Response.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.error("Произошла ошибка при получении полей шаблона. Обратитесь в тех. поддержку");
    }

    private Map<Integer, String> setToMap (Set<TemplateFields> set, Map<Integer, String> map) {
        for (TemplateFields t : set) {
            map.put(t.getId(), t.getName());
        }
        return map;
    }

    @GetMapping(value = "/getTemplateById")
    @ResponseBody
    public Response getTemlpateById(@RequestParam int id) {
        try {
            return Response.ok(templateService.find(id));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("Произошла ошибка при получении полей шаблона. Обратитесь в тех. поддержку");
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/saveTemplate"}, method = RequestMethod.POST)
    public Response saveTemplate(@RequestBody Template template) {
        templateService.add(template);
        return Response.ok(template);

    }

    @ResponseBody
    @RequestMapping(value = {"/deleteTemplate/{id}"}, method = RequestMethod.DELETE)
    public Response deleteTemplate(@PathVariable("id") int id) {
        try {
            templateService.delete(id);
            return Response.ok(0);
        } catch (Exception e) {
            return Response.error(e.getMessage().toString());
        }
    }
}
