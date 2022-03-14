package com.webstore.controller;

import com.webstore.repository.AddressGeneralRepo;
import com.webstore.responses.Response;
import com.webstore.service.AddressGeneralService;
import com.webstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by DVaschenko on 19.07.2016.
 */
@RestController
@RequestMapping(value = "/basket")
public class BasketController {
//    @Autowired
//    private ProductService productService;

    /*Корзина*/
    /*@RequestMapping(value = "/addToBasket", method = RequestMethod.POST)
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
*/

   /* @RequestMapping(value = {"/basket"}, method = RequestMethod.GET)
    public ModelAndView showBasket(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        model.setViewName("basket");

        Basket basket = Utils.getCurrentBasket(request);

        if (basket != null) {
            model.addObject("listBasketItems", basket.getProductList());
            model.addObject("basketCnt", basket.count());
        }

        List<Delivery> deliveryList = deliveryService.getListDelivery();
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