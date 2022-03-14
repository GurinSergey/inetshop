package com.webstore.core.service.impl;

import com.webstore.core.service.ModelService;
import com.webstore.core.dao.ModelDao;
import com.webstore.core.entities.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by SGurin on 19.07.2016.
 */
@Service
public class IModelService implements ModelService {

    @Autowired
    private ModelDao modelDao;

    @Override
    public List<Model> getAllModels() {
        return modelDao.getAllModels();
    }

    @Override
    public Model add(Model model) {
        return modelDao.add(model);
    }

    @Override
    public Model find(Model item) {
        return null;
    }

    @Override
    public Model find(int id) {
        return modelDao.find(id);
    }

    @Override
    public boolean delete(Model item) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return modelDao.delete(id);
    }

    @Override
    public Model update(Model item) {
        return null;
    }


}
