package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Depense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long somme;

    private String description;

    private LocalDate dateDepense;

    private Boolean isArchived = false;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boutique_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boutique boutique;

    public Depense(){}

    public Depense(Long id, Long somme, String description, LocalDate dateDepense, User author, Boutique boutique) {
        this.id = id;
        this.somme = somme;
        this.description = description;
        this.dateDepense = dateDepense;
        this.author = author;
        this.boutique = boutique;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSomme() {
        return somme;
    }

    public void setSomme(Long somme) {
        this.somme = somme;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDepense() {
        return dateDepense;
    }

    public void setDateDepense(LocalDate dateDepense) {
        this.dateDepense = dateDepense;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Boutique getBoutique() {
        return boutique;
    }

    public void setBoutique(Boutique boutique) {
        this.boutique = boutique;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }
}
