package com.example.demo.controller;

import com.example.demo.core.StockFacade;
import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeToStock;
import com.example.demo.dto.out.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
public class StockController {

  private final StockFacade stockFacade;

  @GetMapping(path = "stock")
  public ResponseEntity<Stock> totalStockOnShop(ShoeFilter shoeFilter, @RequestHeader int version) {

    return ResponseEntity.ok(stockFacade.get(version).globalStock(shoeFilter));

  }

  @PatchMapping(path = "stock")
  public ResponseEntity<Stock> addShoeToGlobalStock(@Valid @RequestBody ShoeToStock shoe) {
    return ResponseEntity.ok(stockFacade.get(1).addShoeToStock(shoe));
  }

}
