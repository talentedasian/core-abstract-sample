package com.example.demo;

import com.example.demo.core.shoe.ShoeEntity;
import com.example.demo.core.shoe.ShoeJpaRepository;
import com.example.demo.dto.in.ShoeFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class ShoeJpaRepositoryTest {

  @Autowired ShoeJpaRepository repo;

  @Test
  public void saveShoe() throws Exception{
    String name = "Lincoln";
    ShoeEntity shoeToSave = new ShoeEntity(ShoeFilter.Color.BLACK, 30, 8, name);
    repo.save(shoeToSave);

    ShoeEntity shoeQueried = repo.findById(name).get();

    assertThat(shoeQueried)
        .isEqualTo(shoeToSave);
  }

  @Test
  public void updateShoe() throws Exception{
    String name = "Lincoln";
    repo.save(new ShoeEntity(ShoeFilter.Color.BLACK, 30, 8, name));
    int quantityToUpdate = 10;
    repo.update(name, quantityToUpdate);

    ShoeEntity shoeQueried = repo.findById(name).get();

    assertThat(shoeQueried.getAvailableStock())
        .isEqualTo(quantityToUpdate);
  }

}
