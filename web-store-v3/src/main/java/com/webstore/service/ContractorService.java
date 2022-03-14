package com.webstore.service;

import com.webstore.domain.Contractor;
import com.webstore.repository.ContractorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;


@Service
public class ContractorService {
    @Autowired
    ContractorRepo contractorRepo;

    public List<Contractor> getAll() {
        return contractorRepo.findAll();
    }

    public Contractor update(Contractor contractor) {
        return contractorRepo.save(contractor);
    }

    public List<Contractor> getByMatchers(Contractor contractor){
        SortOrder sortOrder = SortOrder.ASCENDING;
        String sortField ="id";
        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)/*.withIgnorePaths("mfo")*/;

        Example<Contractor> example = Example.of(contractor, matcher);

        Page<Contractor> contractors =  contractorRepo.findAll(example,
                new PageRequest(0, 50, sortOrder == SortOrder.ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));


        return contractors.getContent();

    }

}
