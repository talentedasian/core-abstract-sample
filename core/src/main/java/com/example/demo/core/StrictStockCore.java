package com.example.demo.core;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeToStock;
import com.example.demo.dto.out.*;

import javax.transaction.NotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
