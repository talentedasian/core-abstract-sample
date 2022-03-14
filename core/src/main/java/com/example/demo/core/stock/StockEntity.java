package com.example.demo.core.stock;

public record StockEntity(int quantity) {

  static final int MAX = 30;

  public StockEntity {
    if (quantity < 0){
      throw InvalidStockQuantity.negativeCause();
    }
    if (quantity > MAX) {
      throw new StockOverflowException(quantity);
    }
  }

  public static StockEntity zero() {
    return new StockEntity(0);
  }

  public static StockEntity ofQuantity(int quantity) {
    return new StockEntity(quantity);
  }

  public static boolean isBelowFull(int quantity) {
    return quantity <= MAX;
  }

  public boolean isEmpty() {
    return quantity == 0;
  }

  public boolean isFull() {
    return quantity == MAX;
  }
}
