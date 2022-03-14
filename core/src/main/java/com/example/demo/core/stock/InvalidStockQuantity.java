package com.example.demo.core.stock;

public class InvalidStockQuantity extends RuntimeException {

  InvalidStockQuantity(String message) {
    super(message);
  }

  public static InvalidStockQuantity negativeCause() {
    return new InvalidStockQuantity("Stock quantities are cannot be less than 0");
  }

  public static InvalidStockQuantity overflowCause() {
    return new InvalidStockQuantity("Stock quantities cannot go over 30");
  }
}
