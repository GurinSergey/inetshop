package com.webstore.controller;

import com.webstore.domain.Product;
import com.webstore.domain.ProductPhoto;
import com.webstore.domain.json.Slider;
import com.webstore.repository.ProductRepo;
import com.webstore.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/statistics")
public class StatisticsController {

    @Autowired
    ProductRepo productRepo;

    @GetMapping(value = "/getTheBestSellingProducts")
    @ResponseBody
    public Response getTheBestSellingProducts(@RequestParam(value = "limit") int limit) {
        List<Product> productList = productRepo.getTheBestSellingProducts(new PageRequest(0, limit));
        List<Slider> forSlider = new ArrayList<>();

        for (Product product : productList) {
            Set<ProductPhoto> productPhotos = product.getPhotos();
            ProductPhoto firstProductPhoto = productPhotos.iterator().next();

            forSlider.add(new Slider(product.getLatIdent(), product.getTitle(), product.getDescription(), product.getCode(), firstProductPhoto.getPath()));
        }

        return Response.ok(forSlider);
    }
}
