package com.example.demo;

import com.example.demo.core.shoe.InMemoryShoeRepo;
import com.example.demo.core.shoe.ShoeEntity;
import com.example.demo.core.shoe.ShoeRepository;
import com.example.demo.core.stock.StockOverflowException;
import com.example.demo.core.stock.StockService;
import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeToStock;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class StockServiceTest {

  ShoeRepository shoeRepo;

  @BeforeEach
  public void setup() {
    shoeRepo = new InMemoryShoeRepo();
  }

  @Test
  public void addShoe() throws Exception{
    StockService service = new StockService(shoeRepo);
    String name = "Lincoln";
    int size = 8;
    String color = "BLACK";
    int quantity = 10;
    service.addShoe(new ShoeToStock(name, size, color, quantity));

    ShoeFilter.Color COLOR = ShoeFilter.Color.valueOf(color);
    List<ShoeEntity> shoesQueried = shoeRepo.search(new ShoeFilter(BigInteger.valueOf(size), COLOR));

    assertThat(shoesQueried)
        .containsExactly(new ShoeEntity(COLOR, quantity, size, name));
  }

  @Test
  public void overflowStockQuantity() throws Exception{
    StockService service = new StockService(shoeRepo);
    String name = "Lincoln";
    int size = 8;
    String color = "BLACK";
    int quantity = 31;
    ThrowableAssert.ThrowingCallable addingCausesOverflow = () -> service.addShoe(new ShoeToStock(name, size, color, quantity));

    assertThatThrownBy(addingCausesOverflow)
        .isInstanceOf(StockOverflowException.class);
  }

}
