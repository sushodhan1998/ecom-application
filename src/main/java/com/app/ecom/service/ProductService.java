package com.app.ecom.service;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.model.Product;
import com.app.ecom.repostitory.ProductRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public ProductResponse createProduct( ProductRequest productRequest ) {
    Product product = new Product();

    Product newProduct = productRepository.save( updateProductRequestToProduct( product, productRequest ) );
    return mapToProductResponse( newProduct );
  }

  private ProductResponse mapToProductResponse( Product newProduct ) {
    ProductResponse productResponse = new ProductResponse();
    productResponse.setId( newProduct.getId() );
    productResponse.setName( newProduct.getName() );
    productResponse.setActive( newProduct.getActive() );
    productResponse.setDescription( newProduct.getDescription() );
    productResponse.setCategory( newProduct.getCategory() );
    productResponse.setPrice( newProduct.getPrice() );
    productResponse.setImageUrl( newProduct.getImageUrl() );
    productResponse.setStockQuantity( newProduct.getStockQuantity() );
    return productResponse;
  }

  private Product updateProductRequestToProduct( Product product, ProductRequest productRequest ) {
    product.setName( productRequest.getName() );
    product.setDescription( productRequest.getDescription() );
    product.setCategory( productRequest.getCategory() );
    product.setPrice( productRequest.getPrice() );
    product.setImageUrl( productRequest.getImageUrl() );
    product.setStockQuantity( productRequest.getStockQuantity() );
    return product;
  }

  public Optional<ProductResponse> updateProduct( ProductRequest productRequest, Long productId ) {
    return productRepository.findById( productId )
      .map( existingProduct -> {
        updateProductRequestToProduct( existingProduct, productRequest );
        Product updatedProduct = productRepository.save( existingProduct );
        return mapToProductResponse( updatedProduct );
      } );
  }

  public List<ProductResponse> getProducts() {
    return productRepository.findByActiveTrue()
      .stream()
      .map( this::mapToProductResponse )
      .toList();

  }

  public boolean deleteProduct( Long productId ) {
    return productRepository.findById( productId )
      .map( product -> {
        product.setActive( false );
        productRepository.save( product );
        return true;
      } ).orElse( false );
  }

  public List<ProductResponse> searchProduct( String keyword ) {
    return productRepository.searchProduct(keyword)
      .stream()
      .map( this::mapToProductResponse)
      .toList();
  }
}
