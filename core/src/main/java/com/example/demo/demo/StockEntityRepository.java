package com.example.demo.demo;

public interface StockEntityRepository {

  void saveStock(StockEntity stockEntity);

  StockEntity retrieveStock();

}
