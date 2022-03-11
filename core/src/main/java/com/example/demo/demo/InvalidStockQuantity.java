package com.example.demo.demo;

public class InvalidStockQuantity extends RuntimeException {

  public InvalidStockQuantity() {
    super("Stock quantities are cannot be less than 0");
  }
}
