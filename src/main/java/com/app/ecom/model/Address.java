package com.app.ecom.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@NoArgsConstructor
@Data
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String street;
  private String city;
  private String state;
  private String country;
  private String zipcode;
}
