package org.example.service;

import org.example.entities.Role;
import org.example.entities.User;

public interface AuthService {
  public boolean register(String prenom, String nom, String email, String password, Role role);
  public User login(String username , String password);

}
