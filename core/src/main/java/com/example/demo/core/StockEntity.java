package com.example.demo.core;

public record StockEntity(int quantity) {

  public StockEntity {
    if (quantity < 0){
      throw InvalidStockQuantity.negativeCause();
    }
    if (quantity > 30) {
      throw InvalidStockQuantity.overflowCause();
    }
  }

  public static StockEntity zero() {
    return new StockEntity(0);
  }

  public static StockEntity ofQuantity(int quantity) {
    return new StockEntity(quantity);
  }

  public boolean isEmpty() {
    return quantity == 0;
  }

  public boolean isFull() {
    return quantity == 30;
  }
}
