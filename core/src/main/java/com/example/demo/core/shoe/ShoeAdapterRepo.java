package com.example.demo.core.shoe;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeToUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.IntStream;

@Repository
@RequiredArgsConstructor
public class ShoeAdapterRepo implements ShoeRepository {

  private final ShoeJpaRepository shoeRepo;

  @Override
  public List<ShoeEntity> all() {
    return shoeRepo.findAll();
  }

  @Override
  public List<ShoeEntity> search(ShoeFilter shoeFilter) {
    return shoeRepo.findBySizeAndColor(shoeFilter.getSize().get().intValueExact(),
        shoeFilter.getColor().get().toString());
  }

  @Override
  public void save(ShoeEntity shoeEntity) {
    if (shoeRepo.existsById(shoeEntity.getName())) {
      throw new ShoeAlreadyExistsException();
    }

    shoeRepo.save(shoeEntity);
  }

  @Override
  public void deleteAll() {
    shoeRepo.deleteAll();
  }

  @Override
  public int totalStock() {
    // todo : replace with better query
    return shoeRepo.findAll().stream()
        .mapToInt(ShoeEntity::getAvailableStock)
        .sum();
  }

  @Override
  public void saveAll(List<ShoeEntity> shoes) {
    shoeRepo.saveAll(shoes);
  }

  @Override
  public ShoeEntity update(ShoeToUpdate shoe) {
    String id = shoe.getName();
    if (shoeRepo.existsById(id)) {
      shoeRepo.update(id, shoe.getQuantity());
      return shoeRepo.findById(id).get();
    }

    throw new ShoeNotFoundException();
  }

  @Override
  public boolean containsShoe(String name) {
    return shoeRepo.existsById(name);
  }

  @Override
  public List<ShoeEntity> updateAll(List<ShoeToUpdate> shoes) {
    List<ShoeEntity> shoesToUpdate = shoeRepo.findByNameIn(shoes.stream().map(ShoeToUpdate::getName).toList());

    IntStream.range(0, shoes.size()).forEach(i -> {
      shoesToUpdate.get(i).setAvailableStock(shoes.get(i).getQuantity());
    });

    shoeRepo.saveAll(shoesToUpdate);
    return shoesToUpdate;
  }

  @Override
  public int totalStockExcept(List<ShoeToUpdate> shoes) {
    // todo : better query
    return shoeRepo.findByNameNotIn(shoes.stream().map(ShoeToUpdate::getName).toList()).stream()
        .mapToInt(ShoeEntity::getAvailableStock)
        .sum();
  }
}
