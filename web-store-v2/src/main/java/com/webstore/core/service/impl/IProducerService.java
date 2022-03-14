package com.webstore.core.service.impl;

import com.webstore.core.dao.ProducerDao;
import com.webstore.core.entities.Producer;
import com.webstore.core.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by DVaschenko on 22.09.2016.
 */
@Service
public class IProducerService implements ProducerService {
    @Autowired
    private ProducerDao producerDao;

    @Override
    public Producer add(Producer item) {
        return producerDao.add(item);
    }

    @Override
    public Producer find(Producer item) {
        return producerDao.find(item);
    }

    @Override
    public Producer find(int id) {
        return producerDao.find(id);
    }

    @Override
    public boolean delete(Producer item) {
        return producerDao.delete(item);
    }

    @Override
    public boolean delete(int id) {
        return producerDao.delete(id);
    }

    @Override
    public Producer update(Producer item) {
        return producerDao.update(item);
    }

    @Override
    public List<Producer> getProducers() {
        return producerDao.getProducers();
    }
}
