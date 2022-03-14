package com.webstore.web.controller.rest;

import com.webstore.core.base.ComplexCompareStorage;
import com.webstore.core.entities.Product;
import com.webstore.core.entities.User;
import com.webstore.core.service.ProductService;
import com.webstore.core.session.attribute.CompareProduct;
import com.webstore.security.services.TokenAuthService;
import com.webstore.web.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.webstore.core.util.Utils.getCurrentCompare;

/**
 * Created by DVaschenko on 18.10.2016.
 */
@Controller
@RequestMapping(value = "/product")
public class ProductRestController {
    @Autowired
    private ProductService productService;
    @Autowired private TokenAuthService tokenAuthService;

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getAll() {
        return Response.ok(productService.getAllProducts());
    }

    @RequestMapping(value = "/toggleChosenProduct", method = RequestMethod.POST)
    @ResponseBody
    public String toggleChosenProduct(@RequestParam(value = "productId") String productId) {
        User user = tokenAuthService.getPrincipal();
        try {
            return productService.mergeChosenProducts(user.getId(), Integer.parseInt(productId));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping(value = "/toggleCompareProduct", method = RequestMethod.POST)
    @ResponseBody
    public ComplexCompareStorage toggleCompareProduct(@RequestParam(value = "productId") String productId, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(true);

        CompareProduct compareProduct = getCurrentCompare(request);

        if (compareProduct == null) {
            compareProduct = new CompareProduct();
            session.setAttribute("compare", compareProduct);
        }

        Product product = productService.find(Integer.parseInt(productId));
        compareProduct.mergeProduct(product);

        if (compareProduct.getProductMap().isEmpty()){
            session.removeAttribute("compare");
        }

        ComplexCompareStorage complexCompareStorage = new ComplexCompareStorage(compareProduct, "test for test");

        return complexCompareStorage;
    }
}

