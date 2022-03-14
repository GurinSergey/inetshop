package com.webstore.web.controller;

import com.webstore.core.entities.OrderDetails;
import com.webstore.core.entities.json.OrderDetailsJson;
import com.webstore.core.service.OrderDetailsService;
import com.webstore.web.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/orderDetails")
public class OrderDetailsController {
    @Autowired private OrderDetailsService orderDetailsService;

    @GetMapping(value = "/getById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getOrderDetailsById(@RequestParam int id) {
        List<OrderDetails> orderDetails =  orderDetailsService.getOrderDetails(id);;
        return Response.ok(OrderDetailsJson.createOrderDetailsJsonList(orderDetails));
    }
}
