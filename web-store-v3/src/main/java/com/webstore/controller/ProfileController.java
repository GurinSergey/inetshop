package com.webstore.controller;

/**
 * Created by DVaschenko on 22.08.2016.
 */
//@Controller
public class ProfileController {

    /*@Autowired
    private ProductService productService;
    @Autowired
    private TokenAuthService tokenAuthService;

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getProfilePage(Model model, HttpServletRequest request){
        User user = tokenAuthService.getPrincipal();
        if (user != null){
            List<Product> products = productService.getChosenProductsByUserId(user.getId());
            productService.setTickIsChosen(user.getId(), products);
            model.addAttribute("chosenProducts", products);

            model = fillFullAttributes(model, request);
        }
        return "profile";
    }

    private Model fillFullAttributes(Model model, HttpServletRequest request) {
        Basket basket = Utils.getCurrentBasket(request);

        if (basket != null) {
            model.addAttribute("listBasketItems", basket.getProductList());
            model.addAttribute("basketCnt", basket.count());
        }

        CompareProduct compareProduct = Utils.getCurrentCompare(request);

        if (compareProduct != null) {
            model.addAttribute("listCompareItems", compareProduct.getProductMap());
            model.addAttribute("compareCnt", compareProduct.count());
        }

        return model;
    }*/
}