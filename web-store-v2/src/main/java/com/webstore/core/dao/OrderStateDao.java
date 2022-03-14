package com.webstore.core.dao;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.OrderStateTable;

import java.util.List;

/**
 * Created by Andoliny on 31.10.2016.
 */
public interface OrderStateDao extends Crud<OrderStateTable> {
    public List<OrderStateTable> getAllStates();
    public OrderStateTable findStateByName(String name);
}
