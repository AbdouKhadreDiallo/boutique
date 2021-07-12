package com.example.demo.repository;

import com.example.demo.models.Gerant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GerantRepository extends JpaRepository<Gerant, Long> {
    Optional<Gerant> findByUsername(String username);
    Boolean existsByUsername(String username);
}
