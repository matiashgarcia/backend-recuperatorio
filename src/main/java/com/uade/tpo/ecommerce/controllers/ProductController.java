package com.uade.tpo.ecommerce.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.ecommerce.entity.Product;
import com.uade.tpo.ecommerce.entity.dto.ProductResponse;
import com.uade.tpo.ecommerce.service.IProductService;
import com.uade.tpo.ecommerce.service.ProductService;

@RestController
@CrossOrigin
@RequestMapping("products")
public class ProductController {

  @Autowired
  private IProductService service;

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
    Optional<Product> result = service.getById(id);
    if (result.isPresent())
      return ResponseEntity.ok(ProductService.productResponseBuilder(result.get()));
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/categoryid/{id}")
  public ResponseEntity<List<ProductResponse>> getProductsByCategoryId(@PathVariable String id) {
    List<ProductResponse> listResult = service.getByCategoryId(id).stream()
        .map(a -> {
          ProductResponse p = ProductService.productResponseBuilder(a);
          p.setDescription(null);
          return p;
        })
        .toList();
    return ResponseEntity.ok(listResult);
  }

  @GetMapping("/search")
  public ResponseEntity<List<ProductResponse>> getProductsByTitle(@RequestParam(required = true) String search) {
    List<ProductResponse> listResult = service.getAllBySearch(search).stream()
        .map(a -> {
          ProductResponse p = ProductService.productResponseBuilder(a);
          p.setDescription(null);
          return p;
        })
        .toList();
    return ResponseEntity.ok(listResult);
  }

  @GetMapping("/cart-products")
  public ResponseEntity<List<ProductResponse>> getCartProducts(@RequestParam(required = true) String productIds) {
    List<Product> productList = service.getCartProducts(productIds.split(","));
    List<ProductResponse> listResult = productList.stream()
        .map(a -> {
          ProductResponse p = ProductService.productResponseBuilder(a);
          p.setDescription(null);
          return p;
        })
        .toList();
    return ResponseEntity.ok(listResult);
  }
}
