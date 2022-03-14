package com.webstore.core.dao;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.OrderDetails;

import java.util.List;

/**
 * Created by Andoliny on 06.08.2017.
 */
public interface OrderDetailsDao extends Crud<OrderDetails> {
    public List<OrderDetails> getOrderDetails(int id);
    public OrderDetails getItem(int id, String name);
}
