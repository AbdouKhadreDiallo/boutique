package com.example.demo.controllers;

import com.example.demo.models.Depense;
import com.example.demo.models.User;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    DialloRepository dialloRepository;

    @Autowired
    ProprietaireRepository proprietaireRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoutiqueRepository boutiqueRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    GerantRepository gerantRepository;

    @Autowired
    DepenseRepository depenseRepository;

    Authentication authentication;

    @GetMapping(value = "/MyDepenses")
    @PreAuthorize("hasRole('PROPRIETAIRE') or hasRole('GERANT') ")
    public ResponseEntity<?> getMyDepenses(){
        UserDetails userPrincipal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findByUsername(userPrincipal.getUsername());
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        for (Depense depense:user.get().getDepenses()){
            HashMap<String, String> depenses = new HashMap<String, String>();
            depenses.put("somme", String.valueOf(depense.getSomme()));
            depenses.put("description", depense.getDescription());
            depenses.put("date", String.valueOf(depense.getDateDepense()));
            depenses.put("boutique", depense.getBoutique().getName());
            arrayList.add(depenses);
        }
        return  ResponseEntity.ok().body(arrayList);
    }

    @GetMapping(value = "/myMoney")
    @PreAuthorize("hasRole('PROPRIETAIRE') or hasRole('GERANT')")
    public ResponseEntity<?> getMyMoney(){
        UserDetails userPrincipal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findByUsername(userPrincipal.getUsername());
        return ResponseEntity.ok().body(user.get().getMoney());
    }

    @GetMapping(value = "/myInformations")
    @PreAuthorize("hasRole('PROPRIETAIRE') or hasRole('GERANT')")
    public ResponseEntity<?> show (){
        UserDetails userPrincipal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findByUsername(userPrincipal.getUsername());
        return  ResponseEntity.ok().body(user.get());
    }

    @PutMapping(value = "/{userId}")
    @PreAuthorize("hasRole('PROPRIETAIRE') or hasRole('GERANT')")
    public ResponseEntity<?> updateUser(@PathVariable("userId") Long userId, @Validated @RequestBody User uer){
        return  ResponseEntity.ok().body(new MessageResponse(""));
    }
}
