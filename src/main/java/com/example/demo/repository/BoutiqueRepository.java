package com.example.demo.repository;

import com.example.demo.models.Boutique;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoutiqueRepository extends JpaRepository<Boutique, Long> {
}
