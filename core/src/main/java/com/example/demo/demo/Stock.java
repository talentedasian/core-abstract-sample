package com.example.demo.demo;

public record Stock(int quantity) {

  public Stock {
    if (quantity < 0){
      throw new InvalidStockQuantity();
    }
  }

  public static Stock zero() {
    return new Stock(0);
  }

  public static Stock ofQuantity(int quantity) {
    return new Stock(quantity);
  }

  public boolean isEmpty() {
    return quantity == 0;
  }

  public boolean isFull() {
    return quantity == 30;
  }
}
