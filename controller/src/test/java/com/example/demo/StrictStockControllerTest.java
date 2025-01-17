package com.example.demo;

import com.example.demo.controller.StockController;
import com.example.demo.core.shoe.InMemoryShoeRepo;
import com.example.demo.core.shoe.ShoeEntity;
import com.example.demo.core.shoe.ShoeRepository;
import com.example.demo.core.stock.StockService;
import com.example.demo.core.stock.StrictStockCore;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockController.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Import(StrictStockCore.class)
public class StrictStockControllerTest {

  @Autowired MockMvc mvc;
  @Autowired ShoeRepository shoeRepo;

  @BeforeEach
  public void setup() {
    shoeRepo.deleteAll();
  }

  @Test
  public void emptyStock() throws Exception{
    mvc.perform(get(create("/stock?color=BLACK&size=22"))
            .header("version", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("state", equalTo("EMPTY")))
        .andExpect(jsonPath("shoes", hasItems()));
  }

  @Test
  public void fullStock() throws Exception{
    int lincolnAvailableStock = 22;
    // shoe to query
    int size = 22;
    shoeRepo.save(new ShoeEntity(ShoeFilter.Color.BLACK, lincolnAvailableStock, size, "Lincoln"));
    // just to make global stock to max, which is 30
    shoeRepo.save(new ShoeEntity(ShoeFilter.Color.BLACK, 30-lincolnAvailableStock, 10, "Crocs"));

    mvc.perform(get(create("/stock?color=BLACK&size=" + size))
            .header("version", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("state", equalTo("FULL")))
        .andExpect(jsonPath("shoes[0].color", equalTo("BLACK")))
                                        // just to check whether the shoes are actually of the requested size
        .andExpect(jsonPath("shoes[0].models[0].shoe.size", equalTo(lincolnAvailableStock)))
        .andExpect(jsonPath("shoes[0].quantity", equalTo(lincolnAvailableStock)));
  }

  @Test
  public void someStock() throws Exception{
    int size = 12;
    int lincolnAvailableStock = 9;
    shoeRepo.save(new ShoeEntity(ShoeFilter.Color.BLACK, lincolnAvailableStock, size, "Lincoln"));

    mvc.perform(get(create("/stock?color=BLACK&size=" + size))
            .header("version", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("state", equalTo("SOME")))
        .andExpect(jsonPath("shoes[0].color", equalTo("BLACK")))
        .andExpect(jsonPath("shoes[0].quantity", equalTo(lincolnAvailableStock)));
  }

  @Test
  public void addShoeToStock() throws Exception{
    String shoeName = "Crocs";
    int size = 1;
    int quantity = 10;
    String color = "BLACK";
    String reqContent = """
        {
          "name": "%s",
          "size": %s,
          "quantity": %s,
          "color": "%s"
        }
        """.formatted(shoeName, size, quantity, color);

    mvc.perform(post(create("/stock"))
            .header("version", 1)
            .content(reqContent)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("state", equalTo("SOME")))
        .andExpect(jsonPath("shoes[0].color", equalTo(color)))
        .andExpect(jsonPath("shoes[0].quantity", equalTo(quantity)))
        .andExpect(jsonPath("shoes[0].models[0].shoe.size", equalTo(size)));
  }

  @Test
  public void maxStockOverflowWhenAddingShoe() throws Exception{
    shoeRepo.save(new ShoeEntity(ShoeFilter.Color.BLACK, 20, 10, "Lincoln"));

    String name = "Crocs";
    int size = 1;
    int quantity = 11;
    String color = "BLACK";
    String reqContent = """
        {
          "name": "%s",
          "size": %s,
          "quantity": %s,
          "color": "%s"
        }
        """.formatted(name, size, quantity, color);

    mvc.perform(post(create("/stock"))
            .header("version", 1)
            .content(reqContent)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("reason", containsStringIgnoringCase("total stock if request is processed is 31")));
  }

  @Test
  public void conflictWhenAddingShoeThatAlreadyExists() throws Exception{
    ShoeFilter.Color color = ShoeFilter.Color.BLACK;
    String shoeName = "Lincoln";

    shoeRepo.save(new ShoeEntity(color, 20, 10, shoeName));

    String reqContent = """
        {
          "name": "%s",
          "size": %s,
          "quantity": %s,
          "color": "%s"
        }
        """.formatted(shoeName, 10, 10, color);

    mvc.perform(post(create("/stock"))
            .header("version", 1)
            .content(reqContent)

            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("reason", containsStringIgnoringCase("already exists")));
  }

  @Test
  public void badRequestIfShoeFilterIsNullOrOneOfItsFieldsIs() throws Exception{
    shoeRepo.save(new ShoeEntity(ShoeFilter.Color.BLACK, 20, 10, "Crocs"));

    mvc.perform(get(create("/stock"))
            .header("version", 1))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("reason", containsStringIgnoringCase("present in the request")));
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
