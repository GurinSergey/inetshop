package com.webstore.controller;

import com.webstore.domain.Product;
import com.webstore.domain.ProductOwnerFields;
import com.webstore.domain.User;
import com.webstore.domain.WatchProduct;
import com.webstore.domain.json.ProductFieldsJson;
import com.webstore.responses.Response;
import com.webstore.security.services.TokenAuthService;
import com.webstore.service.ProductService;
import com.webstore.service.StorageFileService;
import com.webstore.service.WatchService;
import com.webstore.session.attribute.WatchHistory;
import com.webstore.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DVaschenko on 18.10.2016.
 */
@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private TokenAuthService tokenAuthService;
    @Autowired
    private StorageFileService fileService;
    @Autowired
    private WatchService watchService;

    /*Work with product*/
    @GetMapping("/{id}/product")
    @ResponseBody
    public Response getBatchProducts(
            @PathVariable("id") Integer id,
            @RequestParam(value = "offset") Integer offset,
            @RequestParam(value = "limit") Integer limit,
            @RequestParam(value = "sort_direction") String sort_direction,
            @RequestParam(value = "sort_property") String sort_property,
            @RequestParam(value = "filter", required = false) String filter) {
        HashMap<String, Object> results = new HashMap<>();

        results.put("count", /*productService.getCountPackageByTemplateId(id)*/5);
//        results.put("results", productService.getPackageProductsByTemplateId(id, offset, limit, productService.getSort(sort_direction, sort_property)));

        results.put("results", productService.findAllByFilterParams(id, offset, limit, sort_direction, sort_property, filter));
        return Response.ok(results);
    }

    @GetMapping(value = "/product/getAutoCompleteProductForSearch")
    @ResponseBody
    public Response getAutoCompleteProductForSearch(@RequestParam(value = "match") String match) {
        List<Product> productList = productService.getByMatchers(match);

        return Response.ok(productList);
    }

    @GetMapping("/product/{ident}")
    @ResponseBody
    public Response getOneProduct(@PathVariable("ident") String ident, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Product product = productService.find(ident);
        addToWatch(product, request,session);
        return Response.ok(product);
    }

    @GetMapping("/product/{id}/fields")
    @ResponseBody
    public Response getProductFields(@PathVariable("id") Long id) {
        try {
            Product product = productService.find(id);
            if (product == null) {
                return Response.error("Нет продукта с id = " + id);
            }
            if (product.getFields() == null) {
                return Response.error("У продукта с id = " + id + "нет характеристик");
            }

            List<ProductFieldsJson> result = new ArrayList<>();
            product.getFields().forEach(productOwnerFields ->
                    result.add(new ProductFieldsJson(
                        productOwnerFields.getTemplateFields().getId(),
                        productOwnerFields.getTemplateFields().getName(),
                        productOwnerFields.getFieldsValue())
                    )
            );

            return Response.ok(result);

        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }
    /*End*/

    /*Work with photo of product*/
    @GetMapping("/product/{id}/photos")
    @ResponseBody public Response getProductPhotos(
            @PathVariable("id") long productId){
        return Response.ok(productService.getProductPhotos(productId));
    }

    @GetMapping("/product/photo/")
    public @ResponseBody
    Response getFile(
            @RequestParam(value = "id") String catalogId,
            @RequestParam(value = "filename") String filename) {
        try {
            Resource file = fileService.load(catalogId, filename);
            return Response.ok(file.toString());
        } catch (MalformedURLException e) {
            return Response.error("FAIL...");
        }
    }

    @PostMapping("/product/photo/load/")
    public @ResponseBody
    Response loadFile(
            @RequestParam(value = "id") String catalogId,
            @RequestParam(value = "file") MultipartFile file) {
        try {
            fileService.store(catalogId, file);

            return Response.ok("File successfully uploaded");
        } catch (IOException e) {
            return Response.error("FAIL to upload");
        }
    }
    /*End*/


    @GetMapping(value = "/product/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getAll() {
        return Response.ok(productService.getAll());
    }

    /*LAO watch history*/
    private void addToWatch(Product product, HttpServletRequest request,HttpSession session) {
        User user = null;
        WatchHistory watchHistory = Utils.getCurrentWatchHistory(request, session);

        if (watchHistory == null) {
            watchHistory = new WatchHistory();
        }
        watchHistory.addProduct(product);

        try {
            if (Utils.isAuthentication()) {
                user = tokenAuthService.getPrincipal().getUser();
            }
            WatchProduct watchProduct = new WatchProduct(product, user);
            watchService.save(watchProduct);
            session.setAttribute("watchHistory", watchHistory);
        } catch (Exception e) {
            //LAO тут могла быть ваша реклама
            e.printStackTrace();
        }

    }




//    @RequestMapping(value = "/toggleChosenProduct", method = RequestMethod.POST)
//    @ResponseBody
//    public String toggleChosenProduct(@RequestParam Long productId) {
//        User user = tokenAuthService.getPrincipal().getUser();
//        try {
//            return productService.mergeChosenProducts(user.getId(), productId);
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//            return "error";
//        }
//    }

//    @RequestMapping(value = "/toggleCompareProduct", method = RequestMethod.POST)
//    @ResponseBody
//    public ComplexCompareStorage toggleCompareProduct(@RequestParam Long productId, HttpServletRequest request) throws IOException {
//        HttpSession session = request.getSession(true);
//
//        CompareProduct compareProduct = getCurrentCompare(request);
//
//        if (compareProduct == null) {
//            compareProduct = new CompareProduct();
//            session.setAttribute("compare", compareProduct);
//        }
//
//        Product product = productService.find(productId);
//        compareProduct.mergeProduct(product);
//
//        if (compareProduct.getProductMap().isEmpty()){
//            session.removeAttribute("compare");
//        }
//
//        ComplexCompareStorage complexCompareStorage = new ComplexCompareStorage(compareProduct, "test for test");
//
//        return complexCompareStorage;
//    }
}

