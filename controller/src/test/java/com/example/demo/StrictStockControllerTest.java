package com.example.demo;

import com.example.demo.core.*;
import com.example.demo.dto.in.ShoeFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static java.net.URI.create;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Import({ InMemoryStockEntityRepo.class, InMemoryShoeRepo.class})
public class StrictStockControllerTest {

  @Autowired MockMvc mvc;
  @Autowired StockEntityRepository stockEntityRepo;
  @Autowired ShoeRepository shoeRepo;

  @BeforeEach
  public void setup() {
    shoeRepo.deleteAll();
  }

  @Test
  public void emptyStock() throws Exception{
    stockEntityRepo.saveStock(new StockEntity(0));

    mvc.perform(get(create("/stock?color=BLACK&size=22"))
            .header("version", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("state", equalTo("EMPTY")))
        .andExpect(jsonPath("shoes", hasItems()));
  }

  @Test
  public void fullStock() throws Exception{
    stockEntityRepo.saveStock(new StockEntity(30));
    int lincolnAvailableStock = 22;
    shoeRepo.save(new ShoeEntity(ShoeFilter.Color.BLACK, lincolnAvailableStock, lincolnAvailableStock, "Lincoln"));

    mvc.perform(get(create("/stock?color=BLACK&size=22"))
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
    stockEntityRepo.saveStock(new StockEntity(12));
    int lincolnAvailableStock = 9;
    shoeRepo.save(new ShoeEntity(ShoeFilter.Color.BLACK, lincolnAvailableStock, size, "Lincoln"));

    mvc.perform(get(create("/stock?color=BLACK&size=" + size))
            .header("version", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("state", equalTo("SOME")))
        .andExpect(jsonPath("shoes[0].color", equalTo("BLACK")))
        .andExpect(jsonPath("shoes[0].quantity", equalTo(lincolnAvailableStock)));
  }

}
