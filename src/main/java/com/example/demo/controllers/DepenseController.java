package com.example.demo.controllers;

import com.example.demo.repository.BoutiqueRepository;
import com.example.demo.repository.GerantRepository;
import com.example.demo.repository.ProprietaireRepository;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/depense")
public class DepenseController {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ProprietaireRepository proprietaireRepository;

    @Autowired
    BoutiqueRepository boutiqueRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    GerantRepository gerantRepository;

    Authentication authentication;
}
