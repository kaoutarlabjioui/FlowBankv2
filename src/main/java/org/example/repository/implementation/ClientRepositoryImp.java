package org.example.repository.implementation;

import org.example.entities.Client;
import org.example.entities.Role;
import org.example.entities.User;
import org.example.repository.ClientRepository;
import org.example.repository.UserRepository;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientRepositoryImp implements ClientRepository {
    private DatabaseConnection db ;
    private Connection connection;
    private UserRepository userRepository;

    public ClientRepositoryImp(UserRepository userRepository){
        db = DatabaseConnection.getInstance();
        connection = db.getConnection();
        this.userRepository = userRepository;
     }

    public void save( Client client){

        if (client.getId() == null) {
            client.setId(UUID.randomUUID());
        }

       String query = "INSERT INTO clients (id,cin,nom,prenom,tele,adresse,date_naissance,salaire,created_by) values(?,?,?,?,?,?,?,?,?)";

        try{
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setObject(1, client.getId());
            stmt.setString(2, client.getCin());
            stmt.setString(3, client.getNom());
            stmt.setString(4, client.getPrenom());
            stmt.setString(5, client.getTele());
            stmt.setString(6, client.getAdresse());
            stmt.setDate(7, Date.valueOf(client.getDateNaissance()));
            stmt.setBigDecimal(8, client.getSalaire());
            stmt.setObject(9, client.getCreatedBy());

             stmt.executeUpdate();


        } catch (SQLException e){

            System.out.println("Error saving user :" + e.getMessage());
            e.printStackTrace();
        }




    }
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                clients.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clients;
    }

    public Client findByCin( String cin){
        try{
            String query = "SELECT * FROM clients WHERE cin = ?";
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1,cin);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                UUID tellerId = (UUID) rs.getObject("created_by");
                User teller = userRepository.findById(tellerId);
                Client client = new Client(
                        rs.getString("cin"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("tele"),
                        rs.getString("adresse"),
                        rs.getDate("date_naissance").toLocalDate(),
                        rs.getBigDecimal("salaire"),
                        (UUID) rs.getObject("created_by")
                );


                client.setId((UUID) rs.getObject("id"));
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    client.setCreatedAt(ts.toLocalDateTime());
                }

                return client;
            }


        }catch(SQLException e){
            System.out.println(e);
        }


        return null;
    }

    private Client mapRow(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId((UUID) rs.getObject("id"));
        client.setCin(rs.getString("cin"));
        client.setNom(rs.getString("nom"));
        client.setPrenom(rs.getString("prenom"));
        client.setTele(rs.getString("tele"));
        client.setAdresse(rs.getString("adresse"));
        client.setDateNaissance(rs.getDate("date_naissance").toLocalDate());
        client.setSalaire(rs.getBigDecimal("salaire"));
        client.setCreatedBy((UUID) rs.getObject("created_by"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            client.setCreatedAt(ts.toLocalDateTime());
        }
        return client;
    }


}
