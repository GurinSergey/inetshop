package com.webstore.core.dao;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Model;

import java.util.List;

/**
 * Created by SGurin on 19.07.2016.
 */
public interface ModelDao extends Crud<Model> {
    public List<Model> getAllModels();
}
