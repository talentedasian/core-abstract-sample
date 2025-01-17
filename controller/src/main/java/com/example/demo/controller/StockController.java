package com.example.demo.controller;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeToStock;
import com.example.demo.dto.in.ShoeToUpdate;
import com.example.demo.dto.out.Stock;
import com.example.demo.facade.ShoeFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
public class StockController {

  private final ShoeFacade.StockFacade stockFacade;

  @GetMapping(path = "stock")
  public ResponseEntity<Stock> totalStockOnShop(ShoeFilter shoeFilter, @RequestHeader int version) {

    return ResponseEntity.ok(stockFacade.get(version).globalStock(shoeFilter));

  }

  @PostMapping(path = "stock")
  public ResponseEntity<Stock> addShoeToGlobalStock(@Valid @RequestBody ShoeToStock shoe, @RequestHeader int version) {

    return ResponseEntity.status(201).body(stockFacade.get(version).addShoeToStock(shoe));

  }

  @PatchMapping(path = "stock")
  public ResponseEntity<Stock> updateShoeAvailableStock(@Valid @RequestBody ShoeToUpdate shoe, @RequestHeader int version) {

    return ResponseEntity.ok(stockFacade.get(version).updateShoeQuantity(shoe));

  }

  @PatchMapping(path = "stocks")
  public ResponseEntity<Stock> updateShoesAvailableStock(@Valid @RequestBody List<ShoeToUpdate> shoe) {

    return ResponseEntity.ok(stockFacade.get(2).updateShoesQuantity(shoe));

  }

}
