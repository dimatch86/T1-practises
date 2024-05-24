package com.t1.loggableapplication.controller;

import com.t1.loggableapplication.model.Product;
import com.t1.loggableapplication.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/addProduct")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(product));
    }

    @PostMapping("/addProducts")
    public ResponseEntity<String> addProducts(@RequestBody List<Product> products) {
        productService.addProducts(products);
        return ResponseEntity.ok("Products are added");
    }

    @GetMapping("/getProduct")
    public ResponseEntity<Product> getProductByName(@RequestParam(name = "name", required = false) String name) {
        return ResponseEntity.ok(productService.getProductByName(name));
    }

    @GetMapping("/getProducts")
    public ResponseEntity<List<Product>> getProductByPrice(@RequestParam(name = "price", required = false) Integer price) {
        return ResponseEntity.ok(productService.getProductsByPriceGreaterThan(price));
    }

    @DeleteMapping("/getProduct")
    public ResponseEntity<Void> deleteProductByName(@RequestParam(name = "name", required = false) String name) {
        productService.deleteByName(name);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
