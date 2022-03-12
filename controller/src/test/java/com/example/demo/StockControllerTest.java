package com.example.demo;

import com.example.demo.demo.InMemoryStockEntityRepo;
import com.example.demo.demo.StockEntity;
import com.example.demo.demo.StockEntityRepository;
import com.example.demo.demo.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static java.net.URI.create;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Import(InMemoryStockEntityRepo.class)
public class StockControllerTest {

  @Autowired MockMvc mvc;
  @Autowired StockEntityRepository stockEntityRepo;

  @Test
  public void emptyStock() throws Exception{
    stockEntityRepo.saveStock(new StockEntity(0));

    mvc.perform(get(create("/stock")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("state", equalTo("EMPTY")));
  }

  @Test
  public void fullStock() throws Exception{
    stockEntityRepo.saveStock(new StockEntity(30));

    mvc.perform(get(create("/stock")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("state", equalTo("FULL")));
  }

  @Test
  public void someStock() throws Exception{
    stockEntityRepo.saveStock(new StockEntity(12));

    mvc.perform(get(create("/stock")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("state", equalTo("SOME")));
  }

}
