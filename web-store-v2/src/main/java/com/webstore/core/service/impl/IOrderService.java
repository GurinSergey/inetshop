package com.webstore.core.service.impl;

import com.webstore.core.dao.OrderDao;
import com.webstore.core.dao.OrderStateDao;
import com.webstore.core.entities.Order;
import com.webstore.core.entities.enums.OrderState;
import com.webstore.core.entities.json.OrderJson;
import com.webstore.core.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andoliny on 28.10.2016.
 */
@Service
public class IOrderService implements OrderService {
    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderStateDao orderStateDao;

    @Override
    public List<Order> getAllOrders() {
        return orderDao.getAllOrders();
    }

    @Override
    public List<Order> getNewOrders() {
        return orderDao.getOrdersByState(orderStateDao.findStateByName(OrderState.NEW.name()));
    }

    @Override
    public List<Order> getCompleteOrders() {
        return orderDao.getOrdersByState(orderStateDao.findStateByName(OrderState.CLOSED.name()));
    }

    @Override
    public List<OrderJson> getNewOrdersJson(){
        List<OrderJson> orderJson = new ArrayList<>();
        for (Order order : getNewOrders()) {
            orderJson.add(new OrderJson(order));
        }
        return orderJson;
    }

    @Override
    public List<OrderJson> getCompleteOrdersJson() {
        List<OrderJson> orderJson = new ArrayList<>();
        for (Order order : getCompleteOrders()) {
            orderJson.add(new OrderJson(order));
        }
        return orderJson;
    }

    @Override
    public Order add(Order order) {
        return orderDao.add(order);
    }

    @Override
    public Order find(Order order) {
        return orderDao.find(order);
    }

    @Override
    public Order find(int id) {
        return orderDao.find(id);
    }

    @Override
    public boolean delete(Order order) {
        return orderDao.delete(order);
    }

    @Override
    public boolean delete(int id) {
        return orderDao.delete(id);
    }

    @Override
    public Order update(Order order) {
        return orderDao.update(order);
    }
}
