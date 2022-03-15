package com.example.demo.core.shoe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShoeJpaRepository extends JpaRepository<ShoeEntity, String> {

  List<ShoeEntity> findBySizeAndColor(int size, String color);

  @Query(value = """
      UPDATE shoe SET available_stock = :stockQuantity WHERE name = :name
      """)
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  void update(String name, int stockQuantity);

  List<ShoeEntity> findByNameIn(List<String> names);

  List<ShoeEntity> findByNameNotIn(List<String> names);

  boolean existsByName(String name);
}
