package com.example.demo.controllers;


import com.example.demo.models.ERole;
import com.example.demo.models.Proprietaire;
import com.example.demo.models.Role;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.ProprietaireRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/proprietaire")
public class PropritaireController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ProprietaireRepository proprietaireRepository;

    @PostMapping
    public ResponseEntity<?> addProprietaire(@Validated @RequestBody SignupRequest proprietaire){
        if (userRepository.existsByUsername(proprietaire.getUsername())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken! \n or maybe you already have an account; Please Log in"));
        }
        if (userRepository.existsByEmail(proprietaire.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        Proprietaire proprietaire1 =new Proprietaire(
                proprietaire.getUsername(),
                proprietaire.getEmail(),
                encoder.encode(proprietaire.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_PROPRIETAIRE)
                .orElseThrow(() -> new RuntimeException("Error: Role ."));
        roles.add(userRole);
        proprietaire1.setRoles(roles);
        proprietaireRepository.save(proprietaire1);
        return ResponseEntity.ok(new MessageResponse("Proprietaire registered successfully!"));
    }
}
