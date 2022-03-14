package com.example.demo.core;

import lombok.val;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Optional;

public abstract class AbstractStockCore implements StockCore {

  @Autowired StockFacade stockFacade;
  @Autowired StockService stockService;

  @PostConstruct
  void init(){

    val version = Optional.ofNullable(this.getClass().getAnnotation(Implementation.class))
        .map(Implementation::version)
        .orElseThrow(() -> new FatalBeanException("AbstractStockCore implementation should be annotated with @Implementation"));

    stockFacade.register(version, this);

  }

}
