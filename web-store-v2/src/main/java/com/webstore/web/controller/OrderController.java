package com.webstore.web.controller;

import com.webstore.core.entities.Order;
import com.webstore.core.entities.json.OrderJson;
import com.webstore.core.service.OrderService;
import com.webstore.web.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/orders")
public class OrderController {
    @Autowired private OrderService orderService;

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getOrders() {
        return Response.ok(OrderJson.createOrderJsonList(orderService.getAllOrders()));
    }

    @GetMapping(value = "/getById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getOrder(@RequestParam Integer id) {
        Order order =  orderService.find(id);
        return Response.ok(new OrderJson(order));
    }

}
