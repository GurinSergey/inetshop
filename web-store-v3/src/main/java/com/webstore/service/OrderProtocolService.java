package com.webstore.service;

import com.webstore.domain.OrderProtocol;
import com.webstore.repository.OrderProtocolRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderProtocolService {
    @Autowired
    OrderProtocolRepo orderProtocolRepo;

    public OrderProtocol findOne(Long id){
        return orderProtocolRepo.findOne(id);
    }

    public OrderProtocol findOneByOrderId(Long id){
        return orderProtocolRepo.findOne(id);
    }

    public OrderProtocol save(OrderProtocol orderProtocol) {
        orderProtocol.confirm();
        return orderProtocolRepo.save(orderProtocol);
    }
}
