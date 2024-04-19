package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entities.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Integer> {
    VerificationCode findByCodeAndEmail(String code, String email);

    VerificationCode findByEmail(String email);
}
