package com.webstore.core.service.impl;

import com.webstore.core.dao.ModificationDao;
import com.webstore.core.entities.Modification;
import com.webstore.core.service.ModificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by SGurin on 02.08.2016.
 */
@Service
public class IModificationService implements ModificationService {

    @Autowired
    private ModificationDao modificationDao;

    @Override
    public List<Modification> getAllModifications() {
        return modificationDao.getAllModifications();
    }

    @Override
    public Modification add(Modification modification) {
        return modificationDao.add(modification);
    }

    @Override
    public Modification find(Modification item) {
        return null;
    }

    @Override
    public Modification find(int id) {
        return null;
    }

    @Override
    public boolean delete(Modification item) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return modificationDao.delete(id);
    }

    @Override
    public Modification update(Modification item) {
        return null;
    }
}
