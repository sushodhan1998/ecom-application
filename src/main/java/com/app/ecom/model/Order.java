package com.app.ecom.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity( name = "orders" )
@Data
@NoArgsConstructor
public class Order {
  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  private Long id;
  @ManyToOne
  @JoinColumn( name = "user_id", nullable = false )
  private User user;
  @Enumerated( EnumType.STRING )
  private OrderStatus status = OrderStatus.PENDING;
  private BigDecimal totalAmount;
  @OneToMany( mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true )
  private List<OrderItem> orderItems = new ArrayList<>();
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;

}
