package com.example.demo.dto.out;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Stock {

  private final StockState state;
  private final List<ShoesInStock> shoes = new ArrayList<>();

  public Stock(int stocksAvailable, List<ShoesInStock> shoes) {
    this.state = StockState.ofQuantity(stocksAvailable);
    this.shoes.addAll(shoes);

  }

  public enum StockState {
   EMPTY,
   SOME,
   FULL;

   public static StockState ofQuantity(int quantity) {
     if (quantity < 30) {
       if (quantity == 0) return EMPTY;

       return SOME;
     }

     return FULL;
   }


  }

}
