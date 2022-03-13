package com.example.demo.core;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.out.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Implementation(version = 1)
public class StrictStockCore extends AbstractStockCore {

  @Override
  public Stock globalStock(ShoeFilter shoeFilter) {
    StockEntity stockEntity = stockRepo.retrieveStock();

    List<ShoeEntity> shoes = shoeRepo.search(shoeFilter);
    List<ShoesInStock> shoesDto = shoesGroupedByColor(shoes);

    Stock stock = new Stock(stockEntity.quantity(), shoesDto);

    return stock;
  }

  private List<ShoesInStock> shoesGroupedByColor(List<ShoeEntity> shoes) {
    Map<ShoeFilter.Color, List<ShoeModel>> models = shoes.stream()
        .map(shoe -> new ShoeModel(shoe.toShoe(), shoe.getAvailableStock()))
        .collect(Collectors.groupingBy(shoe -> shoe.getShoe().getColor()));

    List<ShoesInStock> shoesByColors = new ArrayList<>();
    models.forEach((k,v) -> {
      int totalStock = v.stream().mapToInt(ShoeModel::getQuantity).sum();
      shoesByColors.add(new ShoesInStock(k, totalStock, v));
    });

    return shoesByColors;
    }

}
