package com.example.demo;

import com.example.demo.core.*;
import com.example.demo.dto.in.ShoeFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;

import static java.net.URI.create;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
public class StrictStockControllerTest {

  @Autowired MockMvc mvc;
  @Autowired ShoeRepository shoeRepo;

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
    String name = "Crocs";
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
        """.formatted(name, size, quantity, color);

    mvc.perform(patch(create("/stock"))
            .header("version", 1)
            .content(reqContent)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("state", equalTo("SOME")))
        .andExpect(jsonPath("shoes[0].color", equalTo(color)))
        .andExpect(jsonPath("shoes[0].quantity", equalTo(quantity)))
        .andExpect(jsonPath("shoes[0].models[0].shoe.size", equalTo(size)));
  }

}
