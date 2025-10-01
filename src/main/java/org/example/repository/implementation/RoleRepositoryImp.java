package org.example.repository.implementation;

import org.example.entities.Role;
import org.example.repository.RoleRepository;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleRepositoryImp implements RoleRepository {
 private Connection connection;

 public RoleRepositoryImp(){
     DatabaseConnection db = DatabaseConnection.getInstance();
     connection = db.getConnection();
 }

  public List<Role> findAll(){


      List<Role> roles = new ArrayList<>();

      String query = "SELECT * FROM roles WHERE role_name <> 'ADMIN'";

      try(Statement stmt = connection.createStatement()){
          ResultSet resultSet = stmt.executeQuery(query);

          while(resultSet.next()){

              Role role = new Role(resultSet.getInt("id"),
                      resultSet.getString("role_name"),
                      resultSet.getString("description"));

              roles.add(role);


          }

      } catch (SQLException e){
          System.out.println("failed to retrieves roles ");
      }
      return roles;
    }

    public Role findById(int id) {
        try {
            String sql = "SELECT * FROM roles WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Role(
                        rs.getInt("id"),
                        rs.getString("role_name"),
                        rs.getString("description")
                );
            }
        } catch (Exception e) {
            System.err.println("Error finding role: " + e.getMessage());
        }
        return null;
    }




  /*  public static void main(String[] args){
RoleRepositoryImp roles= new RoleRepositoryImp();
        System.out.println(roles.findAll());




    }*/



}
