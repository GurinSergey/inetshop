package com.webstore.web.controller;

import com.webstore.core.base.ComplexBasketStorage;
import com.webstore.core.base.ComplexCompareStorage;
import com.webstore.core.base.WatchHistoryStorage;
import com.webstore.core.entities.Product;
import com.webstore.core.service.ProductService;
import com.webstore.core.session.attribute.Basket;
import com.webstore.core.session.attribute.CompareProduct;
import com.webstore.core.session.attribute.WatchHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.webstore.core.util.Utils.getCurrentBasket;
import static com.webstore.core.util.Utils.getCurrentCompare;
import static com.webstore.core.util.Utils.getCurrentWatchHistory;

@RestController
@RequestMapping(value = "/sessionStorage")
public class LocalStorageController {

    @Autowired
    private ProductService productService;


    @RequestMapping(value = "/synchronizeBasket", method = RequestMethod.POST)
    @ResponseBody
    public ComplexBasketStorage synchronizeBasket(@RequestBody ComplexBasketStorage complexBasketStorage, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(true);
        Basket basket = getCurrentBasket(request);
        if (basket == null){
            complexBasketStorage.setNote("sync from Client");
            basket = complexBasketStorage.getBasket();
            session.setAttribute("basket", basket);
        }
        else{
            complexBasketStorage.setBasket(basket);
            complexBasketStorage.setNote("sync from Server");
        }
        return complexBasketStorage;
    }

    @RequestMapping(value = "/synchronizeWatchHistory", method = RequestMethod.POST)
    @ResponseBody
    public WatchHistoryStorage synchronizeWatchHistory(@RequestBody WatchHistoryStorage watchHistoryStorage, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(true);
        WatchHistory watchHistory =  getCurrentWatchHistory(request);
        if (watchHistory == null){
            watchHistoryStorage.setNote("sync from Client");
            watchHistory = watchHistoryStorage.getWatchHistory();
            session.setAttribute("watchHistory", watchHistory);
        }
        else{
            watchHistoryStorage.setWatchHistory(watchHistory);
            watchHistoryStorage.setNote("sync from Server");
        }
        return watchHistoryStorage;
    }


    @RequestMapping(value = "/synchronizeCompare", method = RequestMethod.POST)
    @ResponseBody
    public ComplexCompareStorage synchronizeCompare(@RequestBody ComplexCompareStorage complexCompareStorage, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(true);
        CompareProduct compareProduct = getCurrentCompare(request);
        if (compareProduct == null){
            complexCompareStorage.setNote("sync from Client");
            compareProduct = complexCompareStorage.getCompareProduct();
            session.setAttribute("compare", compareProduct);
        }
        else{
            complexCompareStorage.setCompareProduct(compareProduct);
            complexCompareStorage.setNote("sync from Server");
        }
        return complexCompareStorage;
    }


    /*история просмотров*/
    @RequestMapping(value = "/addToWatchHistory", method = RequestMethod.POST)
    @ResponseBody
    public WatchHistoryStorage addToWatchHistory(@RequestParam(value = "code") String code, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(true);
        WatchHistory watchHistory = getCurrentWatchHistory(request);

        if (watchHistory == null) {
            watchHistory = new WatchHistory();
            session.setAttribute("watchHistory", watchHistory);

        }

        Product product = productService.findByCode(code);
        watchHistory.addProduct(product);
        WatchHistoryStorage watchHistoryStorage = new WatchHistoryStorage(watchHistory, "test for test");

        return watchHistoryStorage;
    }




}
