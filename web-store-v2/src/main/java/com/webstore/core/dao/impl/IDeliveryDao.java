package com.webstore.core.dao.impl;

import com.webstore.core.dao.DeliveryDao;
import com.webstore.core.entities.Delivery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by DVaschenko on 09.02.2018.
 */
@Repository
public class IDeliveryDao implements DeliveryDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Delivery> getListDelivery() {
        return (List<Delivery>)entityManager.createQuery("select d from Delivery d").getResultList();
    }
}
