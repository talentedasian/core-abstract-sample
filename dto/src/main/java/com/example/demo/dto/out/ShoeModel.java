package com.example.demo.dto.out;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ShoeModel {

  private final Shoe shoe;
  private final int quantity;

}
