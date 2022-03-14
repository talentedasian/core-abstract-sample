package com.example.demo.core.stock;

import com.example.demo.core.Implementation;
import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeToUpdate;
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
  public Stock updateShoeQuantity(ShoeToUpdate shoe) {
    return stockService.updateStock(shoe);
  }

  @Override
  public Stock updateShoesQuantity(List<ShoeToUpdate> shoe) {
    return null;
  }

}
