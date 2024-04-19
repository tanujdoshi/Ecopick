package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entities.Wallet;
import java.util.*;

import org.springframework.data.domain.Sort;
import com.example.backend.entities.User;
import com.example.backend.entities.UserMeta;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Wallet findByUserId(UserMeta userMeta);

    List<Wallet> findAllByUserId(Integer userId, Sort sort);

    Wallet findByUser(User user);
}
