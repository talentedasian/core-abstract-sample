package com.example.demo.dto.in;

import com.example.demo.dto.out.Shoe;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ShoeStock {

  private final Shoe shoe;
  private final int quantity;

}
