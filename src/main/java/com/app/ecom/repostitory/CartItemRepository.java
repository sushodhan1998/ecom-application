package com.app.ecom.repostitory;

import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  CartItem findByUserAndProduct(User user, Product product);

  List<CartItem> user(User user);

  void deleteByUserAndProduct(User user, Product product);

  List<CartItem> findByUser(User user);
}
