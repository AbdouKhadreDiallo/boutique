package com.example.demo.repository;

import com.example.demo.models.Proprietaire;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProprietaireRepository extends JpaRepository<Proprietaire, Long> {
    Optional<Proprietaire> findByUsername(String username);
    Boolean existsByUsername(String username);
}
