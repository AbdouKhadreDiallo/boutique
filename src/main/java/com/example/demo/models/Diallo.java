package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@DiscriminatorValue("Diallo")
public class Diallo extends User{
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "boutique_id")
    private Boutique boutique;
    public Diallo() {
    }

    public Boutique getBoutique() {
        return boutique;
    }

    public void setBoutique(Boutique boutique) {
        this.boutique = boutique;
    }

    public Diallo(String username, String email, String password) {
        super(username, email, password);
    }
}
