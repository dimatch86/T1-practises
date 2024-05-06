package com.t1.runtimetracker.service;

import com.t1.runtimetracker.aop.annotation.TrackAsyncTime;
import com.t1.runtimetracker.aop.annotation.TrackTime;
import com.t1.runtimetracker.exception.MissedParameterException;
import com.t1.runtimetracker.exception.ProductException;
import com.t1.runtimetracker.model.Product;
import com.t1.runtimetracker.util.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final HashMap<String, Product> products = new HashMap<>();

    @TrackAsyncTime
    @Override
    public Object addProduct(Product product) {
        return CompletableFuture.supplyAsync(() -> {
            ThreadUtils.waitRandomTime();
            products.put(product.getName(), product);
            return product;
        });
    }

    @TrackAsyncTime
    @Override
    public Object addProducts(List<Product> list) throws ProductException {
        return CompletableFuture.runAsync(() -> {
            if (list.size() == 1) {
                throw new ProductException("Используйте метод addProduct");
            }
            ThreadUtils.waitRandomTime();
            products.putAll(list.stream()
                    .collect(Collectors.toMap(Product::getName, Function.identity())));
        });
    }

    @TrackTime
    @Override
    public Product getProductByName(String name) throws MissedParameterException {
        if (name == null || name.isEmpty()) {
            throw new MissedParameterException("Параметр должен присутствовать в запросе");
        }
        return products.get(name);
    }

    @TrackTime
    @Override
    public List<Product> getProductsByPriceGreaterThan(Integer price) throws MissedParameterException {
        if (price == null) {
            throw new MissedParameterException("Параметр должен присутствовать в запросе");
        }
        return products.values().stream()
                .filter(product -> product.getPrice() > price)
                .toList();
    }
}
