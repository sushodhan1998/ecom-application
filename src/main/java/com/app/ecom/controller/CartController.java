package com.app.ecom.controller;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.model.CartItem;
import com.app.ecom.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/api/cart" )
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @PostMapping
  public ResponseEntity<Void> addToCart(
    @RequestHeader( "X-User-ID" ) String userId,
    @RequestBody CartItemRequest cartItemRequest
  ) {
    boolean isAddedToCart = cartService.addToCart(userId, cartItemRequest);
    return isAddedToCart ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
  }

  @DeleteMapping("/item/{productId}")
  public ResponseEntity<Void> removeCartItem(
    @RequestHeader( "X-User-ID" ) String userId,
    @PathVariable Long productId
  ) {
    boolean isRemovedFromCart = cartService.removeFromCart(userId, productId);
    return isRemovedFromCart ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
  }

  @GetMapping("item")
  public ResponseEntity<List<CartItem>> getCartItems(
    @RequestHeader( "X-User-ID" ) String userId
  ){
    return ResponseEntity.ok(cartService.getCartItems(userId));
  }

}
