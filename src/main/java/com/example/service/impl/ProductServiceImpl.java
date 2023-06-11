package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.exception.ProductNotFound;
import com.example.exception.ServerException;
import com.example.model.Product;
import com.example.model.QueryParam;
import com.example.repo.ProductRepo;
import com.example.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Override
    public List<Product> getProduct(List<QueryParam> queryParams) {
        List<Product> products = new ArrayList<>();

        for (QueryParam queryParam : queryParams) {

            if (queryParam.getQuery().equals("productId")) {
                int productId = Integer.parseInt(queryParam.getValue());

                Product p = getProductById(productId);
                if (!Objects.isNull(p)) {
                    products.add(p);
                } else {
                    throw new ProductNotFound(
                            String.format("Product not found for product id %s", queryParam.getValue()));
                }
            }

            if (queryParam.getQuery().equals("category")) {
                String category = queryParam.getValue();
                List<Product> pList = getProductsByCategory(category);
                products.addAll(pList);
            }

        }

        return products;
    }

    public Product getProductById(int productId) {
        try {
            return productRepo.findById(productId).orElse(null);
        } catch (Exception exception) {
            throw new ServerException("Server error, Please try after sometime");
        }
    }

    public List<Product> getProductsByCategory(String category) {
        try {
            return productRepo.findByCategory(category);
        } catch (Exception exception) {
            throw new ServerException("Server error, Please try after sometime");
        }
    }

}
