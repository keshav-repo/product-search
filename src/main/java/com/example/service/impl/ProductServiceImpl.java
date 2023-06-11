package com.example.service.impl;

import com.example.exception.ServerException;
import com.example.model.Product;
import com.example.model.QueryParam;
import com.example.repo.ProductRepo;
import com.example.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Product> getProduct(List<QueryParam> queryParams) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        List<Predicate> predicateList = new ArrayList<>();

        for (QueryParam queryParam : queryParams) {
            if (queryParam.getQuery().equals("productId")) {
                int productId = Integer.parseInt(queryParam.getValue());
                predicateList.add(builder.equal(root.get("productId"), productId));
            } else if (queryParam.getQuery().equals("category")) {
                String category = queryParam.getValue();
                predicateList.add(builder.equal(root.get("category"), category));
            } else if (queryParam.getQuery().equals("minPrice")) {
                double minPrice = Double.parseDouble(queryParam.getValue());
                predicateList.add(builder.greaterThanOrEqualTo(root.get("price"), minPrice));
            } else if (queryParam.getQuery().equals("maxPrice")) {
                double maxPrice = Double.parseDouble(queryParam.getValue());
                predicateList.add(builder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
        }

        try {
            return entityManager.createQuery(query.select(root).where(predicateList.toArray(new Predicate[0])))
                    .getResultList();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new ServerException("Server error, Please try after sometime");
        }

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
