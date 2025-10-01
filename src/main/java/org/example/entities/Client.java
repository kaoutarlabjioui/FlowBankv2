package org.example.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Client {

    private UUID id;
    private String cin;
    private String nom;
    private String prenom;
    private String tele;
    private String adresse;
    private LocalDate dateNaissance;
    private BigDecimal salaire;
    private LocalDateTime createdAt;
    private UUID createdBy;

    // Constructeur par défaut
    public Client() {}
    // Constructeur avec paramètres
    public Client( String cin, String nom, String prenom, String tele, String adresse, LocalDate dateNaissance, BigDecimal salaire, UUID createdBy) {

        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.tele = tele;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.salaire = salaire;
        this.createdBy = createdBy;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getCin() {
        return cin;
    }
    public void setCin(String cin) {
        this.cin = cin;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public String getTele() {
        return tele;
    }
    public void setTele(String tele) {
        this.tele = tele;
    }
    public String getAdresse() {
        return adresse;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public LocalDate getDateNaissance() {
        return dateNaissance;
    }
    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
    public BigDecimal getSalaire() {
        return salaire;
    }
    public void setSalaire(BigDecimal salaire) {
        this.salaire = salaire;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public UUID getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }
    // toString()
    @Override
    public String toString() {
        return "Client{" + "id=" + id +
                ", cin='" + cin + '\'' + ", nom='"
                + nom + '\'' + ", prenom='" + prenom +
                '\'' + ", tele='" + tele + '\'' +
                ", adresse='" + adresse + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", salaire=" + salaire +
                ", createdAt=" + createdAt + ", createdBy=" + createdBy + '}';
    }

















}
