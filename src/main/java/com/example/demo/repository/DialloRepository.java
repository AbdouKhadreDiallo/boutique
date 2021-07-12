package com.example.demo.repository;

import com.example.demo.models.Diallo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DialloRepository extends JpaRepository<Diallo, Long> {
    Optional<Diallo> findByUsername(String username);
    Boolean existsByUsername(String username);
}
