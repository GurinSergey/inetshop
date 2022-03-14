package com.webstore.core.service;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Mark;

import java.util.List;

/**
 * Created by SGurin on 19.07.2016.
 */
public interface MarkService extends Crud<Mark> {
    public List<Mark> getAllMarks();
}
