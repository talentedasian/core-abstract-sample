package com.example.demo.controller;

import com.example.demo.demo.StockService;
import com.example.demo.dto.out.StockDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class StockController {

  @GetMapping(path = "stock")
  public ResponseEntity<StockDTO> totalStockOnShop() {

    return ResponseEntity.ok(new StockService().retrieveStock());

  }

}
