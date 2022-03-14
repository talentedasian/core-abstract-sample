package com.example.demo;

import com.example.demo.core.shoe.ShoeEntity;
import com.example.demo.core.shoe.ShoeRepository;
import com.example.demo.dto.in.ShoeFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.net.URI.create;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
public class PatchStockControllerTest {

  @Autowired MockMvc mvc;
  @Autowired ShoeRepository shoeRepo;

  @Test
  public void updateAvailableStockOnExistingShoe() throws Exception {
    String name = "Flops";
    shoeRepo.save(new ShoeEntity(ShoeFilter.Color.BLACK, 20, 10, name));

    int quantityToUpdate = 5;
    String reqContent = """
        {
          "name": "%s",
          "quantity": %s
        }
        """.formatted(name, quantityToUpdate);

    mvc.perform(patch(create("/stock"))
            .header("version", 1)
            .content(reqContent)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("shoes[0].quantity", equalTo(quantityToUpdate)));
  }

  @Test
  public void overflowingUpdateOnAvailableStockOnExistingShoe() throws Exception {
    String name = "Flops";
    shoeRepo.save(new ShoeEntity(ShoeFilter.Color.BLACK, 20, 10, name));

    int quantityToUpdate = 31;
    String reqContent = """
        {
          "name": "%s",
          "quantity": %s
        }
        """.formatted(name, quantityToUpdate);

    mvc.perform(patch(create("/stock"))
            .header("version", 1)
            .content(reqContent)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("reason",
            containsStringIgnoringCase("total stock if request is processed is " + quantityToUpdate)));
  }

  @Test
  public void addShoesToStock() throws Exception{
    String crocs = "Crocs";
    int crocsSize = 1;
    int crocsQuantity = 10;
    String nike = "Nike";
    int nikeSize = 12;
    int nikeQuantity = 18;
    String color = "BLACK";
    ShoeFilter.Color COLOR = ShoeFilter.Color.BLACK;
    // setup
    shoeRepo.saveAll(List.of(
        new ShoeEntity(COLOR, 10, crocsSize, crocs),
        new ShoeEntity(COLOR, 5, nikeSize, nike)));

    String reqContent = """
        [
          {
            "name": "%s",
            "quantity": %s
          },
          {
            "name": "%s",
            "quantity": %s
          }
        ]
        """.formatted(crocs, crocsQuantity, nike, nikeQuantity);

    mvc.perform(patch(create("/stocks"))
            .header("version", 2)
            .content(reqContent)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("state", equalTo("SOME")))
        .andExpect(jsonPath("shoes[0].color", equalTo(color)))
        .andExpect(jsonPath("shoes[0].quantity", equalTo(crocsQuantity + nikeQuantity)))
        .andExpect(jsonPath("shoes[0].models[0].shoe.size", equalTo(crocsSize)))
        .andExpect(jsonPath("shoes[0].models[0].quantity", equalTo(crocsQuantity)))
        .andExpect(jsonPath("shoes[0].color", equalTo(color)))
        .andExpect(jsonPath("shoes[0].models[1].shoe.size", equalTo(nikeSize)))
        .andExpect(jsonPath("shoes[0].models[1].quantity", equalTo(nikeQuantity)));
  }

  @Test
  public void maxStockOverflowWhenAddingShoes() throws Exception{
    shoeRepo.save(new ShoeEntity(ShoeFilter.Color.BLACK, 20, 10, "Lincoln"));

    String name = "Crocs";
    int size = 1;
    int quantity = 11;
    String color = "BLACK";
    String reqContent = """
        [
          {
            "name": "%s",
            "quantity": %s
          }
        ]
        """.formatted(name, quantity);

    mvc.perform(patch(create("/stocks"))
            .header("version", 2)
            .content(reqContent)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("reason", containsStringIgnoringCase("total stock if request is processed is 31")));
  }

}
