package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.entities.User;
import com.example.backend.entities.UserMeta;

@Repository
public interface UserMetaRepository extends JpaRepository<UserMeta, Integer>{
    UserMeta findByUser(User id);
    UserMeta findById(int userId);

}
