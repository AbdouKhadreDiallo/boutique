package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Proprietaire")
public class Proprietaire extends User{
    private String adresse;

    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL)
    private Set<Boutique> boutiques = new HashSet<>();

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public Proprietaire(){

    }

    public Set<Boutique> getBoutiques() {
        return boutiques;
    }

    public void setBoutiques(Set<Boutique> boutiques) {
        this.boutiques = boutiques;
    }

    public Proprietaire(Long id, String username, String email, String password, Set<Role> roles, String adresse, Set<Boutique> boutiques) {
        super(id, username, email, password, roles);
        this.adresse = adresse;
        this.boutiques = boutiques;
    }

    public Proprietaire(String username, String email, String password) {
        super(username, email, password);
    }

    public Proprietaire(Long id, String username, String email, String password, Set<Role> roles) {
        super(id, username, email, password, roles);
    }

    public Proprietaire(Long id, String username, String email, String firstname, String lastname, Long money, String password, Set<Role> roles, String adresse) {
        super(id, username, email, firstname, lastname, money, password, roles);
        this.adresse = adresse;
    }

}
