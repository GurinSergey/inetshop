package com.webstore.service;


import com.webstore.repository.SchedulerRepo;
import com.webstore.domain.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SchedulerService {

    @Autowired
    private SchedulerRepo schedulerRepo;


    public List<Scheduler> getAll(){
        return schedulerRepo.findAll();
    }

    public Scheduler update(Scheduler scheduler) {

        return schedulerRepo.save(scheduler);
    }

    public Scheduler find(Scheduler item) {
        return schedulerRepo.findOne(item.getSchedulerId());
    }

    public Scheduler find(long id) {
        return schedulerRepo.findOne(id);
    }

    public void delete(Scheduler item) {
        schedulerRepo.delete(item);
    }

    public Scheduler findBySchedulerName(String name){
       return schedulerRepo.findBySchedulerName(name);
    }

    public  List<Scheduler> findAllCurrentTask(Date date){
        return schedulerRepo.findAllCurrentTask(date);
    }


}
