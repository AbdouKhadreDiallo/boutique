package com.example.demo.repository;

import com.example.demo.models.Depense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepenseRepository extends JpaRepository<Depense, Long> {
}
