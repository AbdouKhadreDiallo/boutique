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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/boutique")
public class BoutiqueController {

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

    @PostMapping
    @PreAuthorize("hasRole('PROPRIETAIRE') or hasRole('GERANT')")
    public ResponseEntity<?> createBoutique(@Validated @RequestBody Boutique boutique){
        UserDetails userPrincipal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("connected"+userPrincipal);
        Boutique boutique1 = new Boutique();
        Long partGerant;
        Long partProprietaire;
        Long partDiallo;

        if (boutique.getPartDiallo()!=null){
            if (boutique.getPartGerant()!=null && boutique.getProprietaire()!=null){
                if (boutique.getCapital()!= (boutique.getPartDiallo()+boutique.getPartGerant()+boutique.getPartProprietaire())){
                    return ResponseEntity.badRequest().body(new MessageResponse("erreur de calcul"));
                }
            }
        }
        else{
            if (boutique.getPartGerant()!=null && boutique.getProprietaire()!=null){
                if (boutique.getCapital()!= (boutique.getPartGerant()+boutique.getPartProprietaire())){
                    return ResponseEntity.badRequest().body(new MessageResponse("erreur de calcul"));
                }
            }
        }

        if (proprietaireRepository.existsByUsername(userPrincipal.getUsername())){
            Optional<Proprietaire> proprietaire = proprietaireRepository.findByUsername(userPrincipal.getUsername());
            boutique1.setProprietaire(proprietaire.get());
            System.out.println("proprio ===> "+boutique.getProprietaire().getUsername());
            boutique1 = boutiqueRepository.save(boutique1);
            Set<Boutique> boutiques = new HashSet<>();
            Optional<Boutique> boutique2 = boutiqueRepository.findById(boutique1.getId());
            boutiques.add(boutique2.get());
            proprietaire.get().setBoutiques(boutiques);
            boutiques.add(boutique1);
            proprietaire.get().setBoutiques(boutiques);
            boutique1.setPartProprietaire(boutique.getPartProprietaire());
            proprietaire.get().setMoney(boutique.getPartProprietaire());
        }
        else{
            Optional<Gerant> gerant = gerantRepository.findByUsername(userPrincipal.getUsername());
            boutique1.setProprietaire(gerant.get());
            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findByName(ERole.ROLE_PROPRIETAIRE)
                    .orElseThrow(() -> new RuntimeException("Error: Role ."));
            Role oldRole = roleRepository.findByName(ERole.ROLE_GERANT)
                    .orElseThrow(() -> new RuntimeException("Error: Role ."));
            roles.add(userRole);
            roles.add(oldRole);
            gerant.get().setRoles(roles);
            boutique1 = boutiqueRepository.save(boutique1);
            //Boutique boutique2 = boutiqueRepository.getById(boutique1.getId());
            Set<Boutique> boutiques = new HashSet<>();
            Boutique boutique2 = boutiqueRepository.findById(boutique1.getId())
                    .orElseThrow(() -> new RuntimeException("No butique"));
            boutiques.add(boutique2);
            gerant.get().setBoutiques(boutiques);
            boutique1.setProprietaire(gerant.get());
            boutique1.setPartProprietaire(boutique.getPartProprietaire());
            gerant.get().setMoney(boutique.getPartProprietaire());
        }
        boutique1.setCapital(boutique.getCapital());
        if ( boutique.getCapital()==null || boutique.getPartProprietaire()==null || boutique.getPartGerant() == null){
            return ResponseEntity.badRequest().body(new MessageResponse("le capital ou les parts ne peuvent pas être null"));
        }

        if (boutique.getPartGerant() != null){
            boutique1.setPartGerant(boutique.getPartGerant());
            if (boutique.getGerant() == null){
                return ResponseEntity.badRequest().body(new MessageResponse("La part du gerant doit avoit un gérant"));
            }
            else{
                if (gerantRepository.existsByUsername(boutique.getGerant().getUsername())){
                    Optional<Gerant> gerant = gerantRepository.findByUsername(boutique.getGerant().getUsername());
                    if (gerant.get().getBoutique()!=null){
                        return ResponseEntity.badRequest().body(new MessageResponse("Ce gerant est indisponible"));
                    }
                    else {
                        boutique1.setGerant(gerant.get());
                        gerant.get().setBoutique(boutique1);
                    }
                }
                else{
                    Set<Role> roles = new HashSet<>();
                    Role userRole = roleRepository.findByName(ERole.ROLE_GERANT)
                            .orElseThrow(() -> new RuntimeException("Error: Role ."));
                    roles.add(userRole);
                    System.out.println("create gerant");
                    Gerant gerant = new Gerant(boutique.getGerant().getUsername(), "lildou68@gmail.com", encoder.encode("azerty"));
                    gerant.setRoles(roles);
                    gerant.setBoutique(boutique1);
                    gerant.setMoney(boutique.getPartGerant());
                    gerantRepository.save(gerant);
                    System.out.println("gerant ==> "+ gerant.getUsername());
                    boutique1.setGerant(gerant);
                }
            }

        }
        if (boutique.getDiallos()!=null){
            boutique1.setPartDiallo(boutique.getPartDiallo());
            if (boutique.getDiallos()==null){
                return ResponseEntity.badRequest().body(new MessageResponse("La part des Diallos doit avoit un Diallo"));
            }
            else{
                //boutiqueRepository.save(boutique1);
                Set<Role> roles = new HashSet<>();
                Role userRole = roleRepository.findByName(ERole.ROLE_DIALLO)
                        .orElseThrow(() -> new RuntimeException("Error: Role ."));
                roles.add(userRole);
                Set<Diallo> diallos = new HashSet<>();
                for (Diallo diallo: boutique.getDiallos()){
                    Diallo diallo1 = new Diallo();
                    if (diallo.getUsername()!=null){
                        diallo1.setUsername(diallo.getUsername());
                        diallo1.setRoles(roles);
                        diallo1.setBoutique(boutique1);
                        diallos.add(diallo1);
                        diallo1.setMoney(boutique.getPartDiallo());
                        dialloRepository.save(diallo1);
                        boutique1.setDiallos(diallos);
                        System.out.println("diallo ===> "+ diallo1.getUsername());

                    }
                }
            }
        }


        return ResponseEntity.ok(new MessageResponse("Boutique registered successfully!"));

    }

