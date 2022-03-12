package com.example.demo.demo;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * In memory implementation -- mostly used for tests -- for
 * a {@link StockEntityRepository}.
 */
public class InMemoryStockEntityRepo implements StockEntityRepository {

  StockEntity stockEntity;

  @Override
  public void saveStock(StockEntity stockEntity) {
    this.stockEntity = stockEntity;
  }

  @Override
  public StockEntity retrieveStock() {
    return stockEntity;
  }
}
