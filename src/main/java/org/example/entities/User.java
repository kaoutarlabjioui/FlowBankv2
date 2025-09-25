package org.example.entities;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {

    private UUID id;
    private String username;
    private String nom;
    private String prenom;
    private String email;
    private String passwordHash;
    private LocalDateTime createdAt;
    private Role role;

    public User(UUID id, String username, String nom, String prenom, String email,
                String passwordHash, LocalDateTime createdAt, Role role) {
        this.id = id;
        this.username = username;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.role = role;
    }


    public UUID getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getNom() {
        return nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public String getEmail() {
        return email;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public Role getRole() {
        return role;
    }




    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role.getRoleName() +
                '}';
    }

}
