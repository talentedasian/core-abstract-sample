package com.example.demo;

import com.example.demo.controller.StockController;
import com.example.demo.core.shoe.InMemoryShoeRepo;
import com.example.demo.core.shoe.ShoeEntity;
import com.example.demo.core.shoe.ShoeRepository;
import com.example.demo.core.stock.LenientStockCore;
import com.example.demo.core.stock.StockService;
import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.facade.ShoeFacade;
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

import static java.net.URI.create;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
  public void allShoesIfShoeFilterIsNullOrOneOfTheFieldsAreNull() throws Exception{
    String lincoln = "Lincoln";
    String nike = "nike";
    shoeRepo.save(new ShoeEntity(ShoeFilter.Color.BLACK, 10, 8, lincoln));
    shoeRepo.save(new ShoeEntity(ShoeFilter.Color.BLACK, 20, 8, nike));
    shoeRepo.save(new ShoeEntity(ShoeFilter.Color.BLACK, 0, 8, "Random"));

    mvc.perform(get(create("/stock"))
            .header("version", 2))
        .andExpect(status().isOk())
        .andExpect(jsonPath("state", equalTo("FULL")))
                                      // check if all shoes are indeed included
        .andExpect(jsonPath("shoes[0].models[*].quantity", hasItem(0)));
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
            .content(reqContent)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("reason", containsStringIgnoringCase("total stock if request is processed is 31")));
  }

  @TestConfiguration
  static class Configuration {
    @Bean
    public ShoeFacade.StockFacade stockFacade() {
      return new ShoeFacade.StockFacade();
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
