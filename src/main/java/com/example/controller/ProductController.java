package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Product;
import com.example.model.QueryParam;
import com.example.service.ProductService;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> allProducts(@RequestParam(required = false) String id,
                                     @RequestParam(required = false) String category,
                                     @RequestParam(required = false) String minPrice,
                                     @RequestParam(required = false) String maxPrice

    ) {

        List<QueryParam> queryParams = new ArrayList<>();

        if (StringUtils.hasLength(id)) {
            queryParams.add(new QueryParam("productId", id, "String"));
        }
        if (StringUtils.hasLength(category)) {
            queryParams.add(new QueryParam("category", category, "String"));
        }
        if (StringUtils.hasLength(minPrice)) {
            queryParams.add(new QueryParam("minPrice", minPrice, "String"));
        }
        if (StringUtils.hasLength(maxPrice)) {
            queryParams.add(new QueryParam("maxPrice", maxPrice, "String"));
        }

        return productService.getProduct(queryParams);
    }


}
