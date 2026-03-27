package com.app.ecom.service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.model.CartItem;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

  private final CartItemRepository cartItemRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;

  public boolean addToCart(
    String userId,
    CartItemRequest cartItemRequest
  ) {
    return userRepository.findById(Long.valueOf(userId))
      .map(user -> {
        return productRepository.findById(cartItemRequest.getProductId())
          .map(
            product -> {
              if (product.getStockQuantity() < cartItemRequest.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
              }
              cartItemRepository.findByUserAndProduct(user, product)
                .ifPresentOrElse(
                  existingCartItem -> {
                    existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
                    existingCartItem.setPrice(
                      product.getPrice()
                        .multiply(BigDecimal.valueOf(existingCartItem.getQuantity()))
                    );
                    cartItemRepository.save(existingCartItem);
                  }, () -> {
                    CartItem cartItem = CartItem.builder()
                      .user(user)
                      .product(product)
                      .quantity(cartItemRequest.getQuantity())
                      .price(product.getPrice().multiply(BigDecimal.valueOf(cartItemRequest.getQuantity())))
                      .build();
                    cartItemRepository.save(cartItem);
                  }
                );
              return true;
            }
          ).orElseThrow(() -> new RuntimeException("Product not found with id: " + cartItemRequest.getProductId()));
      }).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
  }

  public boolean removeFromCart(String userId, Long productId) {
    return userRepository.findById(Long.valueOf(userId))
      .map(user -> productRepository.findById(productId)
        .map(product -> {
          cartItemRepository.deleteByUserAndProduct(user, product);
          return true;
        })
        .orElse(false)
      )
      .orElse(false);
  }

  public List<CartItem> getCartItems(String userId) {
    return userRepository.findById(Long.valueOf(userId))
      .map(cartItemRepository::findByUser)
      .orElseGet(List::of);
  }
}
