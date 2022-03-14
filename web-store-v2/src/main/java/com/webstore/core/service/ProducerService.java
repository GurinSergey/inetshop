package com.webstore.core.service;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Producer;

import java.util.List;

/**
 * Created by DVaschenko on 22.09.2016.
 */
public interface ProducerService extends Crud<Producer> {
    List<Producer> getProducers();
}
