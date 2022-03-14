package com.example.demo.core;

import com.example.demo.dto.in.ShoeFilter;

import java.util.List;

public interface ShoeRepository {

  List<ShoeEntity> all();

  List<ShoeEntity> search(ShoeFilter shoeFilter);

  void save(ShoeEntity shoeEntity);

  void deleteAll();

  int count();
}
