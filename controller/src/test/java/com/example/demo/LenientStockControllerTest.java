package com.example.demo;

import com.example.demo.controller.StockController;
import com.example.demo.core.*;
import com.example.demo.dto.in.ShoeFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static java.net.URI.create;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StockController.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Import({ LenientStockCore.class })
public class LenientStockControllerTest {

  @Autowired MockMvc mvc;
  @Autowired ShoeRepository shoeRepo;

  @BeforeEach
  public void setup() {
    shoeRepo.deleteAll();
  }

  @Test
  public void addShoesToStock() throws Exception{
    String crocs = "Crocs";
    int crocsSize = 1;
    int crocsQuantity = 10;
    String nike = "Nike";
    int nikeSize = 12;
    int nikeQuantity = 20;
    String color = "BLACK";
    String reqContent = """
        [
          {
            "name": "%s",
            "size": %s,
            "quantity": %s,
            "color": "%s"
          },
          {
            "name": "%s",
            "size": %s,
            "quantity": %s,
            "color": "%s"
          }
        ]
        """.formatted(crocs, crocsSize, crocsQuantity, color, nike, nikeSize, nikeQuantity, color);

    mvc.perform(patch(create("/stocks"))
            .header("version", 2)
            .content(reqContent)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("state", equalTo("FULL")))
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
            "size": %s,
            "quantity": %s,
            "color": "%s"
          }
        ]
        """.formatted(name, size, quantity, color);

    mvc.perform(patch(create("/stocks"))
            .header("version", 2)
            .content(reqContent)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("reason", containsStringIgnoringCase("total stock if request is processed is 31")));
  }

  @TestConfiguration
  static class Configuration {
    @Bean
    public StockFacade stockFacade() {
      return new StockFacade();
    }
    @Bean
    ShoeRepository shoeRepository() {
      return new InMemoryShoeRepo();
    }
    @Bean
    StockService stockService() {
      return new StockService(shoeRepository());
    }
  }

}
