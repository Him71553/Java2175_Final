package com.mybank.model;

import com.mybank.service.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Currency {
    public final int id;
    public final String name;
    public final double exchangeRate;

    public static List<Currency> getCurrencies() throws SQLException {
        final List<Currency> currencies = new ArrayList<>();
        final Connection conn = Database.getConnection();
        final Statement stmt = conn.createStatement();
        final ResultSet rs = stmt.executeQuery("SELECT * FROM currencies;");
        while (rs.next()) {
            final int id = rs.getInt("id");
            final String name = rs.getString("name");
            final double exchangeRate = rs.getDouble("exchangeRate");
            final Currency currency = new Currency(id, name, exchangeRate);
            currencies.add(currency);
        }
        rs.close();
        stmt.close();
        conn.close();
        return currencies;
    }

    private Currency(int id, String name, double exchangeRate) {
        this.id = id;
        this.name = name;
        this.exchangeRate = exchangeRate;
    }
}
