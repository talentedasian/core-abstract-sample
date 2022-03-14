package com.example.demo;

import com.example.demo.core.stock.InvalidStockQuantity;
import com.example.demo.core.stock.StockEntity;
import com.example.demo.core.stock.StockOverflowException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class StockEntityTest {

  @Test
  public void zeroStocksShouldBeEmpty() throws Exception{
    StockEntity zeroStock = StockEntity.zero();

    boolean isStockEmpty = zeroStock.isEmpty();
    assertThat(isStockEmpty)
        .isTrue();
  }

  @Test
  public void stockWithQuantityOfThirtyShouldBeFull() throws Exception{
    StockEntity fullStock = StockEntity.ofQuantity(30);

    boolean isStockFull = fullStock.isFull();
    assertThat(isStockFull)
        .isTrue();
  }

  @Test
  public void stockWithOneOrMoreThanOneIsNotEmpty() throws Exception{
    StockEntity singleStock = StockEntity.ofQuantity(1);
    StockEntity moreThanOneStock = StockEntity.ofQuantity(10);

    boolean isSingleStockEmpty = singleStock.isEmpty();
    boolean isMultipleStockEmpty = moreThanOneStock.isEmpty();

    assertAll
        (
            () -> assertThat(isSingleStockEmpty)
            .isFalse(),
            () -> assertThat(isMultipleStockEmpty)
            .isFalse()
        );
  }

  @ParameterizedTest
  @ValueSource(ints = { -11, -1, -999, -2, -312 })
  public void invalidNegativeStock(int invalidStockQuantity) throws Exception{
    ThrowableAssert.ThrowingCallable invalidStockConstruction = () -> StockEntity.ofQuantity(invalidStockQuantity);

    assertThatThrownBy(invalidStockConstruction)
        .isInstanceOf(InvalidStockQuantity.class)
        .hasMessageContaining("cannot be less than 0");
  }

  @ParameterizedTest
  @ValueSource(ints = { 31, 99, 100, 42, 944 })
  public void invalidOverflowingStock(int quantityMoreThan30) throws Exception{
    ThrowableAssert.ThrowingCallable invalidStockConstruction = () -> StockEntity.ofQuantity(quantityMoreThan30);

    assertThatThrownBy(invalidStockConstruction)
        .isInstanceOf(StockOverflowException.class);
  }

}
