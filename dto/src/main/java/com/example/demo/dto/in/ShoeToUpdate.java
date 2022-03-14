package com.example.demo.dto.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class ShoeToUpdate {

  @NotBlank
  private final String name;

  @Min(value = 1, message = "Minimum quantity is 1")
  @Max(value = 30, message = "Maximum quantity is 30")
  private final int quantity;

}
