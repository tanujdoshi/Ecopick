package com.example.backend.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.entities.Farms;
import com.example.backend.entities.User;

@Repository
public interface FarmRepository extends JpaRepository<Farms, Integer> {
    Farms findById(int id);

    List<Farms> findByUser(User user);

    List<Farms> findTop8ByOrderByIdDesc();

    List<Farms> findByNameIgnoreCaseContaining(String name);

}
