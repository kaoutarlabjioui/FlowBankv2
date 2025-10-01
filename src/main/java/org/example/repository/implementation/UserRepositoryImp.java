package org.example.repository.implementation;

import org.example.entities.Role;
import org.example.entities.User;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.example.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

    public class UserRepositoryImp implements UserRepository {
    private Connection connection;
    private DatabaseConnection db;
    private RoleRepository roleRepository;

    public UserRepositoryImp(RoleRepository roleRepository) {
        db = DatabaseConnection.getInstance();
        connection = db.getConnection();
        this.roleRepository = roleRepository;
    }

    public boolean save(User user) {

        try {
            if (user.getId() == null) {
                user.setId(UUID.randomUUID());
            }

            String query = "INSERT INTO users (id, username, nom, prenom, email, password, role_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setObject(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getNom());
            stmt.setString(4, user.getPrenom());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPassword());
            stmt.setInt(7, user.getRole().getId());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {

            System.out.println("Error saving user :" + e.getMessage());
            e.printStackTrace();

            return false;
        }
    }

    public User findByEmail(String email) {
        try {
            String query = "SELECT * FROM users WHERE email = ?";

            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                int roleId = result.getInt("role_id");
                Role role = roleRepository.findById(roleId);


                User user = new User(
                        result.getString("username"),
                        result.getString("nom"),
                        result.getString("prenom"),
                        result.getString("email"),
                        result.getString("password"),
                        role
                );
                user.setId(result.getObject("id", java.util.UUID.class));
                return user;
            }

        } catch (Exception e) {
            System.err.println("Error finding user by username: " + e.getMessage());
        }
        return null;
    }

    public User findByUsername(String username) {
        try {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, username);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                int roleId = result.getInt("role_id");
                Role role = roleRepository.findById(roleId);
                User user = new User(
                        result.getString("username"),
                        result.getString("nom"),
                        result.getString("prenom"),
                        result.getString("email"),
                        result.getString("password"),
                        role
                );
                user.setId(result.getObject("id", java.util.UUID.class));
                return user;
            }


        } catch (SQLException e) {
            System.out.println("Error finding user by username : " + e.getMessage());
        }

        return null;
    }


        public User findById(UUID id) {
            try {
                String sql = "SELECT * FROM users WHERE id = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setObject(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int roleId = rs.getInt("role_id");
                    Role role = roleRepository.findById(roleId);
                    return new User(
                            rs.getString("username"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("password"),
                            role
                    );
                }
            } catch (Exception e) {
                System.err.println("Error finding teller: " + e.getMessage());
            }
            return null;
        }









}


