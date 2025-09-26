package org.example.repository;

import org.example.entities.User;

public interface UserRepository {

        boolean save(User user);
        User findByEmail(String email);
        User findByUsername(String username);
}
