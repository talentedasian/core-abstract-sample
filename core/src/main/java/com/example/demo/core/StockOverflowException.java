package com.example.demo.core;

public class StockOverflowException extends RuntimeException {

  private final int totalStock;

  public StockOverflowException(int totalStock) {
    this.totalStock = totalStock;
  }
  public int totalStock() {
    return totalStock;
  }

}
