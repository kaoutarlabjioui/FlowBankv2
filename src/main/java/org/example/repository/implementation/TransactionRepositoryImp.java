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

    private final DatabaseConnection db;
    private final Connection connection;

    public TransactionRepositoryImp() {
        db = DatabaseConnection.getInstance();
        connection = db.getConnection();
    }

    @Override
    public Transaction saveWithAccountUpdate(Transaction transaction) {
        if (transaction.getId() == null) transaction.setId(UUID.randomUUID());

        boolean applyBalancesNow = transaction.getType() != TransactionType.TRANSFER ||
                transaction.getCompteDestination() == null ||
                transaction.getCompteDestination().equals(transaction.getCompteSource());

        try {
            connection.setAutoCommit(false);

            // 1️⃣ Mise à jour des balances si nécessaire
            if (applyBalancesNow) {
                applyTransactionBalances(transaction);
                transaction.setStatus(TransactionStatus.SETTLED);
            } else {
                transaction.setStatus(TransactionStatus.PENDING);
            }
            save(transaction);



            connection.commit();
            return transaction;

        } catch (SQLException e) {
            try {
                connection.rollback();
                System.out.println("Rollback effectué (saveWithAccountUpdate)");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return null;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void applyTransactionBalances(Transaction transaction) {
        String updateBalanceQuery = "UPDATE accounts SET balance = balance + ? WHERE id = ?";

        try {
            if (transaction.getType() == TransactionType.DEPOSIT) {
                try (PreparedStatement stmt = connection.prepareStatement(updateBalanceQuery)) {
                    stmt.setBigDecimal(1, transaction.getMontant());
                    stmt.setObject(2, transaction.getCompteDestination());
                    stmt.executeUpdate();
                }
            } else if (transaction.getType() == TransactionType.WITHDRAW) {
                try (PreparedStatement stmt = connection.prepareStatement(updateBalanceQuery)) {
                    stmt.setBigDecimal(1, transaction.getMontant().negate().subtract(transaction.getFeeAmount()));
                    stmt.setObject(2, transaction.getCompteSource());
                    stmt.executeUpdate();
                }
            } else if (transaction.getType() == TransactionType.TRANSFER) {
                // Source
                try (PreparedStatement stmt = connection.prepareStatement(updateBalanceQuery)) {
                    stmt.setBigDecimal(1, transaction.getMontant().negate().subtract(transaction.getFeeAmount()));
                    stmt.setObject(2, transaction.getCompteSource());
                    stmt.executeUpdate();
                }
                // Destination
                if (transaction.getCompteDestination() != null) {
                    try (PreparedStatement stmt = connection.prepareStatement(updateBalanceQuery)) {
                        stmt.setBigDecimal(1, transaction.getMontant());
                        stmt.setObject(2, transaction.getCompteDestination());
                        stmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Transaction save(Transaction transaction) {

        if (transaction.getId() == null) transaction.setId(UUID.randomUUID());
        String query = """
            INSERT INTO transactions (
                id, type, montant, devise, fee_amount, date_execution,
                compte_source, compte_destination, description, status, fee_rule_id
            ) VALUES (?, ?::transaction_type, ?, ?::currency, ?, ?, ?, ?, ?, ?::transaction_status, ?)
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, transaction.getId());
            stmt.setString(2, transaction.getType().name().toLowerCase());
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
            try {
                connection.rollback();
                System.out.println("⚠️ Rollback effectué (save)");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        String query = "SELECT * FROM transactions WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Transaction> findByAccountId(UUID accountId) {
        List<Transaction> list = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE compte_source = ? OR compte_destination = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, accountId);
            stmt.setObject(2, accountId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> list = new ArrayList<>();
        String query = "SELECT * FROM transactions";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void updateStatus(UUID transactionId, TransactionStatus status) {
        String sql = "UPDATE transactions SET status = ?::transaction_status WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setObject(2, transactionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<Transaction> findByStatus(TransactionStatus status) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE status = ?::transaction_status"; // ✅ bien écrire transactions
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transactions.add(mapResultSet(rs)); // tu as déjà ta méthode mapResultSet
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }


    private Transaction mapResultSet(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId((UUID) rs.getObject("id"));
        transaction.setType(TransactionType.valueOf(rs.getString("type").toUpperCase()));
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
