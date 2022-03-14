package com.example.demo.core;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeToStock;
import com.example.demo.dto.out.Stock;

import java.util.List;

public interface StockCore {

  Stock globalStock(ShoeFilter shoeFilter);

  Stock addShoeToStock(ShoeToStock shoeStock);

  Stock addShoesToStock(List<ShoeToStock> shoes);
}
