package com.example.demo.core.shoe;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeToUpdate;

import java.util.Collection;
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

  @Override
  public ShoeEntity update(ShoeToUpdate shoe) {
    String key = shoe.getName();
    if (db.containsKey(key)) {
      ShoeEntity shoeEntity = db.get(key);
      shoeEntity.setAvailableStock(shoe.getQuantity());
      db.put(key, shoeEntity);
      return shoeEntity;
    }

    throw new ShoeNotFoundException();
  }

  @Override
  public boolean containsShoe(String name) {
    return db.containsKey(name);
  }

  @Override
  public List<ShoeEntity> updateAll(List<ShoeToUpdate> shoes) {
    shoes.stream()
        .forEach(shoe -> update(shoe));

    return shoes.stream()
        .map(shoe -> {
          ShoeEntity shoeEntity = db.get(shoe.getName());
          shoeEntity.setAvailableStock(shoe.getQuantity());
          return shoeEntity;
        })
        .toList();
  }

  @Override
  public int totalStockExcept(List<ShoeToUpdate> shoes) {
    Collection<ShoeEntity> shoeEntities = db.values();
    shoeEntities.forEach(shoeEntity -> {
      shoes.stream()
          .forEach(shoe -> {
            if (shoeEntity.getName().equals(shoe.getName())) {
              shoeEntities.remove(shoeEntity);
            }
          });
    });

    return shoeEntities.stream().mapToInt(ShoeEntity::getAvailableStock).sum();
  }
}
