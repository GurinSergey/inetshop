package com.webstore.service;

import com.webstore.repository.OrderStateTableRepo;
import com.webstore.domain.Order;
import com.webstore.domain.enums.OrderState;
import com.webstore.domain.json.SmsInfoJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Andoliny on 09.11.2016.
 */
@Service
public class SmsInfoService {

    @Autowired
    private OrderService orderService;

    @Autowired
    OrderStateTableRepo orderStateTableRepo;

    public SmsInfoJson getSmsInfoJson() {
        SmsInfoJson smsInfoJson = new SmsInfoJson();
        smsInfoJson.setOrdersJson(orderService.getNewOrdersJson());
        return smsInfoJson;
    }

    public boolean updateStatus(Long orderId, OrderState status) {
        Order order;
        try {
            order = orderService.find(orderId);
            order.setState(status);
            orderService.save(order);
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    public boolean updateStatus(List<Long> idOrdersJson, OrderState status) {
        Order order;
        try {
            for(long id : idOrdersJson){
                order = orderService.find(id);
                order.setState(status);
                orderService.save(order);
            }
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

}
