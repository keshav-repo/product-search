package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Product;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    
    @GetMapping
    public List<Product> allProducts(){
        List<Product> products =  new ArrayList<>();
        products.add(new Product(1, "Sports Shirt") );
        return products;
    }

}
