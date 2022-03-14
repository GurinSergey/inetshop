package com.webstore.service;

import com.webstore.domain.FavoriteProduct;
import com.webstore.repository.FavoriteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    @Autowired
    private FavoriteRepo favoriteRepo;

    public FavoriteProduct save(FavoriteProduct product){
        return favoriteRepo.save(product);
    }

    public List<FavoriteProduct> getFavoriteListByUserId(long userId){
        return favoriteRepo.findByUserOrderByFavoriteDate(userId);
    }
}
