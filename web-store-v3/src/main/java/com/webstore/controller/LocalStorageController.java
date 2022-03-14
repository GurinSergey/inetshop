package com.webstore.controller;

import com.webstore.base.ComplexBasketStorage;
import com.webstore.base.ComplexCompareStorage;
import com.webstore.base.FavoriteStorage;
import com.webstore.base.WatchHistoryStorage;
import com.webstore.base.exception.CheckException;
import com.webstore.domain.FavoriteProduct;
import com.webstore.domain.Product;
import com.webstore.domain.User;
import com.webstore.responses.Response;
import com.webstore.security.config.CustomUserDetails;
import com.webstore.security.services.TokenAuthService;
import com.webstore.service.FavoriteService;
import com.webstore.service.ProductService;
import com.webstore.service.WatchService;
import com.webstore.session.attribute.Basket;
import com.webstore.session.attribute.CompareProduct;
import com.webstore.session.attribute.WatchHistory;
import com.webstore.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/sessionStorage")
public class LocalStorageController {

    @Autowired
    private ProductService productService;

    @Autowired
    private WatchService watchService;

    @Autowired
    private FavoriteService favoriteService;


    @Autowired
    private TokenAuthService tokenAuthService;


    @RequestMapping(value = "/synchronizeBasket", method = RequestMethod.POST)
    @ResponseBody
    public Response synchronizeBasket(@RequestBody ComplexBasketStorage complexBasketStorage, HttpServletRequest request) throws IOException {
        try {
            HttpSession session = request.getSession(true);

            complexBasketStorage.setNote("sync from Client");
            Basket basket = complexBasketStorage.getBasket();

            Long[] productsId = new Long[basket.count()];
            for (int i = 0; i < basket.getProductList().size(); i++) {
                Product product = productService.find(basket.getProductList().get(i).getId());
                product.setCnt(basket.getProductList().get(i).getCnt());
                basket.getProductList().set(i, product);

                productsId[i] = basket.getProductList().get(i).getId();
            }

            session.setAttribute("basket", basket);

            return Response.ok(productService.getPackageProducts(productsId));
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/synchronizeWatchHistory", method = RequestMethod.POST)
    @ResponseBody
    public Response synchronizeWatchHistory(@RequestBody WatchHistoryStorage watchHistoryStorage, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(true);
        WatchHistory watchHistory = Utils.getCurrentWatchHistory(request, session);
        watchHistoryStorage.setNote("sync from Server Session Store");

     //   if (watchHistory == null) {

           if (Utils.isAuthentication()) {
               User user = tokenAuthService.getCurrentUser();

                watchHistory = new WatchHistory(watchService.findAllWatchProductByUser(user));
                watchHistoryStorage.setNote("sync from Server DBA Store");
            } else {
               //Если история в сессии пустая,
               // тогда берем ее из локалсторейджа клиента, который он передал
               if (watchHistory == null) {
                   watchHistoryStorage.setNote("sync from Client");
                   watchHistory = watchHistoryStorage.getWatchHistory();
               }
            }
    //    }
        session.setAttribute("watchHistory", watchHistory);
        watchHistoryStorage.setWatchHistory(watchHistory);

        return Response.ok(watchHistoryStorage);
    }

    @PostMapping("/addToFavoriteList")
    @ResponseBody
    public Response addToFavoriteList(@RequestBody String id){
        try {
            CustomUserDetails user = tokenAuthService.getPrincipal();
            Product product = productService.find(Long.parseLong(id));
            if (product == null)
                throw new CheckException("Отсутствует продукт");

            FavoriteProduct favoriteProduct = new FavoriteProduct(product, user.getUser());
            favoriteService.save(favoriteProduct);
            return getFavoriteList();
        } catch (CheckException e) {
            return Response.error(e.getMessage());
        } catch (Exception e){
            return Response.error("Не удалось добавить в Избранное");
        }
    }

    @GetMapping("/getFavoriteList")
    @ResponseBody
    public Response getFavoriteList(){
        try {
            CustomUserDetails user = tokenAuthService.getPrincipal();
            return Response.ok(favoriteService.getFavoriteListByUserId(user.getUser().getId()));
        } catch (CheckException e) {
            return Response.error(e.getMessage());
        } catch (Exception e){
            return Response.error("Не удалось получить список Избранное");
        }
    }

    @RequestMapping(value = "/synchronizeCompare", method = RequestMethod.POST)
    @ResponseBody
    public ComplexCompareStorage synchronizeCompare(@RequestBody ComplexCompareStorage complexCompareStorage, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(true);
        CompareProduct compareProduct = Utils.getCurrentCompare(request);
        if (compareProduct == null) {
            complexCompareStorage.setNote("sync from Client");
            compareProduct = complexCompareStorage.getCompareProduct();
            session.setAttribute("compare", compareProduct);
        } else {
            complexCompareStorage.setCompareProduct(compareProduct);
            complexCompareStorage.setNote("sync from Server");
        }
        return complexCompareStorage;
    }


    /*история просмотров*/
    /*Не используется в в3*/
    @RequestMapping(value = "/addToWatchHistory", method = RequestMethod.POST)
    @ResponseBody
    public WatchHistoryStorage addToWatchHistory(@RequestParam(value = "code") String code, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(true);
        WatchHistory watchHistory = Utils.getCurrentWatchHistory(request, session);

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
