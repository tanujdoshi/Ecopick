package com.example.backend.repository;

import com.example.backend.entities.Role;
import com.example.backend.entities.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    User findByRole(Role role);

}
