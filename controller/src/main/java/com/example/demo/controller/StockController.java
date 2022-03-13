package com.example.demo.controller;

import com.example.demo.core.StockFacade;
import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.out.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
public class StockController {

  private final StockFacade stockFacade;

  @GetMapping(path = "stock")
  public ResponseEntity<Stock> totalStockOnShop(ShoeFilter shoeFilter, @RequestHeader int version) {

    return ResponseEntity.ok(stockFacade.get(version).globalStock(shoeFilter));

  }

}
