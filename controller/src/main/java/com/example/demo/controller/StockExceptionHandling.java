package com.example.demo.controller;

import com.example.demo.core.shoe.ShoeAlreadyExistsException;
import com.example.demo.core.shoe.ShoeNotFoundException;
import com.example.demo.core.stock.ShoeFilterNullException;
import com.example.demo.core.stock.StockOverflowException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class StockExceptionHandling extends ResponseEntityExceptionHandler {

  @ExceptionHandler(StockOverflowException.class)
  public ResponseEntity<ExceptionMessage> handleStockOverflow(StockOverflowException e) {
    int totalStock = e.totalStock();
    var reason = new ExceptionMessage("Maximum stock quantity is 30. Total stock if request is processed is " + totalStock);

    return new ResponseEntity<ExceptionMessage>(reason, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ShoeNotFoundException.class)
  public ResponseEntity<ExceptionMessage> handleShoeNotFoundForUpdates() {
    var reason = new ExceptionMessage("Could not update non existing shoe");
    return new ResponseEntity<ExceptionMessage>(reason, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ShoeAlreadyExistsException.class)
  public ResponseEntity<ExceptionMessage> handleSavingExistingShoe() {
    var reason = new ExceptionMessage("Shoe already exists. Consider updating instead");
    return new ResponseEntity<ExceptionMessage>(reason, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ShoeFilterNullException.class)
  public ResponseEntity<ExceptionMessage> handleShoeFilterNull() {
    var reason = new ExceptionMessage("Color and Size must be present in the request");
    return new ResponseEntity<ExceptionMessage>(reason, HttpStatus.BAD_REQUEST);
  }

  @Getter
  @RequiredArgsConstructor
  class ExceptionMessage {
    private final String reason;
  }

}
