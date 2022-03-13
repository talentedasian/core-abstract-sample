package com.example.demo.core;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.out.Shoes;
import com.example.demo.dto.out.Stock;
import com.example.demo.dto.out.Stock.StockState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StockFacade {

  private final Map<Integer, StockCore> implementations = new HashMap<>();

  public StockCore get(Integer version){
    return implementations.get(version);
  }

  public void register(Integer version, StockCore stockImplementation) {
    implementations.put(version, stockImplementation);
  }
}
