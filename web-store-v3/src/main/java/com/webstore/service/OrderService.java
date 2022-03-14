package com.webstore.service;

import com.webstore.domain.OrderHistoryStateList;
import com.webstore.domain.User;
import com.webstore.domain.json.OrderJson;
import com.webstore.repository.OrderHistoryStateRepo;
import com.webstore.repository.OrderRepo;
import com.webstore.repository.OrderStateTableRepo;
import com.webstore.domain.Order;
import com.webstore.domain.enums.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andoliny on 28.10.2016.
 */
@Service
public class OrderService {
    @Autowired
    OrderRepo orderRepo;


    /*Эта штука на переработке, всем чмоки в этм чате*/
    @Autowired
    OrderStateTableRepo orderStateTableRepo;
    /**************************************************/

    @Autowired
    OrderHistoryStateRepo orderHistoryStateRepo;

    public List<OrderHistoryStateList> getHistoryState(Order order){
        return orderHistoryStateRepo.findAllByOrder(order);
    }

    public List<Order> getAll() {
        return orderRepo.findAll(sortByIdDESC());
    }

    public List<Order> getNewOrders() {
        return orderRepo.findAllByState(orderStateTableRepo.findByState(OrderState.NEW.name()));
    }

    public Long getCountBatchOrdersByClient(User user) {
        return orderRepo.findAllCountByClient(user);
    }

    public List<Order> getBatchOrdersByClient(User user, int offset, int limit) {
        return orderRepo.findAllByClientWithPagination(user, new PageRequest(offset, limit, Sort.Direction.DESC, "orderId"));
    }

    public List<Order> getCompleteOrders() {
        return orderRepo.findAllByState(orderStateTableRepo.findByState(OrderState.CLOSED.name()));
    }

    public List<OrderJson> getNewOrdersJson(){
        List<OrderJson> orderJson = new ArrayList<>();
        for (Order order : getNewOrders()) {
            orderJson.add(new OrderJson(order));
        }
        return orderJson;
    }

    public List<OrderJson> getCompleteOrdersJson() {
        List<OrderJson> orderJson = new ArrayList<>();
        for (Order order : getCompleteOrders()) {
            orderJson.add(new OrderJson(order));
        }
        return orderJson;
    }

    public Order save(Order order) {
        return orderRepo.save(order);
    }

    public Order find(Order order) {
        return orderRepo.findOne(order.getOrderId());
    }

    public Order find(Long id) {
        return orderRepo.findOne(id);
    }

    public void delete(Order order) {
        orderRepo.delete(order);
    }

    private Sort sortByIdDESC() {
        return new Sort(Sort.Direction.DESC, "orderId");
    }
}
