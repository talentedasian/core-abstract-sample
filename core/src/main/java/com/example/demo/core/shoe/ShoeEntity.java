package com.example.demo.core.shoe;

import com.example.demo.dto.in.ShoeFilter.Color;
import com.example.demo.dto.out.Shoe;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;

@NoArgsConstructor
@Getter
@Entity
public class ShoeEntity {

  @Id
  private String name;
  private String color;
  private int availableStock;
  private int size;

  public ShoeEntity(Color color, int availableStock, int size, String name) {
    this.color = color.toString();
    this.availableStock = availableStock;
    this.size = size;
    this.name = name;
  }

  public Shoe toShoe() {
    return Shoe.builder()
        .name(name)
        .color(Color.valueOf(color))
        .size(BigInteger.valueOf(size))
        .build();
  }

}
