package com.example.demo.core.shoe;

import com.example.demo.core.shoe.ShoeEntity;
import com.example.demo.dto.in.ShoeFilter;

import java.util.List;

public interface ShoeRepository {

  List<ShoeEntity> all();

  List<ShoeEntity> search(ShoeFilter shoeFilter);

  void save(ShoeEntity shoeEntity);

  void deleteAll();

  int totalStock();

  void saveAll(List<ShoeEntity> shoes);
}
