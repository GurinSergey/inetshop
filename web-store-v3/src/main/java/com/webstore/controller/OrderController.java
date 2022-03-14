package com.webstore.controller;

import com.webstore.base.ValidateState;
import com.webstore.base.stateStepBase.OrderStateHandler;
import com.webstore.base.stateStepBase.OrderStateStep;
import com.webstore.domain.*;
import com.webstore.domain.enums.OrderState;
import com.webstore.domain.enums.ResultCode;
import com.webstore.domain.enums.UserState;
import com.webstore.domain.json.BasketJson;
import com.webstore.domain.json.OrderBasicJson;
import com.webstore.responses.Response;
import com.webstore.responses.ResponseOrder;
import com.webstore.security.services.TokenAuthService;
import com.webstore.service.*;
import com.webstore.session.attribute.Basket;
import com.webstore.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/orders")
public class OrderController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TokenAuthService tokenAuthService;
    @Autowired
    private OrderDetailsService orderDetailsService;
    @Autowired
    OrderProtocolService orderProtocolService;
    @Autowired
    private OrderStateHandler orderStateHandler;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getAll")
    @ResponseBody
    public Response getOrders() {
        return Response.ok(orderService.getAll());
    }

    @GetMapping(value = "/getAllByCurrentUser")
    @ResponseBody
    public Response getUserOrders(@RequestParam(value = "offset") Integer offset,
                                  @RequestParam(value = "limit") Integer limit) {
        User user = tokenAuthService.getCurrentUser();

        List<Order> orders = orderService.getBatchOrdersByClient(user, offset, limit);

        if (orders == null) {
            return Response.ok("У Вас еще нет заказов");
        }

        try {
            List<OrderBasicJson> orderBasicList = new ArrayList<>();

            for (Order order : orders) {
                OrderBasicJson orderBasic = new OrderBasicJson();

                orderBasic.setOrderId(order.getOrderId());
                orderBasic.setOrderDate(order.getOrderDate());
                orderBasic.setSum(order.getSum());

                List<String> photoUrls = new ArrayList<>();
                for (OrderDetails orderDetail : orderDetailsService.find(order)) {
                    Set<ProductPhoto> productPhotos = orderDetail.getProduct().getPhotos();
                    ProductPhoto firstProductPhoto = productPhotos.iterator().next();

                    photoUrls.add(firstProductPhoto.getPath());
                }

                orderBasic.setPhotoUrls(photoUrls);
                orderBasic.setOrderState(OrderState.valueOf(order.getState().name()).getValue());
                orderBasic.setFlag(false);

                orderBasicList.add(orderBasic);
            }

            HashMap<String, Object> results = new HashMap<>();

            results.put("count", orderService.getCountBatchOrdersByClient(user));
            results.put("results", orderBasicList);

            return Response.ok(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.error("Произошла ошибка при получении информации о Ваших заказах с сервера. Обратитесь в тех. поддержку");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getOrder(@RequestParam Long id) {
        return Response.ok(orderService.find(id));
    }

    @RequestMapping(value = "/addOrder", method = RequestMethod.POST)
    @ResponseBody
    public Response addOrder(@RequestBody BasketJson basketJson, HttpServletRequest request) throws IOException {
        try {
            HttpSession session = request.getSession(true);
            Basket basket = Utils.getCurrentBasket(request);

            User user = userService.findUserByEmail(basketJson.getEmail());
            if (user == null) {
                user = new User();
                user.setUserName(basketJson.getEmail());
                user.setPersonName(basketJson.getFio());
                user.setPassword("need_to_change_by_user_during_register");
                user.setUserRole(roleService.findUserRoleByName("ROLE_USER"));
                user.setState(UserState.INACTIVE);

                user = userService.update(user);
            }

            Order order = new Order(user, basketJson.getFio(), null, basketJson.getMobile());

            order.setSum(basket.getTotalSum());
            order.setDeliveryKind(basketJson.getDeliveryKind());
            order.setPaymentMethod(basketJson.getPaymentMethod());
            order.setDeliveryAddress(basketJson.getAddress());
            order.setNote(basketJson.getNote());
            order.setCallBack(basketJson.isCallBack() ? "X" : " ");
            order.setState(OrderState.NEW);

            if (basket.getProductList().size() == 0) {
                return Response.error("Произошла ошибка при оформлении Вашего заказа. Обратитесь в тех. поддержку.");
            }

            for (Product product : basket.getProductList()) {
                OrderDetails orderDetails = new OrderDetails(order, product, product.getPrice(), product.getCnt());
                order.setOrderDetails(orderDetails);
            }

            order = orderService.save(order);

            session.setAttribute("basket", null);

            return Response.ok(order.getOrderId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.error("Произошла ошибка при оформлении Вашего заказа. Обратитесь в тех. поддержку.");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getProtocolById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getOrderProtocol(@RequestParam Long id) {
        return Response.ok(orderProtocolService.findOneByOrderId(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/updateStateOrder", method = RequestMethod.POST)
    @ResponseBody
    public Response updateStateOrder(@RequestParam("orderId") long orderId, @RequestParam("newState") OrderState orderState) {
        /*init*/
        Order order = orderService.find(orderId);
        User user = tokenAuthService.getCurrentUser();

        /*LAO orderProtocol это класс для логирования*/
        OrderProtocol orderProtocol = new OrderProtocol(orderId)
                .addLineProtocol("Cмена статуса " + order.getState() + " --> " + orderState)
                .addLineProtocol("Начало обработки позиций по заказу");


        orderStateHandler.setOrder(order);
        orderStateHandler.setUser(user);
        orderStateHandler.setOrderProtocol(orderProtocol);

        OrderStateStep stateStep = orderStateHandler.runStateStep(orderState);//старт обработки статуса

        if (orderState == OrderState.NEW) {
            user = null;
        }

        order.setOper(user);
        order.setState(stateStep.getOrderState());
        orderService.save(order);

        if (stateStep.isHasError()) {
            orderProtocol.addLineProtocol("Ошибка").addLineProtocol(stateStep.getResponseMessage());
            orderProtocolService.save(orderProtocol);
            if (stateStep.getArrayResponse().size() > 0) {

                return Response.ok(/*ResultCode.NO_REST,*/stateStep.getResultCode(),
                        new ResponseOrder(order.getOrderId(),
                                order.getState(),
                                order.getOper().getPersonName(),
                                stateStep.getArrayResponse()));
            } else return Response.ok(stateStep.getResultCode(), (Object) stateStep.getResponseMessage());
        }

        orderProtocolService.save(orderProtocol.addLineProtocol("обработка успешно завершена"));

        return Response.ok(ValidateState.makeResultCode(user, order), order);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/updateStateOrderMass", method = RequestMethod.POST)
    @ResponseBody
    public Response updateStateOrderMass(@RequestParam("orderId") long[] orderIds, @RequestParam("newState") OrderState orderState) {

        if (1 == 1) return Response.error("Массовое изменение статусов на доработке");

        User user = tokenAuthService.getCurrentUser();
        List<Response> arrayResponse = new ArrayList<Response>();

        for (int i = 0; i < orderIds.length; i++) {
            long orderId = orderIds[i];
            Order order = orderService.find(orderId);
            if (order == null) {
                arrayResponse.add(Response.ok(ResultCode.NOT_FOUND, orderId));
                continue;
            }

            if ((orderState == OrderState.IN_PROCESS) && (ValidateState.makeResultCode(user, order) == ResultCode.READ_ONLY)) {
                arrayResponse.add(Response.ok(ResultCode.READ_ONLY, order));
                continue;
            }
            if (orderState == OrderState.NEW) {
                user = null;
            }

            order.setOper(user);
            order.setState(orderState);
            orderService.save(order);
            arrayResponse.add(Response.ok(ValidateState.makeResultCode(user, order), order));
        }


        return Response.ok(arrayResponse);
    }


}