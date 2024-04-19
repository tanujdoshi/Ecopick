package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entities.Images;

public interface ImagesRepository extends JpaRepository<Images, Integer> {

    void deleteById(int id);
}
