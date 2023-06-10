package com.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.exception.ServerException;
import com.example.model.Product;
import com.example.repo.ProductRepo;
import com.example.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepo productRepo;

    @Override
    public List<Product> getProduct() {
        try{
          List<Product> products = productRepo.findAll();
          return products;
        }catch(Exception exception){
            throw new ServerException("some exception from server");
        }
    }
}
