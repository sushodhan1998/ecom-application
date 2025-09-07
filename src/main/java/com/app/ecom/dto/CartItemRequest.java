package com.app.ecom.dto;

import lombok.Data;

@Data
public class CartItemRequest {
  public Long productId;
  public Integer quantity;
}
