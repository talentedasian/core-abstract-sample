package com.example.demo.core.shoe;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeToUpdate;
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

  @Override
  public ShoeEntity update(ShoeToUpdate shoe) {
    ShoeEntity shoeEntity = shoeRepo.getById(shoe.getName());
    shoeEntity.setAvailableStock(shoe.getQuantity());

    shoeRepo.save(shoeEntity);
    return shoeEntity;
  }
}
