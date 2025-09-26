package org.example.repository;

import org.example.entities.Role;

import java.util.List;

public interface RoleRepository {

    List<Role> findAll();
    Role findById(int id);
}
