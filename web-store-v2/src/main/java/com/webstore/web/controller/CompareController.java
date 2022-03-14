package com.webstore.web.controller;

import com.webstore.core.service.TemplateService;
import com.webstore.core.session.attribute.CompareProduct;
import com.webstore.core.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by SGurin on 03.11.2016.
 */
@Controller
public class CompareController {

    @Autowired
    private TemplateService templateService;

    @RequestMapping(value = {"/compare"}, method = RequestMethod.GET)
    public ModelAndView showBasket(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        model.setViewName("compare");

        CompareProduct compareProduct = Utils.getCurrentCompare(request);

        if (compareProduct != null) {
            model.addObject("listCompareItems", compareProduct.getProductMap());
            model.addObject("compareCnt", compareProduct.count());
            model.addObject("templates", templateService.getAllTemplates());
        }

        return model;
    }
}
