package com.example.demo.dto.out;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Stock {

  private final StockState state;
  private final List<ShoesInStock> shoes = new ArrayList<>();

  public Stock(StockState state, List<ShoesInStock> shoes) {
    this.state = state;
    this.shoes.addAll(shoes);

  }

  public enum StockState {
   EMPTY,
   SOME,
   FULL;
  }

}
