package com.example.demo;

import com.example.demo.demo.InvalidStockQuantity;
import com.example.demo.demo.Stock;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class StockTest {

  @Test
  public void zeroStocksShouldBeEmpty() throws Exception{
    Stock zeroStock = Stock.zero();

    boolean isStockEmpty = zeroStock.isEmpty();
    AssertionsForClassTypes.assertThat(isStockEmpty)
        .isTrue();
  }

  @Test
  public void stockWithOneOrMoreThanOneIsNotEmpty() throws Exception{
    Stock singleStock = Stock.ofQuantity(1);
    Stock moreThanOneStock = Stock.ofQuantity(100);

    boolean isSingleStockEmpty = singleStock.isEmpty();
    boolean isMultipleStockEmpty = moreThanOneStock.isEmpty();

    Assertions.assertAll
        (
        () -> AssertionsForClassTypes.assertThat(isSingleStockEmpty)
            .isFalse(),
        () -> AssertionsForClassTypes.assertThat(isMultipleStockEmpty)
            .isFalse()
        );
  }

  @ParameterizedTest
  @ValueSource(ints = {-11, -1, -999, -2, -312})
  public void invalidStock(int invalidStockQuantity) throws Exception{
    ThrowableAssert.ThrowingCallable invalidStockConstruction = () -> Stock.ofQuantity(invalidStockQuantity);

    AssertionsForClassTypes.assertThatThrownBy(invalidStockConstruction)
        .isInstanceOf(InvalidStockQuantity.class)
        .hasMessageContaining("cannot be less than 0");
  }

}
