package com.example.demo.core;

import com.example.demo.dto.in.ShoeFilter.Color;
import com.example.demo.dto.out.Shoe;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@RequiredArgsConstructor
@Getter
public class ShoeEntity {

  private final Color color;
  private final int availableStock;
  private final int size;
  private final String name;

  public Shoe toShoe() {
    return Shoe.builder()
        .name(name)
        .color(color)
        .size(BigInteger.valueOf(size))
        .build();
  }

}
