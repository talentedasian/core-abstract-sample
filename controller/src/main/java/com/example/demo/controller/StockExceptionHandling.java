package com.example.demo.controller;

import com.example.demo.core.StockOverflowException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class StockExceptionHandling {

  @ExceptionHandler(StockOverflowException.class)
  public ResponseEntity<ExceptionMessage> handleStockOverflow(StockOverflowException e) {
    int totalStock = e.totalStock();
    var reason = new ExceptionMessage("Maximum stock quantity is 30. Total stock if request is processed is " + totalStock);

    return new ResponseEntity<ExceptionMessage>(reason, HttpStatus.CONFLICT);
  }

  @Getter
  @RequiredArgsConstructor
  class ExceptionMessage {
    private final String reason;
  }

}
