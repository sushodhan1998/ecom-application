package com.app.ecom.controller;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/api/products" )
public class ProductController {

  private final ProductService productService;

  @PostMapping
  public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
    return new ResponseEntity<>(
      productService.createProduct(productRequest),
      HttpStatus.CREATED
    );
  }

  @GetMapping
  public ResponseEntity<List<ProductResponse>> getProducts() {
    return new ResponseEntity<>(
      productService.getAllProducts(),
      HttpStatus.OK
    );
  }

  @PutMapping( "/{id}" )
  public ResponseEntity<ProductResponse> updateProduct(
    @PathVariable Long id,
    @RequestBody ProductRequest productRequest
  ) {
    return productService.updateProduct(id, productRequest)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping( "/{id}" )
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    Boolean isDeleted = productService.deleteProduct(id);
    return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }

  @GetMapping("/search")
  public ResponseEntity<List<ProductResponse>> searchProduct(@RequestParam String keyword){
    return new ResponseEntity<>(productService.searchProducts(keyword),HttpStatus.OK);
  }
}
