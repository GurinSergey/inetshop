package com.webstore.core.dao;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Mark;

import java.util.List;

/**
 * Created by SGurin on 11.07.2016.
 */
public interface MarkDao extends Crud<Mark> {
    public List<Mark> getAllMarks();
}
