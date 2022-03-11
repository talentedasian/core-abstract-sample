package com.example.demo.demo;

import com.example.demo.dto.out.StockDTO;

public class StockService {

  public StockDTO retrieveStock() {
    Stock stock = Stock.zero();

    if (stock.isFull()) {
      return new StockDTO("FULL");
    }

    String nonFullStock = stock.isEmpty() ? "EMPTY" : "SOME";
    return new StockDTO(nonFullStock);
  }

}
