package com.webstore.service;

import com.webstore.repository.ModificationRepo;
import com.webstore.domain.Modification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by SGurin on 02.08.2016.
 */
@Service
public class ModificationService {

    @Autowired private ModificationRepo modificationRepo;

    public List<Modification> getAllModifications() {
        return modificationRepo.findAll();
    }

    public Modification add(Modification modification) {
        return modificationRepo.save(modification);
    }

    public Modification find(Modification item) {
        return modificationRepo.findOne(item.getId());
    }

    public Modification find(int id) {
        return modificationRepo.findOne(id);
    }

    public void delete(Modification item) {
        modificationRepo.delete(item);
    }

    public void delete(int id){
        modificationRepo.delete(id);
    }

}
