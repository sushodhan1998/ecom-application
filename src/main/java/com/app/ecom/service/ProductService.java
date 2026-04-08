package com.app.ecom.service;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.model.Product;
import com.app.ecom.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public ProductResponse createProduct(ProductRequest productRequest) {
    Product product = new Product();
    updateProductFromRequest(product, productRequest);
    return mapToProductResponse(productRepository.save(product));
  }

  public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
    return productRepository.findById(id)
      .map(existingProduct -> {
        updateProductFromRequest(existingProduct, productRequest);
        productRepository.save(existingProduct);
        return mapToProductResponse(existingProduct);
      });
  }

  public List<ProductResponse> getAllProducts() {
    return productRepository.findByActiveTrue()
      .stream()
      .map(this::mapToProductResponse)
      .toList();
  }

  public Boolean deleteProduct(Long id) {
    return productRepository.findById(id)
      .map(product -> {
        product.setActive(false);
        productRepository.save(product);
        return true;
      }).orElse(false);
  }

  private ProductResponse mapToProductResponse(Product product) {
    ProductResponse productResponse = new ProductResponse();
    productResponse.setId(String.valueOf(product.getId()));
    productResponse.setName(product.getName());
    productResponse.setPrice(product.getPrice());
    productResponse.setDescription(product.getDescription());
    productResponse.setCategory(product.getCategory());
    productResponse.setImageUrl(product.getImageUrl());
    productResponse.setStockQuantity(product.getStockQuantity());
    productResponse.setActive(product.isActive());
    return productResponse;
  }

  private void updateProductFromRequest(Product product, ProductRequest productRequest) {
    product.setName(productRequest.getName());
    product.setPrice(productRequest.getPrice());
    product.setDescription(productRequest.getDescription());
    product.setCategory(productRequest.getCategory());
    product.setImageUrl(productRequest.getImageUrl());
    product.setStockQuantity(productRequest.getStockQuantity());
  }

  public List<ProductResponse> searchProducts(String keyword) {
    return productRepository.searchProductsBy(keyword)
      .stream()
      .map(this::mapToProductResponse)
      .toList();
  }
}
