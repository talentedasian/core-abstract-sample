package com.example.demo.core.shoe;

import com.example.demo.core.Implementation;
import com.example.demo.facade.ShoeFacade;
import lombok.val;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Optional;

public abstract class AbstractShoeCore implements ShoeCore {

  @Autowired
  private ShoeFacade shoeFacade;
  @Autowired
  protected ShoeRepository shoeRepo;

  @PostConstruct
  void init(){

    val version = Optional.ofNullable(this.getClass().getAnnotation(Implementation.class))
                          .map(Implementation::version)
                          .orElseThrow(() -> new FatalBeanException("AbstractShoeCore implementation should be annotated with @Implementation"));

    shoeFacade.register(version, this);

  }

}
