package com.app.ecom.service;

import com.app.ecom.dto.CartItemResponse;
import com.app.ecom.dto.OrderItemsDto;
import com.app.ecom.dto.OrderResponse;
import com.app.ecom.model.Order;
import com.app.ecom.model.OrderItem;
import com.app.ecom.model.OrderStatus;
import com.app.ecom.model.User;
import com.app.ecom.repostitory.CartItemRepository;
import com.app.ecom.repostitory.OrderRepository;
import com.app.ecom.repostitory.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

  private final CartService cartService;
  private final UserRepository userRepository;
  private final OrderRepository orderRepository;
  private final CartItemRepository cartItemRepository;

  public Optional<OrderResponse> createOrder( String userId ) {

    Optional<User> user = userRepository.findById( Long.valueOf( userId ) );
    if(user.isEmpty()) {
      return Optional.empty();
    }
    BigDecimal totalPrice = cartService.getCartItems( userId ).stream()
      .map( CartItemResponse::getPrice )
      .reduce( BigDecimal.ZERO, BigDecimal::add );

    if (totalPrice.compareTo( BigDecimal.ZERO ) == 0) {
     return Optional.empty();
    }
    Order order = new Order();
    order.setUser( user.get() );
    order.setTotalAmount( totalPrice );
    order.setStatus( OrderStatus.PAID );
    List<OrderItem> orderItems = cartService.getCartItems( userId )
      .stream()
      .map( cartItemResponse ->
        new OrderItem(
          null,
          order,
          cartItemResponse.getProduct(),
          cartItemResponse.getQuantity(),
          cartItemResponse.getPrice()
        ) )
      .toList();
    order.setOrderItems( orderItems );
    orderRepository.save( order );

    orderItems.forEach(
      orderItem -> cartItemRepository.deleteByUserAndProduct( user.get(), orderItem.getProduct() )
    );
    return Optional.of(mapToOrderResponse( order ));
  }

  private OrderResponse mapToOrderResponse( Order order ) {
    OrderResponse response = new OrderResponse();
    response.setId( order.getId() );
    response.setOrderItems( order.getOrderItems()
      .stream()
      .map( this::mapToOrderItemsDto )
      .toList() );
    response.setTotalAmount(
      response.getOrderItems().stream()
        .map( OrderItemsDto::getSubTotal )
        .reduce( BigDecimal.ZERO, BigDecimal::add )
    );
    response.setStatus( order.getStatus() );
    response.setCreatedAt( order.getCreatedAt() );
    response.setUpdatedAt( order.getUpdatedAt() );
    return response;
  }

  private OrderItemsDto mapToOrderItemsDto( OrderItem orderItem ) {
    OrderItemsDto dto = new OrderItemsDto();
    dto.setId( orderItem.getId() );
    dto.setProductId( orderItem.getProduct().getId() );
    dto.setQuantity( orderItem.getQuantity() );
    dto.setPrice( orderItem.getPrice() );
    dto.setSubTotal( orderItem.getPrice().multiply( BigDecimal.valueOf( orderItem.getQuantity() ) ) );
    return dto;
  }
}
