package com.webstore.core.service.impl;

import com.webstore.core.dao.OrderStateDao;
import com.webstore.core.entities.Order;
import com.webstore.core.entities.enums.OrderState;
import com.webstore.core.entities.json.SmsInfoJson;
import com.webstore.core.service.OrderService;
import com.webstore.core.service.SmsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Andoliny on 09.11.2016.
 */
@Service
public class ISmsInfoService implements SmsInfoService {

    @Autowired
    private OrderService orderService;

    @Autowired
    OrderStateDao orderStateDao;

    @Override
    public SmsInfoJson getSmsInfoJson() {
        SmsInfoJson smsInfoJson = new SmsInfoJson();
        smsInfoJson.setOrdersJson(orderService.getNewOrdersJson());
        return smsInfoJson;
    }

    @Override
    public boolean updateStatus(int orderId, OrderState status) {
        Order order;
        try {
            order = orderService.find(orderId);
            order.setState(status);
            orderService.update(order);
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    @Override
    public boolean updateStatus(List<Integer> idOrdersJson, OrderState status) {
        Order order;
        try {
            for(int id : idOrdersJson){
                order = orderService.find(id);
                order.setState(status);
                orderService.update(order);
            }
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

}
