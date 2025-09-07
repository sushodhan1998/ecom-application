package com.app.ecom.dto;

import com.app.ecom.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
  private Long id;
  private BigDecimal totalAmount;
  private OrderStatus status;
  private List<OrderItemsDto> orderItems;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
