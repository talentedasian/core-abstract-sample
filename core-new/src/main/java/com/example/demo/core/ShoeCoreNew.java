package com.example.demo.core;

import com.example.demo.core.shoe.AbstractShoeCore;
import com.example.demo.core.shoe.ShoeEntity;
import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.out.Shoes;

@Implementation(version = 2)
public class ShoeCoreNew extends AbstractShoeCore {

  @Override
  public Shoes search(final ShoeFilter filter) {
    if (filter != null) {
      if (filter.getColor().isEmpty() || filter.getSize().isEmpty()) {
        return allShoes();
      }
      return Shoes.builder()
          .shoes(shoeRepo.search(filter).stream()
              .map(ShoeEntity::toShoe)
              .toList())
          .build();
    }

    return allShoes();
  }

  private Shoes allShoes() {
    return Shoes.builder().shoes(shoeRepo.all().stream()
            .map(ShoeEntity::toShoe)
            .toList())
        .build();
  }


}
