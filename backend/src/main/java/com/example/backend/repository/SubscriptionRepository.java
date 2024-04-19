package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.entities.Days;
import com.example.backend.entities.Subscription;
import com.example.backend.entities.User;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    List<Subscription> findAllByUserIdAndProductId(int userId, int productId);

    List<Subscription> findByDays(Days days);

    List<Subscription> findByUser(User user);

    void deleteByUserIdAndProductId(int id, int id2);

}