package com.app.ecom.controller;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.dto.CartItemResponse;
import com.app.ecom.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/api/cart" )
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @PostMapping
  public ResponseEntity<String> addToCart(
    @RequestHeader( "X-User-Id" ) String userId,
    @RequestBody CartItemRequest request
  ) {
    if (Boolean.FALSE.equals( cartService.addToCart( userId, request ) ))
      return ResponseEntity.badRequest().body( "Product is out of stock or Product not found or User not found" );
    return ResponseEntity.status( HttpStatus.CREATED ).build();
  }

  @DeleteMapping( "item/{productId}" )
  public ResponseEntity<Void> removeFromCart(
    @RequestHeader( "X-User-Id" ) String userId,
    @PathVariable Long productId
  ) {
    Boolean isCartItemDeleted = cartService.deleteCartItem( userId, productId );
    return isCartItemDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }

  @GetMapping( "/item" )
  public ResponseEntity<List<CartItemResponse>> getCartItems(
    @RequestHeader( "X-User-Id" ) String userId
  ){
    return ResponseEntity.ok(cartService.getCartItems(userId));
  }
}
