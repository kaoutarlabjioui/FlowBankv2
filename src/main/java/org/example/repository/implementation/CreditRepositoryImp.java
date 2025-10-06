package org.example.repository.implementation;

import org.example.entities.Credit;
import org.example.entities.enums.CreditStatus;
import org.example.repository.CreditRepository;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CreditRepositoryImp implements CreditRepository {

   private DatabaseConnection db;
  private  Connection connection;

    public CreditRepositoryImp(){
        db= DatabaseConnection.getInstance();
        connection = db.getConnection();
    }



    public void save (Credit credit){

        if(credit.getId()==null){
            credit.setId(UUID.randomUUID());
        }

        String query = "INSERT INTO credits ( id,  montant,  duree_mois, taux_interet,type_interet,  start_date,  end_date,status,  monthly_payment,  account_id) VALUES (?,?,?,?,?::interest_type,?,?,?::credit_status,?,?)";

        try{
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setObject(1,credit.getId());
            stmt.setBigDecimal(2, credit.getMontant());
            stmt.setInt(3, credit.getDureeMois());
            stmt.setBigDecimal(4, credit.getTauxInteret());
            stmt.setString(5, credit.getTypeInteret().name().toLowerCase());
            stmt.setDate(6, Date.valueOf(credit.getStartDate()));
            stmt.setDate(7, Date.valueOf(credit.getEndDate()));
            stmt.setString(8, credit.getStatus().name());
            stmt.setBigDecimal(9, credit.getMonthlyPayment());
            stmt.setObject(10, credit.getAccountId());

            stmt.executeUpdate();

            System.out.println("Credit enregistré avec succès dans la base.");


        }catch(SQLException e){
            System.out.println(" Erreur lors de l'enregistrement du crédit : " +e.getMessage());
        }



    }
    @Override
    public Optional<Credit> findById(UUID id) {
        String sql = "SELECT * FROM credits WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding credit", e);
        }
        return Optional.empty();
    }


    public List<Credit> findAll() {
        List<Credit> credits = new ArrayList<>();
        String sql = "SELECT * FROM credits";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                credits.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all credits", e);
        }
        return credits;
    }
    @Override
    public List<Credit> findByAccountId(UUID accountId) {
        List<Credit> credits = new ArrayList<>();
        String sql = "SELECT * FROM credits WHERE account_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, accountId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                credits.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding credits by account id", e);
        }
        return credits;
    }

    public List<Credit> findByStatus(CreditStatus status) {
        List<Credit> credits = new ArrayList<>();
        String sql = "SELECT * FROM credits WHERE status = ?::credit_status";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                credits.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding credits by status", e);
        }
        return credits;
    }

    public void updateStatus(UUID id, CreditStatus status) {
        String sql = "UPDATE credits SET status = ?::credit_status WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setObject(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating credit status", e);
        }
    }


    private Credit mapRow(ResultSet rs) throws SQLException {
        Credit credit = new Credit();
        credit.setId((UUID) rs.getObject("id"));
        credit.setMontant(rs.getBigDecimal("montant"));
        credit.setDureeMois(rs.getInt("duree_mois"));
        credit.setTauxInteret(rs.getBigDecimal("taux_interet"));
        credit.setTypeInteret(Enum.valueOf(org.example.entities.enums.InterestType.class, rs.getString("type_interet").toUpperCase()));
        credit.setStartDate(rs.getDate("start_date").toLocalDate());
        credit.setEndDate(rs.getDate("end_date").toLocalDate());
        credit.setStatus(Enum.valueOf(CreditStatus.class, rs.getString("status")));
        credit.setMonthlyPayment(rs.getBigDecimal("monthly_payment"));
        credit.setAccountId((UUID) rs.getObject("account_id"));
        return credit;
    }

}
