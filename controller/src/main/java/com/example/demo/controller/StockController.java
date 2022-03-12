package com.example.demo.controller;

import com.example.demo.core.StockService;
import com.example.demo.dto.out.Stock;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class StockController {

  private final StockService stockService;

  public StockController(StockService stockService) {
    this.stockService = stockService;
  }

  @GetMapping(path = "stock")
  public ResponseEntity<Stock> totalStockOnShop() {

    return ResponseEntity.ok(stockService.retrieveStock());

  }

}
