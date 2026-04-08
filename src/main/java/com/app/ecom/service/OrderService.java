package com.app.ecom.service;

import com.app.ecom.dto.OrderItemDTO;
import com.app.ecom.dto.OrderResponse;
import com.app.ecom.model.*;
import com.app.ecom.repository.OrderRepository;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final CartService cartService;
  private final UserService userService;
  private final UserRepository userRepository;
  private final OrderRepository orderRepository;

  public Optional<OrderResponse> createOrder(String userId) {
    //validate cart items
    List<CartItem> cartItems = cartService.getCartItems(userId);
    if(cartItems.isEmpty()) {
      return Optional.empty();
    }
    //validate for user
    Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
    if(userOptional.isEmpty()) {
      return Optional.empty();
    }
    User user = userOptional.get();
    //calculate total price
    BigDecimal totalPrice = cartItems.stream()
      .map(CartItem::getPrice)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
    //create order
    Order order = new Order();
    order.setUser(user);
    order.setStatus(OrderStatus.CONFIRMED);
    order.setTotalAmount(totalPrice);

    List<OrderItem> orderItems = cartItems.stream()
      .map(cartItem -> {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getPrice());
        orderItem.setOrder(order);
        return orderItem;
      }).toList();
    order.setItems(orderItems);
    Order savedOrder = orderRepository.save(order);
    //clear the cart
    cartService.clearCart(userId);
    return Optional.of(mapToOrderResponse(savedOrder));
  }

  private OrderResponse mapToOrderResponse(Order savedOrder) {
    return new OrderResponse(
      savedOrder.getId(),
      savedOrder.getTotalAmount(),
      savedOrder.getStatus(),
      savedOrder.getItems()
        .stream()
        .map(order -> new OrderItemDTO(
          order.getId(),
          order.getProduct().getId(),
          order.getQuantity(),
          order.getPrice(),
          order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()))
        )).toList(),
      savedOrder.getCreatedAt()
    );
  }
}
