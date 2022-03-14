package com.example.demo.controller;

import com.example.demo.core.ShoeEntity;
import com.example.demo.core.ShoeRepository;
import com.example.demo.dto.in.ShoeFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