    @PutMapping(value = "/{boutiqueId}/addGerant")
    @PreAuthorize("hasRole('PROPRIETAIRE')")
    public ResponseEntity<?> addGerantInBoutique(@PathVariable("boutiqueId") Long boutiqueId, @Validated @RequestBody Boutique boutique){
        UserDetails userPrincipal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Proprietaire> proprietaire = proprietaireRepository.findByUsername(userPrincipal.getUsername());

        Boutique actualBoutique = boutiqueRepository.findById(boutiqueId).orElseThrow(()->new IllegalStateException("boutique with id"+boutiqueId+"does not exist"));
        System.out.println("connected "+ userPrincipal.getUsername());
        System.out.println("proprio "+actualBoutique.getProprietaire().getUsername());
        System.out.println("type connected "+ userPrincipal.getClass());
        System.out.println("type propri "+ actualBoutique.getProprietaire().getClass());
        if (!actualBoutique.getProprietaire().getUsername().equals(userPrincipal.getUsername())){
            return ResponseEntity.badRequest().body(new MessageResponse("Vous n'etes pas abilités gros"));
        }
        if (boutique.getGerant() == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Ou est le gérant idiot ?"));
        }
        if (actualBoutique.getGerant() != null){
            return ResponseEntity.badRequest().body(new MessageResponse("cette boutique a deja un gerant du nom de "+actualBoutique.getGerant().getFirstname()));
        }
        //if (gerantRepository.existsByUsername(boutique.getGerant().getUsername())){
        //    Optional<Gerant> gerant = gerantRepository.findByUsername(boutique.getGerant().getUsername());
        //    actualBoutique.setGerant(gerant.get());
        //    boutiqueRepository.save(actualBoutique);
        //    return ResponseEntity.ok().body(new MessageResponse("Ajouter bien bon"));
        //}
        else {
            Gerant gerant = new Gerant(boutique.getGerant().getUsername(), boutique.getGerant().getEmail(), encoder.encode("azerty"));
            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findByName(ERole.ROLE_GERANT)
                    .orElseThrow(() -> new RuntimeException("Error: Role ."));
            roles.add(userRole);
            gerant.setRoles(roles);
            gerant.setBoutique(actualBoutique);
            if (gerant instanceof Gerant){
                actualBoutique.setGerant(gerant);
            }
            gerantRepository.save(gerant);
            actualBoutique.setGerant(gerant);
            boutiqueRepository.save(actualBoutique);
            HashSet<Diallo> hmap = new HashSet();
            actualBoutique.setDiallos(hmap);
            boutiqueRepository.save(actualBoutique);
            return ResponseEntity.ok().body(new MessageResponse("Ajouter bien bon"));
        }



    }

