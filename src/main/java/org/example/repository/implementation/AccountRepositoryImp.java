package org.example.repository.implementation;

import org.example.entities.Account;
import org.example.entities.enums.AccountStatus;
import org.example.entities.enums.AccountType;
import org.example.entities.enums.Currency;
import org.example.repository.AccountRepository;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AccountRepositoryImp implements AccountRepository {

    DatabaseConnection db;
    Connection connection;

    public AccountRepositoryImp() {
        db = DatabaseConnection.getInstance();
        connection = db.getConnection();
    }


    public void save(Account account){
        String query = "INSERT INTO accounts (id, type, balance, devise, status, created_at, date_cloture, client_id) VALUES (?, ?::account_type, ?,  ?::currency, ?::account_status, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, account.getId());
            stmt.setString(2, account.getType().name());     // enum → string
            stmt.setBigDecimal(3, account.getBalance());
            stmt.setString(4, account.getDevise().name());   // enum → string
            stmt.setString(5, account.getStatus().name());   // enum → string
            stmt.setTimestamp(6, Timestamp.valueOf(account.getCreatedAt()));

            if (account.getDateCloture() != null) {
                stmt.setTimestamp(7, Timestamp.valueOf(account.getDateCloture()));
            } else {
                stmt.setNull(7, Types.TIMESTAMP);
            }

            stmt.setObject(8, account.getClientId());

            stmt.executeUpdate();
            System.out.println("Account saved in database!");
        } catch (SQLException e) {
            System.out.println("Error saving account: " + e.getMessage());
            e.printStackTrace();
        }

    }


    public List<Account> findByClientId(UUID clientId) {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts WHERE client_id = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setObject(1, clientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Account account = new Account();
                account.setId((UUID) rs.getObject("id"));
                account.setType(AccountType.valueOf(rs.getString("type")));
                account.setBalance(rs.getBigDecimal("balance"));
                account.setDevise(Currency.valueOf(rs.getString("devise")));
                account.setStatus(AccountStatus.valueOf(rs.getString("status")));
                account.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                Timestamp clotureTs = rs.getTimestamp("date_cloture");
                if (clotureTs != null) {
                    account.setDateCloture(clotureTs.toLocalDateTime());
                }

                account.setClientId((UUID) rs.getObject("client_id"));

                accounts.add(account);
            }
        } catch (SQLException e) {
            System.out.println(" Error fetching accounts by clientId: " + e.getMessage());
            e.printStackTrace();
        }

        return accounts;
    }

    public Optional<Account> findById(UUID accountId) {
        String query = "SELECT * FROM accounts WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, accountId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Account account = new Account(
                        UUID.fromString(rs.getString("id")),
                        AccountType.valueOf(rs.getString("type")),
                        rs.getBigDecimal("balance"),
                        Currency.valueOf(rs.getString("devise")),
                        AccountStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("date_cloture") != null
                                ? rs.getTimestamp("date_cloture").toLocalDateTime()
                                : null,
                        UUID.fromString(rs.getString("client_id"))
                );
                return Optional.of(account);
            }
        } catch (SQLException e) {
            System.out.println(" Error finding account: " + e.getMessage());
            e.printStackTrace();
        }

        return Optional.empty();
    }


    public void updateStatus(UUID accountId, AccountStatus newStatus) {
        String query = "UPDATE accounts SET status = ?, date_cloture = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newStatus.name());

            if (newStatus == AccountStatus.CLOSED) {
                stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            } else {
                stmt.setNull(2, Types.TIMESTAMP);
            }

            stmt.setObject(3, accountId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Account status updated to " + newStatus);
            } else {
                System.out.println(" Account not found with ID: " + accountId);
            }
        } catch (SQLException e) {
            System.out.println(" Error updating account status: " + e.getMessage());
            e.printStackTrace();
        }
    }




    public void update(Account account) {
        String sql = """
            UPDATE account
            SET type = ?::account_type, balance = ?, devise = ?::currency,
                status = ?::account_status, date_cloture = ?, client_id = ?
            WHERE id = ?
            """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, account.getType().name());
            stmt.setBigDecimal(2, account.getBalance());
            stmt.setString(3, account.getDevise().name());
            stmt.setString(4, account.getStatus().name());
            stmt.setTimestamp(5, account.getDateCloture() != null ? Timestamp.valueOf(account.getDateCloture()) : null);
            stmt.setObject(6, account.getClientId());
            stmt.setObject(7, account.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating account", e);
        }
    }
























}







