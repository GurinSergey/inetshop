package com.webstore.core.dao;

import com.webstore.core.entities.Delivery;

import java.util.List;

/**
 * Created by DVaschenko on 09.02.2018.
 */
public interface DeliveryDao {
    List<Delivery> getListDelivery();
}
