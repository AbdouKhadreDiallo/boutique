package com.example.demo.repository;

import com.example.demo.models.Inventaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventaireRepository extends JpaRepository<Inventaire, Long> {
    Inventaire findTopByOrderByIdDesc();
}
