package com.webstore.service;

import com.webstore.repository.MarkRepo;
import com.webstore.domain.Mark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarkService {
    @Autowired
    private MarkRepo markRepo;

    public List<Mark> getAllMarks() {
        return markRepo.findAll();
    }

    public Mark find(Long id){
       return markRepo.findOne(id);
    }

    public void delete (Mark mark){
        markRepo.delete(mark);
    }

    public void delete (Long id){
        markRepo.delete(id);
    }

    public void add (Mark mark){
        markRepo.save(mark);
    }

}
