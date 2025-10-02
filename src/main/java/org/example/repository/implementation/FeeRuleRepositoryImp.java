package org.example.repository.implementation;

import org.example.entities.FeeRule;
import org.example.entities.enums.Currency;
import org.example.entities.enums.FeeMode;
import org.example.entities.enums.TransactionType;
import org.example.repository.FeeRuleRepository;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FeeRuleRepositoryImp implements FeeRuleRepository {
    DatabaseConnection db;
    Connection connection;

    public FeeRuleRepositoryImp(){
        db = DatabaseConnection.getInstance();
        connection = db.getConnection();
    }

 public FeeRule save(FeeRule feeRule){
        if(feeRule.getId()== null){
            feeRule.setId(UUID.randomUUID());
        }
        String query ="INSERT INTO fee_rules (id,operation_type,mode,value,currency,is_active,created_at) VALUES (?,?::transaction_type,?::fee_mode,?,?::currency,?,?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setObject(1, feeRule.getId());
            stmt.setString(2, feeRule.getOperationType().name().toLowerCase());
            stmt.setString(3, feeRule.getMode().name());
            stmt.setBigDecimal(4, feeRule.getValue());
            stmt.setString(5, feeRule.getCurrency().name());
            stmt.setBoolean(6, feeRule.isActive());
            stmt.setTimestamp(7, Timestamp.valueOf(feeRule.getCreatedAt() != null ? feeRule.getCreatedAt() : LocalDateTime.now()));
            stmt.executeUpdate();
            return feeRule;
        }catch(SQLException e){
            System.out.println("Error saving FeeRule : " + e.getMessage());
            e.printStackTrace();
            return null;
        }

 }

 public Optional<FeeRule> findById(UUID id){
        String query = "select * from fee_rules where id = ?";

        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setObject(1,id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                FeeRule feeRule = mapResultSet(rs);
                return Optional.of(feeRule);
            }
        } catch (SQLException e){
            System.out.println("Error finding FeeRule : " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
 }

    public List<FeeRule> findAll() {
        List<FeeRule> list = new ArrayList<>();
        String query = "SELECT * FROM fee_rules";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching FeeRules: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }


    public void update(FeeRule feeRule) {
        String query = "UPDATE fee_rules SET operation_type = ?::transaction_type, mode = ?::fee_mode, " +
                "value = ?, currency = ?::currency, is_active = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, feeRule.getOperationType().name());
            stmt.setString(2, feeRule.getMode().name());
            stmt.setBigDecimal(3, feeRule.getValue());
            stmt.setString(4, feeRule.getCurrency().name());
            stmt.setBoolean(5, feeRule.isActive());
            stmt.setObject(6, feeRule.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating FeeRule: " + e.getMessage());
            e.printStackTrace();
        }
    }

   public void deactivate(UUID id) {
        String query = "UPDATE fee_rules SET is_active = false WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deactivating FeeRule: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private FeeRule mapResultSet(ResultSet rs) throws SQLException {
        FeeRule feeRule = new FeeRule();
        feeRule.setId((UUID) rs.getObject("id"));
        feeRule.setOperationType(TransactionType.valueOf(rs.getString("operation_type").toUpperCase()));
        feeRule.setMode(FeeMode.valueOf(rs.getString("mode")));
        feeRule.setValue(rs.getBigDecimal("value"));
        feeRule.setCurrency(Currency.valueOf(rs.getString("currency")));
        feeRule.setActive(rs.getBoolean("is_active"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) feeRule.setCreatedAt(ts.toLocalDateTime());
        return feeRule;
    }

}
