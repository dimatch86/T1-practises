package com.t1.loggableapplication.service;

import com.t1.loggableapplication.exception.MissedParameterException;
import com.t1.loggableapplication.exception.ProductException;
import com.t1.loggableapplication.model.Product;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final HashMap<String, Product> products = new HashMap<>();
    
    public Product addProduct(Product product) {
        products.put(product.getName(), product);
        return product;
    }
    
    public void addProducts(List<Product> list) {
        if (list.size() == 1) {
            throw new ProductException("Используйте метод addProduct");
        }
        products.putAll(list.stream()
                .collect(Collectors.toMap(Product::getName, Function.identity())));
    }
    
    public Product getProductByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new MissedParameterException("Параметр должен присутствовать в запросе");
        }
        Product product = products.get(name);
        if (product == null) {
            throw new ProductException(MessageFormat.format("Товар с названием {0} не найден", name));
        }
        return products.get(name);
    }
    
    public List<Product> getProductsByPriceGreaterThan(Integer price) {
        if (price == null) {
            throw new MissedParameterException("Параметр должен присутствовать в запросе");
        }
        return products.values().stream()
                .filter(product -> product.getPrice() > price)
                .toList();
    }

    public void deleteByName(String name) {
        products.remove(name);
    }
}
