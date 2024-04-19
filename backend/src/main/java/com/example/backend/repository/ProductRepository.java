package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.backend.entities.Farms;
import com.example.backend.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByFarm(Farms farm);

    Product findById(int id);

    void deleteById(int id);

    List<Product> findByProductNameContaining(String infix);

    List<Product> findTop8ByOrderByIdDesc();
}
