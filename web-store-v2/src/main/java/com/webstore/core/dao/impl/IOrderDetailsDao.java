package com.webstore.core.dao.impl;

import com.webstore.core.dao.OrderDetailsDao;
import com.webstore.core.entities.OrderDetails;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andoliny on 06.08.2017.
 */
@Repository
public class IOrderDetailsDao implements OrderDetailsDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public OrderDetails add(OrderDetails orderDetails) {
        return em.merge(orderDetails);
    }

    @Override
    public OrderDetails find(OrderDetails orderDetails) {
        return null;
    }

    @Override
    public OrderDetails find(int id) {
        return null;
    }

    @Override
    @Transactional
    public boolean delete(OrderDetails orderDetails) {
        try{
            em.remove(orderDetails);
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
           /* em.createNativeQuery(
                    "delete  from OrderDetails orderDetails " +
                            "where id = ?1").setParameter(1, id).executeUpdate();
            return true;*/
            em.remove(em.find(OrderDetails.class, id));
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    @Transactional
    public OrderDetails update(OrderDetails orderDetails) {
        return em.merge(orderDetails);
    }

    @Override
    @Transactional
    public List<OrderDetails> getOrderDetails(int order_id) {
        try {
            Query query = em.createQuery(
                    "select orderDetails from OrderDetails orderDetails " +
                            "where orderDetails.order.order_id = :order_id").setParameter("order_id", order_id);
            return query.getResultList();
        }
        catch (Exception ex){
            System.out.println("В заказе нет товаров: " + ex);
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public OrderDetails getItem(int order_id, String name) {
        try {
            Query query = em.createQuery(
                    "select orderDetails from OrderDetails orderDetails " +
                            "where orderDetails.order.order_id = :order_id AND  orderDetails.product.title =:name")
                    .setParameter("order_id", order_id)
                    .setParameter("name", name);
            return (OrderDetails) query.getSingleResult();
        }
        catch (Exception ex){
            System.out.println("Товар не найден в заказе: " + ex);
            return new OrderDetails();
        }
    }
}
