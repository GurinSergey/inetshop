package com.webstore.controller;


import com.webstore.domain.Order;
import com.webstore.domain.User;
import com.webstore.domain.enums.OrderState;
import com.webstore.domain.enums.ResultCode;
import com.webstore.metrics.ActiveUserStore;
import com.webstore.responses.Response;
import com.webstore.security.services.TokenAuthService;
import com.webstore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private TokenAuthService tokenAuthService;

    @Autowired
    ActiveUserStore activeUserStore;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/updateOrder", method = RequestMethod.POST)
    @ResponseBody
    public Response updateOrder(@RequestBody Order order) {
        Order oldOrder = orderService.find(order.getOrderId());
        if (oldOrder == null) {
            return Response.ok(ResultCode.NOT_FOUND, order);
        }
        User user = tokenAuthService.getCurrentUser();
        //LAO обновляем только то что разрешаем в контроллере! никаких старый ордер = новый ордер быть не должно!
        oldOrder.setDeliveryKind(order.getDeliveryKind());
        oldOrder.setDeliveryAddress(order.getDeliveryAddress());
        oldOrder.setNote(order.getNote());
        oldOrder.setDeliveryDate(order.getDeliveryDate());
        oldOrder.setPeriodTime(order.getPeriodTime());
        oldOrder.setPhone(order.getPhone());
        oldOrder.setOper(user);
        //LAO убрал эту запись из стории смены статусов
       // oldOrder.setState(OrderState.MANAGER_CHANGE);
        //обновляем пользователя
        orderService.save(oldOrder);

        return Response.ok(oldOrder);
    }

//отдельный сервис получения данных списка товаров для админки
    @GetMapping(value = "/getAllProduct", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getAll() {
        return Response.ok(productService.getAll());
    }

    @RequestMapping(value = "/loggedUsers", method = RequestMethod.GET)
    public String getLoggedUsers() {
       return new String("wtf");
    }




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
