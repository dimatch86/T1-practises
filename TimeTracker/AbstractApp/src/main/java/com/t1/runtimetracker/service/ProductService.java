package com.t1.runtimetracker.service;

import com.t1.runtimetracker.model.Product;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ProductService {

    Object addProduct(Product product) throws ExecutionException, InterruptedException;
    Object addProducts(List<Product> products);
    Product getProductByName(String name);
    List<Product> getProductsByPriceGreaterThan(Integer price);
}
