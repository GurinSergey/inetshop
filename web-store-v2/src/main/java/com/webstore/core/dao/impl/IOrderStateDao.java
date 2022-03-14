package com.webstore.core.dao.impl;


import com.webstore.core.dao.OrderStateDao;
import com.webstore.core.entities.OrderStateTable;
import com.webstore.core.entities.enums.OrderState;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Andoliny on 31.10.2016.
 */
@Repository
public class IOrderStateDao implements OrderStateDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public List<OrderStateTable> getAllStates() {
        try {
            Query query = em.createQuery("select state from OrderState state");
            return query.getResultList();
        }
        catch (Exception ex){
            System.out.println(ex);
            return null;
        }

    }

    @Override
    @Transactional
    public OrderStateTable findStateByName(String name){
        try {
            Query query = em.createQuery(
                    "select orderState from OrderState orderState " +
                            "where orderState.state = :name ").setParameter("name", name);
            return (OrderStateTable) query.getSingleResult();
        } catch (NoResultException ex) {
            return this.findStateByName(OrderState.NEW.name());
        } catch (Exception e) {
            System.out.println("Статуса заказа '" + name + "' нет в таблице статусов order_state | " + e);
            return new OrderStateTable();
        }

    }


    @Override
    public OrderStateTable add(OrderStateTable state) {
        return em.merge(state);
    }

    @Override
    public OrderStateTable find(OrderStateTable state) {
        return em.find(OrderStateTable.class, state);
    }

    @Override
    public OrderStateTable find(int id) {
       return em.find(OrderStateTable.class, id);
    }

    @Override
    public boolean delete(OrderStateTable state) {
        try{
            em.remove(state);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex);
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            em.remove(em.find(OrderStateTable.class, id));
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }

    @Override
    public OrderStateTable update(OrderStateTable state) {
        return em.merge(state);
    }
}
