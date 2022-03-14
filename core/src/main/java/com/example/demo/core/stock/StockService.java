package com.example.demo.core.stock;

import com.example.demo.core.shoe.ShoeEntity;
import com.example.demo.core.shoe.ShoeRepository;
import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeToStock;
import com.example.demo.dto.in.ShoeToUpdate;
import com.example.demo.dto.out.ShoeModel;
import com.example.demo.dto.out.ShoesInStock;
import com.example.demo.dto.out.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

  private final ShoeRepository shoeRepository;

  @Transactional(readOnly = true)
  public Stock fetchStock(ShoeFilter shoeFilter) {
    StockEntity stockEntity = new StockEntity(shoeRepository.totalStock());

    List<ShoeEntity> shoes = shoeRepository.search(shoeFilter);

    Stock.StockState stockState = shoesStockState(stockEntity);
    return new Stock(stockState, shoesGroupedByColor(shoes));
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

  @Transactional(readOnly = true)
  public Stock allShoes() {
    StockEntity stockEntity = new StockEntity(shoeRepository.totalStock());

    List<ShoeEntity> shoes = shoeRepository.all();

    Stock.StockState stockState = shoesStockState(stockEntity);
    return new Stock(stockState, shoesGroupedByColor(shoes));
  }

  @Transactional
  public Stock addShoe(ShoeToStock shoeStock) {
    int totalStock = shoeRepository.totalStock();
    int finalTotalStock = totalStock + shoeStock.getQuantity();
    totalShoesQuantityOverflowStockCheck(finalTotalStock);
    ShoeEntity shoeEntity = new ShoeEntity(ShoeFilter.Color.valueOf(shoeStock.getColor()),
        shoeStock.getQuantity(),
        shoeStock.getSize(),
        shoeStock.getName());
    shoeRepository.save(shoeEntity);

    StockEntity stockEntity = new StockEntity(finalTotalStock);
    return new Stock(shoesStockState(stockEntity), shoesGroupedByColor(List.of(shoeEntity)));
  }

  @Transactional
  public Stock addShoes(List<ShoeToStock> shoes) {
    int totalStock = shoeRepository.totalStock();
    int totalStockToAdd = shoes.stream().mapToInt(ShoeToStock::getQuantity).sum();

    sizeOfListOverflowStockCheck(totalStock, shoes);
    int finalTotalStock = totalStock + totalStockToAdd;
    totalShoesQuantityOverflowStockCheck(finalTotalStock);

    List<ShoeEntity> shoeEntities = shoes.stream()
        .map(shoe -> new ShoeEntity(ShoeFilter.Color.valueOf(shoe.getColor()),
            shoe.getQuantity(),
            shoe.getSize(),
            shoe.getName()))
        .toList();
    shoeRepository.saveAll(shoeEntities);

    StockEntity stockEntity = new StockEntity(finalTotalStock);
    return new Stock(shoesStockState(stockEntity), shoesGroupedByColor(shoeEntities));
  }

  private void totalShoesQuantityOverflowStockCheck(int totalStock) {
    boolean willTotalQuantityOverflowMaxStock = !StockEntity.isBelowFull(totalStock);
    maxStockCheck(willTotalQuantityOverflowMaxStock, totalStock);
  }

  private void sizeOfListOverflowStockCheck(int totalStock, List<ShoeToStock> shoes) {
    int total = totalStock + shoes.size();
    boolean willListOfShoeModelsToAddOverflowMaxStock = !StockEntity.isBelowFull(total);
    maxStockCheck(willListOfShoeModelsToAddOverflowMaxStock, total);
  }

  private void maxStockCheck(boolean willOverflowMaxStock, int totalStock) {
    if (willOverflowMaxStock) {
      throw new StockOverflowException(totalStock);
    }
  }

  @Transactional
  public Stock updateStock(ShoeToUpdate shoe) {
    int totalStock = shoeRepository.totalStock();
    int finalTotal = shoe.getQuantity() + totalStock;
    totalShoesQuantityOverflowStockCheck(finalTotal);

    ShoeEntity updatedShoe = shoeRepository.update(shoe);
    return new Stock(shoesStockState(new StockEntity(finalTotal)), shoesGroupedByColor(List.of(updatedShoe)));
  }
}
