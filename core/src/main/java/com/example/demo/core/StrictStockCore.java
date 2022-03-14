package com.example.demo.core;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeToStock;
import com.example.demo.dto.out.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Implementation(version = 1)
public class StrictStockCore extends AbstractStockCore {

  @Override
  public Stock globalStock(ShoeFilter shoeFilter) {
    return stockService.fetchStock(shoeFilter);
  }

  @Override
  public Stock addShoeToStock(ShoeToStock shoeStock) {
    return stockService.addShoe(shoeStock);
  }

}
