package com.example.demo.core.shoe;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoeJpaRepository extends JpaRepository<ShoeEntity, String> {

  List<ShoeEntity> findBySizeAndColor(int size, String color);

}
