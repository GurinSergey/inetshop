package com.webstore.core.dao;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Producer;

import java.util.List;

/**
 * Created by DVaschenko on 22.07.2016.
 */
public interface ProducerDao extends Crud<Producer> {
    Producer findByName(String name);
    List<Producer> getProducers();
}
