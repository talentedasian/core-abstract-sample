package com.example.demo.dto.in;

import com.example.demo.dto.out.Shoe;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigInteger;

@RequiredArgsConstructor
@Getter
public class ShoeToStock {

  @NotBlank(message = "Name cannot be blank")
  private final String name;

  @Min(value = 1, message = "Size cannot be less than 1")
  private final int size;

  // all caps
  @Pattern(regexp = "[A-Z]*")
  private final String color;

  @Min(value = 1, message = "Minimum quantity is 1")
  @Max(value = 30, message = "Maximum quantity is 30")
  private final int quantity;



}
