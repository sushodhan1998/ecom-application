package com.app.ecom.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
public class OrderItemsDto {
  private Long id;
  private Integer quantity;
  private Long productId;
  private BigDecimal price;
  private BigDecimal subTotal;
}
