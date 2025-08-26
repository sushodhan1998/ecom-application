package com.app.ecom.repostitory;

import com.app.ecom.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByActiveTrue();
}
