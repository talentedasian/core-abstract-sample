package com.example.demo.core.shoe;

import com.example.demo.dto.in.ShoeFilter.Color;
import com.example.demo.dto.out.Shoe;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ShoeEntity that = (ShoeEntity) o;

    if (availableStock != that.availableStock) return false;
    if (size != that.size) return false;
    if (!Objects.equals(name, that.name)) return false;
    return Objects.equals(color, that.color);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (color != null ? color.hashCode() : 0);
    result = 31 * result + availableStock;
    result = 31 * result + size;
    return result;
  }
}
