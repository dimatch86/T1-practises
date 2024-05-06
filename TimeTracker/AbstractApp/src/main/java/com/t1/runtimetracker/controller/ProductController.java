package com.t1.runtimetracker.controller;

import com.t1.runtimetracker.model.Product;
import com.t1.runtimetracker.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app")
public class ProductController {
    private final ProductServiceImpl productService;

    @PostMapping("/addProduct")
    public void addProduct(@RequestBody Product product) {
        productService.addProduct(product);
    }

    @PostMapping("/addProducts")
    public void addProducts(@RequestBody List<Product> products) {
        productService.addProducts(products);
    }

    @GetMapping("/getProduct")
    public Product getProductByName(@RequestParam(name = "name", required = false) String name) {
        return productService.getProductByName(name);
    }

    @GetMapping("/getProducts")
    public List<Product> getProductByPrice(@RequestParam(name = "price", required = false) Integer price) {
        return productService.getProductsByPriceGreaterThan(price);
    }


}
