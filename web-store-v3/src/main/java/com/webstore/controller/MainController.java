package com.webstore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.webstore.base.WatchHistoryStorage;
import com.webstore.base.jsonView.View;
import com.webstore.domain.Comment;
import com.webstore.domain.Product;
import com.webstore.domain.ProductGroup;
import com.webstore.domain.User;
import com.webstore.domain.enums.SortProductCriteria;
import com.webstore.security.services.TokenAuthService;
import com.webstore.service.*;
import com.webstore.session.attribute.Basket;
import com.webstore.session.attribute.CompareProduct;
import com.webstore.session.attribute.WatchHistory;
import com.webstore.util.Utils;
import com.webstore.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.webstore.util.Utils.*;

@Controller
@SessionAttributes(value = "basket", types = Basket.class)
public class MainController {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private ProductService productService;

    @Autowired private MarkService markService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ProductGroupService productGroupService;

    @Autowired
    private TokenAuthService tokenAuthService;

    private List<Product> liveSearchData = new ArrayList<>();

    @RequestMapping(value = "/getTags", method = RequestMethod.GET)
    @ResponseBody
    public List<Product> getTags(@RequestParam String productCode) {
        return simulateSearchResult(productCode);
    }

    private List<Product> simulateSearchResult(String productCode) {
        List<Product> result = new ArrayList<>();
        for (Product product : liveSearchData) {
            if (product.getCode().contains(productCode)) {
                result.add(product);
            }
        }
        return result;
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcomePage(
            Model model,
            HttpServletRequest request,
            @RequestParam Map<String, String> allRequestParams) throws JsonProcessingException {

        User user = tokenAuthService.getCurrentUser();

        List<Product> products = productService.getProducts();
        String sort_criteria = allRequestParams.containsKey("sort") ? allRequestParams.get("sort") : "";

        if (!sort_criteria.equals("")) {
            products = getSortedProducts(sort_criteria);
        }
        if (products == null) return "redirect:/";

        if (user != null) productService.setTickIsChosen(user.getId(), products);

        model.addAttribute("listItems", products);
        model.addAttribute("catalogList", getJsonString(catalogService.getCatalogListJson()));
        model.addAttribute("marks", markService.getAllMarks());

        liveSearchData = productService.getProducts();

        model = fillFullAttributes(model, request);

        return "main";
    }

    @RequestMapping(value = {"/contacts"}, method = RequestMethod.GET)
    public String contacts(Model model, HttpServletRequest request) {
        model = fillFullAttributes(model, request);

        return "contacts";
    }

    @RequestMapping(value = {"/testPage" , "/testpage"}, method = RequestMethod.GET)
    public String testPage(Model model, HttpServletRequest request) {
        model = fillFullAttributes(model, request);

        return "testPage";
    }

    private List<Product> getSortedProducts(String sort_criteria) {
        switch (sort_criteria) {
            case "cheap":
                return productService.productSorted(productService.getProducts(), SortProductCriteria.CHEAP);
            case "expensive":
                return productService.productSorted(productService.getProducts(), SortProductCriteria.EXPENSIVE);
            case "new":
                return productService.productSorted(productService.getProducts(), SortProductCriteria.NEW);
            default:
                return null;
        }
    }

    private Model fillFullAttributes(Model model, HttpServletRequest request) {
        List<ProductGroup> productGroupList = productGroupService.getProductGroupList();
        model.addAttribute("groupList", productGroupList);


        Basket basket = Utils.getCurrentBasket(request);

        if (basket != null) {
            model.addAttribute("basketCnt", basket.count());
        }

        CompareProduct compareProduct = getCurrentCompare(request);

        if (compareProduct != null) {
            model.addAttribute("compareCnt", compareProduct.count());
        }

        return model;
    }

}