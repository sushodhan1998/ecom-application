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
@RequestMapping( "/api/products" )
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping()
  public ResponseEntity<ProductResponse> createProduct( @RequestBody ProductRequest productRequest ) {
    return new ResponseEntity<>( productService.createProduct( productRequest ), HttpStatus.CREATED );
  }

  @GetMapping()
  public ResponseEntity<List<ProductResponse>> getProducts() {
    return ResponseEntity.ok( productService.getProducts() );
  }

  @PutMapping( "/{id}" )
  public ResponseEntity<ProductResponse> updateProduct(
    @PathVariable( name = "id" ) Long productId,
    @RequestBody ProductRequest productRequest ) {
    return productService.updateProduct( productRequest, productId )
      .map( ResponseEntity::ok )
      .orElseGet( () -> ResponseEntity.notFound().build() );
  }

  @DeleteMapping( "/{id}" )
  public ResponseEntity<Void> deleteProduct(
    @PathVariable( name = "id" ) Long productId ) {
    boolean isDeleted = productService.deleteProduct( productId );
    return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }

}
