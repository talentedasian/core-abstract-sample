package com.example.demo.controller;

import com.example.demo.core.ShoeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoeJpaRepository extends JpaRepository<ShoeEntity, String> {

  List<ShoeEntity> findBySizeAndColor(int size, String color);

}
