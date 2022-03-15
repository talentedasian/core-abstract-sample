package com.example.demo.facade;

import com.example.demo.core.shoe.ShoeCore;
import com.example.demo.core.stock.StockCore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ShoeFacade {

  private final Map<Integer, ShoeCore> implementations = new HashMap<>();

  public ShoeCore get(Integer version){
    return implementations.get(version);
  }

  public void register(Integer version, ShoeCore implementation){
    this.implementations.put(version, implementation);
  }

  @Component
  @RequiredArgsConstructor
  public static class StockFacade {

    private final Map<Integer, StockCore> implementations = new HashMap<>();

    public StockCore get(Integer version){
      return implementations.get(version);
    }

    public void register(Integer version, StockCore stockImplementation) {
      implementations.put(version, stockImplementation);
    }
  }
}
