package com.webstore.web.controller;

import com.webstore.core.base.ComplexBasketStorage;
import com.webstore.core.dao.impl.IDeliveryDao;
import com.webstore.core.entities.Delivery;
import com.webstore.core.entities.Product;
import com.webstore.core.service.ProductService;
import com.webstore.core.session.attribute.Basket;
import com.webstore.core.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static com.webstore.core.util.Utils.getCurrentBasket;

/**
 * Created by DVaschenko on 19.07.2016.
 */
@RestController
@RequestMapping(value = "/basket")
//@SessionAttributes(value = "basket", types = Basket.class)
public class BasketController {

    @Autowired
    private IDeliveryDao deliveryDao;

    @Autowired
    private ProductService productService;




    /*Корзина*/
    @RequestMapping(value = "/addToBasket", method = RequestMethod.POST)
    @ResponseBody
    public ComplexBasketStorage addToBasket(@RequestParam(value = "code") String code, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(true);


        Basket basket = getCurrentBasket(request);

        if (basket == null) {
            basket = new Basket();
            session.setAttribute("basket", basket);

        }

        Product product = productService.findByCode(code);
        basket.addProduct(product);

        ComplexBasketStorage complexBasketStorage = new ComplexBasketStorage(basket, "test for test");


        return complexBasketStorage;
    }

    @RequestMapping(value = "/deleteFromBasket", method = RequestMethod.POST)
    @ResponseBody
    public ComplexBasketStorage deleteFromBasket(@RequestParam(value = "code") String code, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(true);
        Basket basket = getCurrentBasket(request);
        basket.deleteByCode(code);
        if (!basket.getProductList().isEmpty() ){
            session.setAttribute("basket", basket);
        }
        else{
            session.removeAttribute("basket");
        }
        ComplexBasketStorage complexBasketStorage = new ComplexBasketStorage(basket, "synchronize from Server(remove item)");

        return complexBasketStorage;
    }


   /* @RequestMapping(value = {"/basket"}, method = RequestMethod.GET)
    public ModelAndView showBasket(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        model.setViewName("basket");

        Basket basket = Utils.getCurrentBasket(request);

        if (basket != null) {
            model.addObject("listBasketItems", basket.getProductList());
            model.addObject("basketCnt", basket.count());
        }

        List<Delivery> deliveryList = deliveryDao.getListDelivery();
        model.addObject("listCheckOrders", deliveryList);

        return model;
    }

    @RequestMapping(value = {"/popup"}, method = RequestMethod.GET)
    public ModelAndView showPopup(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        model.setViewName("test");

        return model;
    }
    @RequestMapping(value = {"/cube"}, method = RequestMethod.GET)
    public ModelAndView showCube(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        model.setViewName("cube");

        return model;
    }*/
}