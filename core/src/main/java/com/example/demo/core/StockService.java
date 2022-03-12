package com.example.demo.core;

import com.example.demo.dto.out.Stock;
import com.example.demo.dto.out.Stock.StockState;
import org.springframework.stereotype.Service;

@Service
public class StockService {

  private final StockEntityRepository stockRepo;

  public StockService(StockEntityRepository stockRepo) {
    this.stockRepo = stockRepo;
  }

  public Stock retrieveStock() {
    StockEntity stock = stockRepo.retrieveStock();

    if (stock.isFull()) {
      return new Stock(StockState.FULL);
    }

    var nonFullStock = stock.isEmpty() ? StockState.EMPTY : StockState.SOME;
    return new Stock(nonFullStock);
  }

}
