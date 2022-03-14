package com.example.demo.core.shoe;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeToUpdate;

import java.util.List;

public interface ShoeRepository {

  List<ShoeEntity> all();

  List<ShoeEntity> search(ShoeFilter shoeFilter);

  void save(ShoeEntity shoeEntity);

  void deleteAll();

  int totalStock();

  void saveAll(List<ShoeEntity> shoes);

  ShoeEntity update(ShoeToUpdate shoe);
}
