package com.example.service;

import com.example.model.Product;
import com.example.model.QueryParam;

import java.util.List;

public interface ProductService{
    public List<Product> getProduct(List<QueryParam> queryParams);
}