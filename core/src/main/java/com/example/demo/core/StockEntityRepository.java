package com.example.demo.core;

public interface StockEntityRepository {

  void saveStock(StockEntity stockEntity);

  StockEntity retrieveStock();

}
