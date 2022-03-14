package com.webstore.controller;


import com.webstore.base.ValidateState;
import com.webstore.domain.*;
import com.webstore.domain.enums.*;
import com.webstore.responses.Response;
import com.webstore.security.services.TokenAuthService;
import com.webstore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/orderDetails")
public class OrderDetailsController {

    @Autowired
    private TokenAuthService tokenAuthService;
    @Autowired
    private OrderDetailsService orderDetailsService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private ReserveOrderService reserveOrderService;


    @GetMapping(value = "/adminGetOrderDetailsById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response adminGetOrderDetailsById(@RequestParam Long id) {
        User user = tokenAuthService.getCurrentUser();
        Order order = orderService.find(id);

        if (order == null) {
            return Response.error("Such order does not exist");
        }

        //LAO проверим доступ к деталям заказа getResultCode

        List<OrderDetails> orderDetails = orderDetailsService.find(order);

        return Response.ok(ValidateState.makeResultCode(user, order), orderDetails);
    }

    @GetMapping(value = "/userGetOrderDetailsById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response userGetOrderDetailsById(@RequestParam Long id) {
        User user = tokenAuthService.getCurrentUser();
        Order order = orderService.find(id);

        if (order == null) {
            return Response.error("Заказа с id = " + id + " не существует");
        }

        if (!user.getId().equals(order.getClient().getId())) {
            return Response.error("Доступ запрещен");
        }

        List<OrderDetails> orderDetails = orderDetailsService.find(order);

        return Response.ok(ValidateState.makeResultCode(user, order), orderDetails);
    }

    @GetMapping(value = "/getOrderStatesById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getOrderStatesById(@RequestParam Long id) {

        Order order = orderService.find(id);

        if (order == null) {
            return Response.error("Заказа с id = " + id + " не существует");
        }

        List<OrderHistoryStateList> orderHistoryStateList = orderService.getHistoryState(order);

        return Response.ok(orderHistoryStateList);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getAllReserveByOrderDetailId", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getAllReserveByOrderDetailId(@RequestParam Long id) {

        List<ReservOrderDetail> reservOrderDetails = reserveOrderService.findAllReserveByOrderDetailId(id);

        return Response.ok(reservOrderDetails);
    }


    private boolean isCorrectInvoice(OrderProtocol orderProtocol, Invoice invoice) {
        if ((invoice == null) || (invoice.getState() == InvoiceState.CLOSED) || (invoice.getInvoiceType() == InvoiceType.PURCHASE)) {
            orderProtocol.addLineProtocol("Накладная не найдена или закрыта, невозможно изменить остатки");
            return false;
        }
        return true;
    }


}