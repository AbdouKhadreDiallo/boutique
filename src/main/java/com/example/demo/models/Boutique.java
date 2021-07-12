package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Boutique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String addresse;
    private Long capital;
    private Long partProprietaire;
    private Long partGerant;
    private Long partDiallo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proprietaire_id")
    private User proprietaire;


    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "gerant_id")
    private Gerant gerant;

    @OneToMany(mappedBy = "boutique", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<Depense> depenses = new HashSet<>();

    @OneToMany(mappedBy = "boutique", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Gerant.class)
    private Set<Diallo> diallos = new HashSet<>();

    @OneToMany(mappedBy = "boutique", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<Inventaire> inventaires = new HashSet<>();


    public Gerant getGerant() {
        return gerant;
    }

    public void setGerant(Gerant gerant) {
        this.gerant = gerant;
    }

    public User getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(User proprietaire) {
        this.proprietaire = proprietaire;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddresse() {
        return addresse;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public Long getCapital() {
        return capital;
    }

    public Set<Depense> getDepenses() {
        return depenses;
    }

    public void setDepenses(Set<Depense> depenses) {
        this.depenses = depenses;
    }

    public Set<Diallo> getDiallos() {
        return diallos;
    }

    public void setDiallos(Set<Diallo> diallos) {
        this.diallos = diallos;
    }

    public Set<Inventaire> getInventaires() {
        return inventaires;
    }

    public void setInventaires(Set<Inventaire> inventaires) {
        this.inventaires = inventaires;
    }

    public void setCapital(Long capital) {
        this.capital = capital;
    }

    public Long getPartProprietaire() {
        return partProprietaire;
    }

    public void setPartProprietaire(Long partProprietaire) {
        this.partProprietaire = partProprietaire;
    }

    public Long getPartGerant() {
        return partGerant;
    }

    public void setPartGerant(Long partGerant) {
        this.partGerant = partGerant;
    }

    public Long getPartDiallo() {
        return partDiallo;
    }

    public void setPartDiallo(Long partDiallo) {
        this.partDiallo = partDiallo;
    }

    public Boutique(){}

    public Boutique(Long id, String addresse, Long capital, Long partProprietaire, Long partGerant, Long partDiallo, Proprietaire proprietaire) {
        this.id = id;
        this.addresse = addresse;
        this.capital = capital;
        this.partProprietaire = partProprietaire;
        this.partGerant = partGerant;
        this.partDiallo = partDiallo;
        this.proprietaire = proprietaire;
    }

    public Boutique(Long id, String addresse, Long capital, Long partProprietaire, Long partGerant, Long partDiallo) {
        this.id = id;
        this.addresse = addresse;
        this.capital = capital;
        this.partProprietaire = partProprietaire;
        this.partGerant = partGerant;
        this.partDiallo = partDiallo;
    }

}
