package com.example.demo.dto.out;

import com.example.demo.dto.in.ShoeFilter.Color;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ShoesInStock {

  private final Color color;
  private final int quantity;

  private final List<ShoeModel> models;

}
