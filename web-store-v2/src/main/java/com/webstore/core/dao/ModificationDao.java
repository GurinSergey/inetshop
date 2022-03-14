package com.webstore.core.dao;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Modification;

import java.util.List;

/**
 * Created by SGurin on 02.08.2016.
 */
public interface ModificationDao extends Crud<Modification> {
    public List<Modification> getAllModifications();
}
