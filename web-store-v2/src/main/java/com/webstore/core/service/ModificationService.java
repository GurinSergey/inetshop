package com.webstore.core.service;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Modification;

import java.util.List;

/**
 * Created by SGurin on 02.08.2016.
 */
public interface ModificationService extends Crud<Modification> {
    public List<Modification> getAllModifications();
}
