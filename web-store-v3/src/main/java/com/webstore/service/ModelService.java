package com.webstore.service;

import com.webstore.repository.ModelRepo;
import com.webstore.domain.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelService {

    @Autowired private ModelRepo modelRepo;

    public List<Model> getAllModels() {
        return modelRepo.findAll();
    }

    public Model add(Model model) {
        return modelRepo.save(model);
    }

    public Model find(Model item) {
        return modelRepo.findOne(item.getId());
    }

    public Model find(Long id) {
        return modelRepo.findOne(id);
    }

    public void delete(Model item) {
        modelRepo.delete(item);
    }

    public void delete (Long id){
        modelRepo.delete(id);
    }

}
