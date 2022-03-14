package com.webstore.controller;

import com.webstore.domain.json.CatalogJson;
import com.webstore.responses.Response;
import com.webstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/catalog")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping(value = "/getCatalog")
    @ResponseBody
    public Response getCatalog() {
        try {
            CatalogJson catalogJson = new CatalogJson();

            return Response.ok(catalogJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.error("Произошла ошибка при получении каталога товаров. Обратитесь в тех. поддержку");
    }

    @GetMapping(value = "/getAllCatalogs")
    @ResponseBody
    public Response getAllCatalogs() {
        try {
            return Response.ok(catalogService.getCatalogList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.error("Произошла ошибка при получении каталога товаров. Обратитесь в тех. поддержку");
    }

    @GetMapping(value = "/getCatalogs")
    @ResponseBody
    public Response getChildrenCatalogs(@RequestParam("id") int id) {
        try {
            return Response.ok(catalogService.find(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.error("Произошла ошибка при получении каталога товаров. Обратитесь в тех. поддержку");
    }

    @GetMapping(value = "/getParentCatalogs")
    @ResponseBody
    public Response getParentCatalogs() {
        try {
            return Response.ok(catalogService.findByParentIsNull());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.error("Произошла ошибка при получении каталога товаров. Обратитесь в тех. поддержку");
    }

}
