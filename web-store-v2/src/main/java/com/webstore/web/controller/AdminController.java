package com.webstore.web.controller;

import com.webstore.core.dao.UserDao;
import com.webstore.core.dao.impl.IUserDao;
import com.webstore.core.entities.json.OrderJson;
import com.webstore.core.service.*;
import com.webstore.core.service.impl.IMarkService;
import com.webstore.core.service.impl.IModelService;
import com.webstore.core.service.impl.IModificationService;
import com.webstore.core.service.impl.IProductGroupService;
import com.webstore.web.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.webstore.core.util.Utils.getJsonString;

/**
 * Created by SGurin on 17.06.2016.
 */
@Controller
public class AdminController {

    @Autowired
    private IProductGroupService productGroupService;

    @Autowired
    private IMarkService markService;

    @Autowired
    private IModelService modelService;

    @Autowired
    private IModificationService modificationService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private ProducerService producerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;



/*
    @RequestMapping(value = {"/admin"}, method = RequestMethod.GET)
    public ModelAndView admin() {
        ModelAndView model = new ModelAndView();
        model.setViewName("admin");

        return model;
    }

    @RequestMapping(value = {"/admin/goods"}, method = RequestMethod.GET)
    public ModelAndView getGoods() {
        ModelAndView model = new ModelAndView();
        model.setViewName("goods");

        model.addObject("templates", templateService.getAllTemplates());
        model.addObject("producers", producerService.getProducers());
        model.addObject("products", productService.getAllProducts());
        model.addObject("addItem", false);

        return model;
    }

    @RequestMapping(value = {"/admin/add_items"}, method = RequestMethod.GET)
    public ModelAndView getGoodsToAdd(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        model.setViewName("goods");
        model.addObject("products", productService.getAllProducts());
        if(request.getHeader("referer") != null){
            model.addObject("addItem", true);
        }

        return model;
    }

    @RequestMapping(value = {"/admin/mark"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getMark(@RequestParam String productCode) {
        ModelAndView model = new ModelAndView();

        Map<String, Object> allObjects = new HashMap<>();
        allObjects.put("marks", markService.getAllMarks());
        model.addAllObjects(allObjects);
        model.setViewName("mark");

        return model;
    }

    @RequestMapping(value = {"/admin/model"}, method = RequestMethod.GET)
    public ModelAndView getModel() {
        ModelAndView model = new ModelAndView();

        Map<String, Object> allObjects = new HashMap<>();
        allObjects.put("marks", markService.getAllMarks());
        allObjects.put("models", modelService.getAllModels());
        model.addAllObjects(allObjects);
        model.setViewName("model");

        return model;
    }

    @RequestMapping(value = {"/admin/modification"}, method = RequestMethod.GET)
    public ModelAndView getModification() {
        ModelAndView model = new ModelAndView();

        Map<String, Object> allObjects = new HashMap<>();
        allObjects.put("marks", markService.getAllMarks());
        allObjects.put("modifications", modificationService.getAllModifications());
        model.addAllObjects(allObjects);
        model.setViewName("modification");

        return model;
    }

    @RequestMapping(value = {"/admin/catalog"}, method = RequestMethod.GET)
    public ModelAndView getCatalog() {
        ModelAndView model = new ModelAndView();

        Map<String, Object> allObjects = new HashMap<>();
        allObjects.put("catalogList", getJsonString(catalogService.getCatalogListJson()));
        allObjects.put("catalog", catalogService.getAllCatalogList());
        allObjects.put("sparePartCategory", productGroupService.getProductGroupList());
        model.addAllObjects(allObjects);
        model.setViewName("catalog");

        return model;
    }


    @RequestMapping(value = {"/admin/templates"}, method = RequestMethod.GET)
    public ModelAndView getTemplates() {
        ModelAndView model = new ModelAndView();

        model.addObject("templates", templateService.getAllTemplates());
        model.setViewName("templates");

        return model;
    }*/




}
