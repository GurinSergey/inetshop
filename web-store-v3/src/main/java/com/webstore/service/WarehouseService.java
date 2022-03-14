package com.webstore.service;

import com.webstore.domain.Warehouse;
import com.webstore.repository.WarehouseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WarehouseService {
    @Autowired
    WarehouseRepo warehouseRepo;


    public Warehouse getOne(Long id) {
        return warehouseRepo.findOne(id);
    }

    public List<Warehouse> getAll() {
        return warehouseRepo.findAll();
    }


    public Warehouse update(Warehouse warehouse) {
        return warehouseRepo.save(warehouse);
    }



}
