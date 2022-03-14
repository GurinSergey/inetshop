package com.webstore.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.webstore.core.base.WatchHistoryStorage;
import com.webstore.core.base.jsonView.View;
import com.webstore.core.entities.Comment;
import com.webstore.core.entities.Product;
import com.webstore.core.entities.ProductGroup;
import com.webstore.core.entities.User;
import com.webstore.core.entities.enums.SortProductCriteria;
import com.webstore.core.service.CatalogService;
import com.webstore.core.service.CommentService;
import com.webstore.core.service.ProductGroupService;
import com.webstore.core.service.ProductService;
import com.webstore.core.service.impl.IMarkService;
import com.webstore.core.session.attribute.Basket;
import com.webstore.core.session.attribute.CompareProduct;
import com.webstore.core.session.attribute.WatchHistory;
import com.webstore.core.util.Utils;
import com.webstore.security.services.TokenAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.webstore.core.util.Const.COUNT_COMMENTS_LOAD;
import static com.webstore.core.util.Utils.*;

/**
 * Created by DVaschenko on 12.07.2016.
 */
@Controller
@SessionAttributes(value = "basket", types = Basket.class)
public class MainController {

    @Autowired private CatalogService catalogService;

    @Autowired private ProductService productService;

    @Autowired private IMarkService markService;

    @Autowired private CommentService commentService;

    @Autowired private ProductGroupService productGroupService;

    @Autowired private TokenAuthService tokenAuthService;

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

        User user = tokenAuthService.getPrincipal();
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

    @RequestMapping(value = "/item/{code}", method = RequestMethod.GET)
    public String showItem(Model model, @PathVariable String code, HttpServletRequest request) {
        Product product = productService.findByCode(code);
        productService.sortOwnerFields(product);
        User user = tokenAuthService.getPrincipal();
        List<Comment> comments = commentService.getComments(0, product.getId());

        if (user != null) productService.setTickIsChosen(user.getId(), product);

        model.addAttribute("item", product);
        model.addAttribute("listComments", getJsonString(comments, View.ForCommentView.class));
        model.addAttribute("limit", COUNT_COMMENTS_LOAD);
        model.addAttribute("limitMax", comments.size() >= COUNT_COMMENTS_LOAD);
        model.addAttribute("cntComments", product.getComments().size());
        model.addAttribute("breadCrumbs", Utils.makeBreadCrumbles(catalogService, product.getId()));




        //LAO
        HttpSession session = request.getSession(true);
        WatchHistory watchHistory = getCurrentWatchHistory(request);
        if (watchHistory == null) {
            watchHistory = new WatchHistory();
            session.setAttribute("watchHistory", watchHistory);
        }
        watchHistory.addProduct(product);
        WatchHistoryStorage watchHistoryStorage = new WatchHistoryStorage();
        watchHistoryStorage.setWatchHistory(watchHistory);
        watchHistoryStorage.setNote("sync from Server");
     //   model.addAttribute("watchHistoryModel",getJsonString(watchHistoryStorage));
        /***********************************/



        model = fillFullAttributes(model, request);

        return "product_v1";
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