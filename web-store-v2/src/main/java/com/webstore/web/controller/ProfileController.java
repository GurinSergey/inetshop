package com.webstore.web.controller;

import com.webstore.core.entities.Product;
import com.webstore.core.entities.User;
import com.webstore.core.service.ProductService;
import com.webstore.core.session.attribute.Basket;
import com.webstore.core.session.attribute.CompareProduct;
import com.webstore.core.util.Utils;
import com.webstore.security.services.TokenAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * Created by DVaschenko on 22.08.2016.
 */
@Controller
public class ProfileController {

    @Autowired
    private ProductService productService;
    @Autowired private TokenAuthService tokenAuthService;

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getProfilePage(Model model, HttpServletRequest request){
        User user = tokenAuthService.getPrincipal();
        if (user != null){
            List<Product> products = productService.getChosenProductsByUserId(user.getId());
            productService.setTickIsChosen(user.getId(), products);
            model.addAttribute("chosenProducts", products);

            model = fillFullAttributes(model, request);
        }
        return "profile";
    }

    private Model fillFullAttributes(Model model, HttpServletRequest request) {
        Basket basket = Utils.getCurrentBasket(request);

        if (basket != null) {
            model.addAttribute("listBasketItems", basket.getProductList());
            model.addAttribute("basketCnt", basket.count());
        }

        CompareProduct compareProduct = Utils.getCurrentCompare(request);

        if (compareProduct != null) {
            model.addAttribute("listCompareItems", compareProduct.getProductMap());
            model.addAttribute("compareCnt", compareProduct.count());
        }

        return model;
    }
}