package com.example.demo.dto.out;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("stock")
public class StockDTO {

  private final String state;

  public String getState() {
    return state;
  }

  public StockDTO(String state) {
    this.state = state;
  }

}
