package com.example.demo.core.stock;

import com.example.demo.core.Implementation;
import com.example.demo.core.stock.AbstractStockCore;
import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeToStock;
import com.example.demo.dto.out.*;

import java.util.List;

@Implementation(version = 1)
public class StrictStockCore extends AbstractStockCore {

  @Override
  public Stock globalStock(ShoeFilter shoeFilter) {
    return stockService.fetchStock(shoeFilter);
  }

  @Override
  public Stock addShoesToStock(List<ShoeToStock> shoe) {
    throw new UnsupportedOperationException("Version 1 cannot handle multiple shoes to add");
  }

}
