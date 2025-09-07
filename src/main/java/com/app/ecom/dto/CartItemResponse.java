package com.app.ecom.dto;

import com.app.ecom.model.Product;
import com.app.ecom.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CartItemResponse {

  private Long id;
  private User user;
  private Product product;
  private Integer quantity;
  private BigDecimal price;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

}
