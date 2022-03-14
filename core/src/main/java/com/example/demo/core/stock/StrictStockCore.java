package com.example.demo.core.stock;

import com.example.demo.core.Implementation;
import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeToUpdate;
import com.example.demo.dto.out.Stock;

import java.util.List;

@Implementation(version = 1)
public class StrictStockCore extends AbstractStockCore {

  @Override
  public Stock globalStock(ShoeFilter shoeFilter) {
    return stockService.fetchStock(shoeFilter);
  }

  @Override
  public Stock updateShoeQuantity(ShoeToUpdate shoe) {
    return stockService.updateStock(shoe);
  }

  @Override
  public Stock updateShoesQuantity(List<ShoeToUpdate> shoe) {
    return null;
  }

}
