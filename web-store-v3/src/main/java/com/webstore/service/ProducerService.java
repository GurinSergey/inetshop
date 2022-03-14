package com.webstore.service;

import com.webstore.repository.ProducerRepo;
import com.webstore.domain.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProducerService {
    @Autowired private ProducerRepo producerRepo;


    public Producer add(Producer item) {
        return producerRepo.save(item);
    }

    public Producer find(Producer item) {
        return producerRepo.findOne(item.getId());
    }

    public Producer find(Long id) {
        return producerRepo.findOne(id);
    }

    public void delete(Producer item) {
        producerRepo.delete(item);
    }

    public List<Producer> getProducers() {
        return producerRepo.findAll();
    }
}
