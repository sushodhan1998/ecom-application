package com.app.ecom.repository;

import com.app.ecom.model.Product;
import com.app.ecom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByActiveTrue();

  @Query("SELECT p FROM Product p" +
    " where p.active = true" +
    " AND p.stockQuantity > 0" +
    " AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
  List<Product> searchProductsBy(@Param("keyword") String keyword);
}
