package com.example.demo.dto.out;

import lombok.Getter;

@Getter
public class Stock {

  private final StockState state;

  public Stock(StockState state) {
    this.state = state;
  }

  public enum StockState {
   EMPTY,
   SOME,
   FULL
  }

}
