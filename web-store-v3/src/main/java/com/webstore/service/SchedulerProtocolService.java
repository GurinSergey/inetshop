package com.webstore.service;


import com.webstore.domain.SchedulerProtocol;
import com.webstore.repository.SchedulerProtocolRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchedulerProtocolService {

    @Autowired
    SchedulerProtocolRepo schedulerProtocolRepo;

    public SchedulerProtocol findOne(Long id){
        return schedulerProtocolRepo.findOne(id);
    }

    public SchedulerProtocol findOneByOrderId(Long id){
        return schedulerProtocolRepo.findOne(id);
    }

    public SchedulerProtocol save(SchedulerProtocol schedulerProtocol) {
        schedulerProtocol.confirm();
        return schedulerProtocolRepo.save(schedulerProtocol);
    }
}
