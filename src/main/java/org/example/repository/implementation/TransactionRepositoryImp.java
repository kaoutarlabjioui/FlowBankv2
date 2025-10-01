package org.example.repository.implementation;

import org.example.entities.Transaction;
import org.example.entities.enums.Currency;
import org.example.entities.enums.TransactionStatus;
import org.example.entities.enums.TransactionType;
import org.example.repository.TransactionRepository;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionRepositoryImp implements TransactionRepository {
    DatabaseConnection db;
    Connection connection;

    public TransactionRepositoryImp(){
        db = DatabaseConnection.getInstance();
        connection = db.getConnection();
    }

    public Transaction save(Transaction transaction) {

        if (transaction.getId() == null) {
            transaction.setId(UUID.randomUUID());
        }
        String query = "INSERT INTO transactions (id, type, montant, devise, fee_amount, date_execution, compte_source, compte_destination, description, status, fee_rule_id) VALUES (?, ?::transaction_type, ?, ?::currency, ?, ?, ?, ?, ?, ?::transaction_status, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, transaction.getId());
            stmt.setString(2, transaction.getType().name());
            stmt.setBigDecimal(3, transaction.getMontant());
            stmt.setString(4, transaction.getDevise().name());
            stmt.setBigDecimal(5, transaction.getFeeAmount());
            stmt.setTimestamp(6, Timestamp.valueOf(transaction.getDateExecution() != null ? transaction.getDateExecution() : LocalDateTime.now()));
            stmt.setObject(7, transaction.getCompteSource());
            stmt.setObject(8, transaction.getCompteDestination());
            stmt.setString(9, transaction.getDescription());
            stmt.setString(10, transaction.getStatus().name());
            stmt.setObject(11, transaction.getFeeRuleId());
            stmt.executeUpdate();
            return transaction;
        } catch (SQLException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Optional<Transaction> findById(UUID id) {
        String query = "SELECT * FROM transactions WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error finding transaction: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Transaction> findByAccountId(UUID accountId) {
        List<Transaction> list = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE compte_source = ? OR compte_destination = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, accountId);
            stmt.setObject(2, accountId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching transactions: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }


    public List<Transaction> findAll() {
        List<Transaction> list = new ArrayList<>();
        String query = "SELECT * FROM transactions";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all transactions: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }


    public void updateStatus(UUID transactionId, TransactionStatus status) {
        String sql = "UPDATE transactions SET status = ?::transaction_status WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setObject(2, transactionId);
            stmt.executeUpdate();
            System.out.println(" Transaction status updated: " + transactionId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Transaction mapResultSet(ResultSet rs) throws SQLException {

        Transaction transaction = new Transaction();
        transaction.setId((UUID) rs.getObject("id"));
        transaction.setType(TransactionType.valueOf(rs.getString("type")));
        transaction.setMontant(rs.getBigDecimal("montant"));
        transaction.setDevise(Currency.valueOf(rs.getString("devise")));
        transaction.setFeeAmount(rs.getBigDecimal("fee_amount"));
        transaction.setDateExecution(rs.getTimestamp("date_execution").toLocalDateTime());
        transaction.setCompteSource((UUID) rs.getObject("compte_source"));
        transaction.setCompteDestination((UUID) rs.getObject("compte_destination"));
        transaction.setDescription(rs.getString("description"));
        transaction.setStatus(TransactionStatus.valueOf(rs.getString("status")));
        transaction.setFeeRuleId((UUID) rs.getObject("fee_rule_id"));
        return transaction;
    }

}
