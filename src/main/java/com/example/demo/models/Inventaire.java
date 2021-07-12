package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Inventaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;

    private Long moneyIn;

    private Long allDepenses;

    private Long benefice;

    private Long part_Propretaire;

    private Long part_Gerant;

    private Long part_Diallo;

    private String inventaireStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boutique_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boutique boutique;

    public Inventaire() {
    }


    public Inventaire(Long id, Date date, Long moneyIn, Long allDepenses, Long benefice, Long part_Propretaire, Long part_Gerant, Long part_Diallo, String inventaireStatus) {
        this.id = id;
        this.date = date;
        this.moneyIn = moneyIn;
        this.allDepenses = allDepenses;
        this.benefice = benefice;
        this.part_Propretaire = part_Propretaire;
        this.part_Gerant = part_Gerant;
        this.part_Diallo = part_Diallo;
        this.inventaireStatus = inventaireStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getMoneyIn() {
        return moneyIn;
    }

    public void setMoneyIn(Long moneyIn) {
        this.moneyIn = moneyIn;
    }

    public Long getAllDepenses() {
        return allDepenses;
    }

    public void setAllDepenses(Long allDepenses) {
        this.allDepenses = allDepenses;
    }

    public Long getBenefice() {
        return benefice;
    }

    public void setBenefice(Long benefice) {
        this.benefice = benefice;
    }

    public Long getPart_Propretaire() {
        return part_Propretaire;
    }

    public void setPart_Propretaire(Long part_Propretaire) {
        this.part_Propretaire = part_Propretaire;
    }

    public Long getPart_Gerant() {
        return part_Gerant;
    }

    public void setPart_Gerant(Long part_Gerant) {
        this.part_Gerant = part_Gerant;
    }

    public Long getPart_Diallo() {
        return part_Diallo;
    }

    public void setPart_Diallo(Long part_Diallo) {
        this.part_Diallo = part_Diallo;
    }

    public String getInventaireStatus() {
        return inventaireStatus;
    }

    public void setInventaireStatus(String inventaireStatus) {
        this.inventaireStatus = inventaireStatus;
    }

    public Boutique getBoutique() {
        return boutique;
    }

    public void setBoutique(Boutique boutique) {
        this.boutique = boutique;
    }
}
