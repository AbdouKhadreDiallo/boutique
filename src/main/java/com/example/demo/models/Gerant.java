package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Gerant")
public class Gerant extends User {


    @OneToOne( fetch = FetchType.EAGER,cascade = CascadeType.ALL, optional = true)
    @JsonIgnore
    private Boutique boutique;

    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Boutique> boutiques = new HashSet<>();

    public Gerant() {
    }

    public Boutique getBoutique() {
        return boutique;
    }

    public void setBoutique(Boutique boutique) {
        this.boutique = boutique;
    }

    public Set<Boutique> getBoutiques() {
        return boutiques;
    }

    public void setBoutiques(Set<Boutique> boutiques) {
        this.boutiques = boutiques;
    }

    public Gerant(String username, String email, String password) {
        super(username, email, password);
    }
}
