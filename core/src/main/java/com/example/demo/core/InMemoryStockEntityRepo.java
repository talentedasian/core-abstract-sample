package com.example.demo.core;

import org.springframework.stereotype.Component;

/**
 * In memory implementation -- mostly used for tests -- for
 * a {@link StockEntityRepository}.
 */
@Component
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
