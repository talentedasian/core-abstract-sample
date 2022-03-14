package com.example.demo.core;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeToStock;
import com.example.demo.dto.out.Stock;

import java.util.List;

@Implementation(version = 2)
public class LenientStockCore extends AbstractStockCore {

  @Override
  public Stock globalStock(ShoeFilter shoeFilter) {
    if (shoeFilter == null || (shoeFilter.getColor().isEmpty() || shoeFilter.getSize().isEmpty())) {
      return stockService.allShoes();
    }

    return stockService.fetchStock(shoeFilter);
  }

  @Override
  public Stock addShoesToStock(List<ShoeToStock> shoes) {
    return stockService.addShoes(shoes);
  }

}
