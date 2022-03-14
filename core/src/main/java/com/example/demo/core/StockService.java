package com.example.demo.core;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.out.ShoeModel;
import com.example.demo.dto.out.ShoesInStock;
import com.example.demo.dto.out.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

  private final ShoeRepository shoeRepository;

  public Stock fetchStock(ShoeFilter shoeFilter) {
    StockEntity stockEntity = new StockEntity(totalStock());

    List<ShoeEntity> shoes = shoeRepository.search(shoeFilter);

    Stock.StockState stockState = shoesStockState(stockEntity);
    return new Stock(stockState, shoesGroupedByColor(shoes));
  }

  private int totalStock() {
    return shoeRepository.all().stream()
        .mapToInt(ShoeEntity::getAvailableStock).sum();
  }

  private Stock.StockState shoesStockState(StockEntity stockEntity) {
    if (stockEntity.isFull()) {
      return Stock.StockState.FULL;
    }

    return stockEntity.isEmpty() ? Stock.StockState.EMPTY : Stock.StockState.SOME;
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
