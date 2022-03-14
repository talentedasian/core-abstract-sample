package com.example.demo.core.shoe;

import com.example.demo.dto.in.ShoeFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// todo : better exception handling of optional values
public class InMemoryShoeRepo implements ShoeRepository {

  Map<String, ShoeEntity> db = new HashMap<>();

  @Override
  public List<ShoeEntity> all() {
    return List.copyOf(db.values());
  }

  @Override
  public List<ShoeEntity> search(ShoeFilter shoeFilter) {
    return db.values().stream()
        .filter(shoe -> shoe.getColor().equals(shoeFilter.getColor().get().toString())
            && shoe.getSize() == shoeFilter.getSize().get().intValueExact())
        .toList();
  }

  @Override
  public void save(ShoeEntity shoeEntity) {
    db.put(shoeEntity.getName(), shoeEntity);
  }

  @Override
  public void deleteAll() {
    db.clear();
  }

  @Override
  public int totalStock() {
    return db.values().stream()
        .mapToInt(ShoeEntity::getAvailableStock)
        .sum();
  }

  @Override
  public void saveAll(List<ShoeEntity> shoes) {
    shoes.stream()
        .forEach(shoe -> {
          db.put(shoe.getName(), shoe);
        });
  }
}
