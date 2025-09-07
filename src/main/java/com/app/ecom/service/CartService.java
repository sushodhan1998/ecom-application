package com.app.ecom.service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.dto.CartItemResponse;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.model.User;
import com.app.ecom.repostitory.CartItemRepository;
import com.app.ecom.repostitory.ProductRepository;
import com.app.ecom.repostitory.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

  private final CartItemRepository cartItemRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;

  public Boolean addToCart( String userId, CartItemRequest request ) {

    userRepository.findById( Long.valueOf( userId ) )
      .orElseThrow( () -> new RuntimeException( "User not found" ) );

    Optional<User> userOpt = userRepository.findById( Long.valueOf( userId ) );
    if (userOpt.isEmpty()) {
      return false;
    }
    User user = userOpt.get();

    Optional<Product> productOpt = productRepository.findById( request.getProductId() );
    if (productOpt.isEmpty()) {
      return false;
    }
    Product product = productOpt.get();

    if (request.getQuantity() > product.getStockQuantity()) {
      return false;
    }

    CartItem existingCartItem = cartItemRepository.findByUserAndProduct( user, product );
    if (existingCartItem != null) {
      existingCartItem.setQuantity(
        existingCartItem.getQuantity() + request.getQuantity()
      );
      existingCartItem.setPrice(
        BigDecimal.valueOf( existingCartItem.getQuantity() )
          .multiply( existingCartItem.getProduct().getPrice() )
      );
      cartItemRepository.save( existingCartItem );
    } else {
      CartItem cartItem = new CartItem();
      cartItem.setUser( user );
      cartItem.setProduct( product );
      cartItem.setQuantity( request.getQuantity() );
      cartItem.setPrice(
        BigDecimal.valueOf( request.getQuantity() )
          .multiply( product.getPrice() )
      );
      cartItemRepository.save( cartItem );
    }
    return true;
  }

  public Boolean deleteCartItem( String userId, Long productId ) {
    Optional<User> userOpt = userRepository.findById( Long.valueOf( userId ) );
    Optional<Product> productOpt = productRepository.findById( productId );

    if (userOpt.isEmpty() || productOpt.isEmpty()) {
      return false;
    }
    cartItemRepository.deleteByUserAndProduct( userOpt.get(), productOpt.get() );
    return true;
  }

  public List<CartItemResponse> getCartItems( String userId ) {
    return userRepository.findById( Long.valueOf( userId ) )
      .map( user ->
        cartItemRepository.findByUser( user )
          .stream()
          .map( this::mapToCartItemResponse )
          .toList()
      )
      .orElseThrow( () -> new RuntimeException( "User not found" ) );
  }

  private CartItemResponse mapToCartItemResponse( CartItem cartItem ) {
    return new CartItemResponse(
      cartItem.getId(),
      cartItem.getUser(),
      cartItem.getProduct(),
      cartItem.getQuantity(),
      cartItem.getPrice(),
      cartItem.getCreatedAt(),
      cartItem.getUpdatedAt()
    );
  }
}
