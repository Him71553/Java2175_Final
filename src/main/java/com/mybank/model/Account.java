package com.mybank.model;

import com.mybank.service.Database;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Account {
    public final String id;
    public final String userId;
    public final String name;
    public final String bankId;
    public final String hashedPwd;
    private int balance;

    public static List<Account> getAccounts() throws SQLException {
        final List<Account> accounts = new ArrayList<>();
        final Connection conn = Database.getConnection();
        final Statement stmt = conn.createStatement();
        final ResultSet rs = stmt.executeQuery("SELECT * FROM accounts;");
        while (rs.next()) {
            final String id = rs.getString("id");
            final String userId = rs.getString("userId");
            final String name = rs.getString("name");
            final String bankId = rs.getString("bankId");
            final String hashedPwd = rs.getString("hashedPwd");
            final int balance = rs.getInt("balance");
            final Account acc = new Account(id, userId, name, bankId, hashedPwd);
            acc.setBalance(balance);
            accounts.add(acc);
        }
        rs.close();
        stmt.close();
        conn.close();
        return accounts;
    }

    public static Account createAccount(String userId, String name, String pwd, String bankId) {
        try {
            final String hashedPwd = hashPassword(pwd);
            final Connection conn = Database.getConnection();
            String id;
            while (true) {
                try {
                    id = bankId + generateRandomId();
            final PreparedStatement stmt = conn.prepareStatement("""
                            INSERT INTO accounts (id, userId, name, bankId, hashedPwd)
                            VALUES (?, ?, ?, ?, ?);
                    """);
                    stmt.setString(1, id);
                    stmt.setString(2, userId);
                    stmt.setString(3, name);
                    stmt.setString(4, bankId);
                    stmt.setString(5, hashedPwd);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
                    break;
                } catch (SQLException ignored) {
                }
            }

            return new Account(id, userId, name, bankId, hashedPwd);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }

    }

    private static String generateRandomId() {
        final SecureRandom random = new SecureRandom();
        final StringBuilder sb = new StringBuilder(14);
        for (int i = 0; i < 14; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private static String hashPassword(String pwd) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final SecureRandom random = new SecureRandom();
        final byte[] salt = new byte[16];
        random.nextBytes(salt);

        final KeySpec spec = new PBEKeySpec(pwd.toCharArray(), salt, 65536, 256);
        final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        final byte[] hash = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(String pwd, String hashedPwd) {
        try {
            final String[] parts = hashedPwd.split(":");
            final byte[] salt = Base64.getDecoder().decode(parts[0]);
            final byte[] hash = Base64.getDecoder().decode(parts[1]);
            final PBEKeySpec spec = new PBEKeySpec(pwd.toCharArray(), salt, 65536, 256);
            final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            final byte[] computedHash = factory.generateSecret(spec).getEncoded();
            return MessageDigest.isEqual(hash, computedHash);
        } catch (Exception e) {
            return false;
        }
    }

    private Account(String id, String userId, String name, String bankId, String hashedPwd) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.bankId = bankId;
        this.hashedPwd = hashedPwd;
    }

    public boolean setBalance(int newBalance) {
        try {
            final Connection conn = Database.getConnection();
            final PreparedStatement stmt = conn.prepareStatement("""
                    UPDATE accounts
                    SET balance = ?
                    WHERE id = ? AND bankId = ?;
                    """);
            stmt.setInt(1, newBalance);
            stmt.setString(2, id);
            stmt.setString(3, bankId);
            stmt.executeUpdate();
            this.balance = newBalance;
            stmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            return false;
        }
    }

    public int getBalance() {
        return balance;
    }

    /*public boolean withdraw(int amount) {
        if (balance < amount) {
            return false;
        }

        return setBalance(balance - amount);
    }*/
}
