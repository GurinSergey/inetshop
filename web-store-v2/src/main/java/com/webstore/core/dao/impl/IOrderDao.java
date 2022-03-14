package com.webstore.core.dao.impl;

import com.webstore.core.dao.OrderDao;
import com.webstore.core.entities.Order;
import com.webstore.core.entities.OrderStateTable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andoliny on 28.10.2016.
 */
@Repository
public class IOrderDao  implements OrderDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public List<Order> getAllOrders() {
        try {
            Query query = em.createQuery("SELECT order FROM Order order");
            return (List<Order>) query.getResultList();
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }

    @Override
    @Transactional
    public List<Order> getOrdersByState(OrderStateTable state) {
        try {
            Query query = em.createQuery(
                    "select order from Order order " +
                            "where order.state = :state").setParameter("state", state);
            return query.getResultList();
        }
        catch (Exception ex){
            System.out.println("Нет новых заказов: " + ex);
            return new ArrayList<>();
        }
    }


    @Override
    @Transactional
    public Order add(Order order) {
        return em.merge(order);
    }

    @Override
    public Order find(Order order) {
        return null;
    }

    @Override
    public Order find(int order_id) {
        try {
            Query query = em.createQuery(
                    "select order from Order order " +
                            "where order.order_id = :order_id").setParameter("order_id", order_id);

            return (Order)query.getSingleResult();
        }
        catch (Exception ex){
            System.out.println("Нет новых заказов: " + ex);
            return new Order();
        }
    }

    @Override
    public boolean delete(Order order) {
        try{
            em.remove(order);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        try {
            em.remove(em.find(Order.class, id));
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    @Transactional
    public Order update(Order order) {
        return em.merge(order);
    }
}
