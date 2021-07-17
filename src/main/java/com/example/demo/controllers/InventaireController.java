package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventaire")
public class InventaireController {
    @Autowired
    BoutiqueRepository boutiqueRepository;

    @Autowired
    InventaireRepository inventaireRepository;

    @Autowired
    GerantRepository gerantRepository;

    @Autowired
    DialloRepository dialloRepository;

    @Autowired
    RoleRepository roleRepository;

    Authentication authentication;

    @PostMapping
    @PreAuthorize("hasRole('GERANT')")
    ResponseEntity<?> inventaire(@Validated @RequestBody Inventaire inventaire){
        //Inventaire lastInventaire = inventaireRepository.findTopByOrderByIdDesc();
        //System.out.println(lastInventaire.getId());

        UserDetails actuallyConnected = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!gerantRepository.existsByUsername(actuallyConnected.getUsername())){
            return ResponseEntity.notFound().build();
        }
        Gerant gerant = gerantRepository.findByUsername(actuallyConnected.getUsername()).orElseThrow(()-> new IllegalStateException("something wrong"));
        Boutique boutique = gerant.getBoutique();
        Inventaire newInventaire = new Inventaire();
        newInventaire.setDate(new Date());
        newInventaire.setMoneyIn(inventaire.getMoneyIn());
        Long depenses=0L;
        for (Depense depense: boutique.getDepenses()){
            if (!depense.getArchived()) depenses += depense.getSomme();

        }
        System.out.println("all depenses===>"+ depenses);
        newInventaire.setAllDepenses(depenses);
        Long total = 0L;
        total+=inventaire.getMoneyIn()+depenses;
        System.out.println("total money==>"+total);
        User user = boutique.getProprietaire();
        Proprietaire proprietaire = null;
        if (user instanceof Proprietaire){
            proprietaire = (Proprietaire) user;
        }
        System.out.println("proprio ==>"+proprietaire.getUsername());
        Long proprietaireDepnse= 0L;
        Long gerantDepense = 0L;
        Long dialloDepense = 0L;

        if (proprietaire.getDepenses().size()>0){
            for (Depense depense: proprietaire.getDepenses()){
                if (depense.getBoutique() == boutique){
                    proprietaireDepnse = proprietaireDepnse + depense.getSomme();
                }
            }
        }
        if (gerant.getDepenses().size()>0){
            for (Depense depense: gerant.getDepenses()){
                gerantDepense = gerantDepense + depense.getSomme();
            }
        }

        System.out.println("depenses gerant "+gerantDepense);
        System.out.println("depenses prorio -->"+ proprietaireDepnse);

        Long partGerant = (((total-boutique.getCapital())-inventaire.getPart_Diallo())/2) - gerantDepense;
        Long partProprietaire = (((total-boutique.getCapital())-inventaire.getPart_Diallo())/2) - proprietaireDepnse;
        System.out.println("part proprietaire ==>"+partProprietaire);
        System.out.println("part gerant ===>"+partGerant);
        Long partDiallo = 0L;
        newInventaire.setBenefice(total-boutique.getCapital());
        for (User diallo: boutique.getDiallos()){
            if (dialloRepository.existsByUsername(diallo.getUsername())){
                for (Depense depense: diallo.getDepenses()){
                    dialloDepense = dialloDepense+depense.getSomme();
                }
                //diallo.setMoney((diallo.getMoney() + inventaire.getPart_Diallo()) - dialloDepense);
                partDiallo = (partDiallo + inventaire.getPart_Diallo()) - dialloDepense;
            }
        }
        System.out.println("diallo depenses ==>"+ dialloDepense);
        System.out.println("part Diallo ===>"+ partDiallo);

        System.out.println("part Diallo before ===>"+ boutique.getPartDiallo());
        newInventaire.setPart_Diallo(partDiallo);
        newInventaire.setPart_Gerant(partGerant);
        newInventaire.setBoutique(boutique);
        newInventaire.setPart_Propretaire(partProprietaire);
        System.out.println("proprio money before ===>"+ proprietaire.getMoney());
        proprietaire.setMoney(proprietaire.getMoney()+partProprietaire);
        System.out.println("gerant money ===>"+ gerant.getMoney());
        gerant.setMoney(gerant.getMoney()+partGerant);

        System.out.println("proprio money ===>"+ proprietaire.getMoney());
        System.out.println("gerant money ===>"+ gerant.getMoney());
        boutique.setCapital(boutique.getCapital()+partProprietaire+partGerant+partDiallo);
        boutique.setPartDiallo(boutique.getPartDiallo() + partDiallo);
        boutique.setPartProprietaire(boutique.getPartProprietaire() + partProprietaire);
        boutique.setPartGerant(boutique.getPartGerant() + partGerant);
        inventaireRepository.save(newInventaire);
        for (Depense depense: boutique.getDepenses()){
            if (depense.getArchived()==false){
                depense.setArchived(true);
            }
        }
        return ResponseEntity.ok().body(newInventaire);
    }

    @GetMapping
    @PreAuthorize("hasRole('GERANT')")
    ResponseEntity<?> inventaires(){
        UserDetails actuallyConnected = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Gerant> gerant = gerantRepository.findByUsername(actuallyConnected.getUsername());
        if (!gerant.isPresent()){
            return ResponseEntity.notFound().build();
        }
        ArrayList<HashMap<String, String>> inventaires = new ArrayList<>();
        for (Inventaire inventaire : inventaireRepository.findAll()){
            if (gerant.get().getBoutique() == inventaire.getBoutique()){
                HashMap<String, String> invent = new HashMap<String, String>();
                invent.put("all depenses", String.valueOf(inventaire.getAllDepenses()));
                invent.put("Benefice", String.valueOf(inventaire.getAllDepenses()));
                invent.put("date", String.valueOf(inventaire.getDate()));
                invent.put("statut", String.valueOf(inventaire.getInventaireStatus()));
                invent.put("entrée d'argent", String.valueOf(inventaire.getMoneyIn()));
                invent.put("part Diallo", String.valueOf(inventaire.getPart_Diallo()));
                invent.put("part Gérant", String.valueOf(inventaire.getPart_Gerant()));
                invent.put("part proprio", String.valueOf(inventaire.getPart_Propretaire()));

                inventaires.add(invent);
            }
        }
        return ResponseEntity.ok().body(inventaires);
    }
}