    @PutMapping(value = "/{boutiqueId}/addDiallo")
    @PreAuthorize("hasRole('PROPRIETAIRE') or hasRole('GERANT') ")
    ResponseEntity<?> addDiallo(@PathVariable("boutiqueId") Long boutiqueId, @Validated @RequestBody Boutique boutique){
        UserDetails userPrincipal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<?> author = userRepository.findByUsername(userPrincipal.getUsername());
        Boutique actualBoutique = boutiqueRepository.findById(boutiqueId).orElseThrow(()->new IllegalStateException("boutique with id"+boutiqueId+"does not exist"));
        if(actualBoutique.getProprietaire() != author.get() && actualBoutique.getGerant()!=author.get()){
            return ResponseEntity.badRequest().body(new MessageResponse("Vous n'etes abiliter à faire cette action"));
        }
        Diallo newDiallo = new Diallo();
        Set<Diallo> diallos = new HashSet<>();
        newDiallo.setBoutique(actualBoutique);
        if (boutique.getDiallos()!=null){
            System.out.println("hello");
            for (Diallo diallo: boutique.getDiallos()){
                System.out.println(diallo.getUsername());
                newDiallo.setUsername(diallo.getUsername());
                Set<Role> roles = new HashSet<>();
                Role userRole = roleRepository.findByName(ERole.ROLE_DIALLO)
                        .orElseThrow(() -> new RuntimeException("Error: Role ."));
                roles.add(userRole);
                newDiallo.setRoles(roles);
                dialloRepository.save(newDiallo);
                diallos.add(newDiallo);

                actualBoutique.setDiallos(diallos);
                return ResponseEntity.ok(new MessageResponse("Diallo added successfully"));
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("something went wrong"));

    }

    @PutMapping(value = "/{boutiqueId}/depense")
    @PreAuthorize("hasRole('GERANT')")
    public ResponseEntity<?> depense(@PathVariable("boutiqueId") Long boutiqueId, @Validated @RequestBody Boutique boutique){
        Boutique actualBoutique = boutiqueRepository.findById(boutiqueId).orElseThrow(()->new IllegalStateException("boutique with id"+boutiqueId+"does not exist"));
        UserDetails userPrincipal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Gerant> gerant = gerantRepository.findByUsername(userPrincipal.getUsername());
        if (actualBoutique.getGerant() != gerant.get()){
            return ResponseEntity.badRequest().body(new MessageResponse("something went wrong"));
        }
        Set<Depense> depenses = new HashSet<>();
        Depense newDepense = new Depense();
        newDepense.setBoutique(actualBoutique);
        if (boutique.getDepenses() != null){
            for (Depense depense: boutique.getDepenses()){
                newDepense.setSomme(depense.getSomme());
                newDepense.setDateDepense(LocalDate.now());
                newDepense.setDescription(depense.getDescription());

                if (depense.getAuthor()!= null ){
                    if (proprietaireRepository.existsByUsername(depense.getAuthor().getUsername())){
                        Optional<Proprietaire> author = proprietaireRepository.findByUsername(depense.getAuthor().getUsername());
                        newDepense.setAuthor(author.get());
                        depenseRepository.save(newDepense);
                        depenses.add(newDepense);
                        author.get().setDepenses(depenses);
                        return ResponseEntity.ok(new MessageResponse("Depense added successfully"));
                    }
                    else{
                        Optional<Diallo> author = dialloRepository.findByUsername(depense.getAuthor().getUsername());
                        newDepense.setAuthor(author.get());
                        depenseRepository.save(newDepense);
                        depenses.add(newDepense);
                        author.get().setDepenses(depenses);
                        return ResponseEntity.ok(new MessageResponse("Depense added successfully"));
                    }
                }
                else{
                    newDepense.setAuthor(gerant.get());
                    depenseRepository.save(newDepense);
                    depenses.add(newDepense);
                    gerant.get().setDepenses(depenses);
                    return ResponseEntity.ok(new MessageResponse("Depense added successfully"));
                }
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong "));
    }

    @GetMapping(value = "/{boutiqueId}/diallo")
    @PreAuthorize("hasRole('PROPRIETAIRE') or hasRole('GERANT') ")
    public ResponseEntity<?> getDiallo(@PathVariable("boutiqueId") Long boutiqueId){
        Boutique actualBoutique = boutiqueRepository.findById(boutiqueId).orElseThrow(()->new IllegalStateException("boutique with id"+boutiqueId+"does not exist"));
        UserDetails userPrincipal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userPrincipal.getUsername().equals(actualBoutique.getProprietaire().getUsername()) || !userPrincipal.getUsername().equals(actualBoutique.getGerant().getUsername())){
            return ResponseEntity.badRequest().body(new MessageResponse("dell dougou foussa yoone nekk"));
        }
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        System.out.println("size ===>"+ actualBoutique.getDiallos().size());
        for(User boutique: actualBoutique.getDiallos()){
            if (dialloRepository.existsByUsername(boutique.getUsername())){
                HashMap<String, String> hmap = new HashMap<String, String>();
                hmap.put("boutique", String.valueOf(boutique.getId()));
                hmap.put("username", boutique.getUsername());
                hmap.put("Money", String.valueOf(boutique.getMoney()));
                arrayList.add(hmap);
            }

        }
            return ResponseEntity.ok().body(arrayList);
    }


    @GetMapping
    @PreAuthorize("hasRole('PROPRIETAIRE') or hasRole('GERANT') ")
    public ResponseEntity<?> getThemAll(){
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        UserDetails userPrincipal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (Boutique boutique: boutiqueRepository.findAll()){
            if (boutique.getProprietaire().getUsername().equals(userPrincipal.getUsername())){
                HashMap<String, String> boutiqueOutput = new HashMap<String, String>();
                boutiqueOutput.put("capital", String.valueOf(boutique.getCapital()));
                boutiqueOutput.put("part propriétaire", String.valueOf(boutique.getPartProprietaire()));
                boutiqueOutput.put("part gérant", String.valueOf(boutique.getPartGerant()));
                boutiqueOutput.put("part Diallo", String.valueOf(boutique.getPartDiallo()));
                arrayList.add(boutiqueOutput);
            }
            if (boutique.getGerant().getUsername().equals(userPrincipal.getUsername())){
                HashMap<String, String> boutiqueOutput = new HashMap<String, String>();
                boutiqueOutput.put("capital", String.valueOf(boutique.getCapital()));
                boutiqueOutput.put("part propriétaire", String.valueOf(boutique.getPartProprietaire()));
                boutiqueOutput.put("part gérant", String.valueOf(boutique.getPartGerant()));
                boutiqueOutput.put("part Diallo", String.valueOf(boutique.getPartDiallo()));
                boutiqueOutput.put("Diallo", "hey");
                arrayList.add(boutiqueOutput);
            }
        }
        return ResponseEntity.ok().body(arrayList);
    }








}