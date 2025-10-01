package org.example.repository;

import org.example.entities.User;

import java.util.UUID;

public interface UserRepository {

        boolean save(User user);
        User findByEmail(String email);
        User findByUsername(String username);
        User findById(UUID id);
}
