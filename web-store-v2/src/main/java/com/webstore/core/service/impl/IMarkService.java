package com.webstore.core.service.impl;

import com.webstore.core.dao.MarkDao;
import com.webstore.core.entities.Mark;
import com.webstore.core.service.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by SGurin on 11.07.2016.
 */
@Service
public class IMarkService implements MarkService{
    @Autowired
    private MarkDao markDao;

    public List<Mark> getAllMarks() {
        return markDao.getAllMarks();
    }

    @Override
    public Mark add(Mark mark) {
        return markDao.add(mark);
    }

    @Override
    public boolean delete(int id) {
        return markDao.delete(id);
    }

    @Override
    public Mark find(Mark item) {
        return null;
    }

    @Override
    public Mark find(int id) {
        return markDao.find(id);
    }

    @Override
    public boolean delete(Mark item) {
        return false;
    }

    @Override
    public Mark update(Mark item) {
        return null;
    }
}
