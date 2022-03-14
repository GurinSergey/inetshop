package com.webstore.service;

import com.webstore.domain.Product;
import com.webstore.domain.User;
import com.webstore.domain.WatchProduct;
import com.webstore.repository.WatchRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class WatchService {

    @Autowired
    private WatchRepo watchRepo;

    public WatchProduct save(WatchProduct watchProduct) {
        return watchRepo.save(watchProduct);
    }

    public Set<Product> findAllWatchProductByUser(User user){
        return watchRepo.findAllWatchProductByUser(user);
    }
}
