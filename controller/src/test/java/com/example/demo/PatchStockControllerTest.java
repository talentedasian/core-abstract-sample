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

import static java.net.URI.create;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
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

    int quantityToUpdate = 20;
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
        .andExpect(jsonPath("reason", containsStringIgnoringCase("total stock if request is processed is 40")));
  }

}
