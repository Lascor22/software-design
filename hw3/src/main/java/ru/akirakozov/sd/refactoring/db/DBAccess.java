package ru.akirakozov.sd.refactoring.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBAccess implements AutoCloseable {

    private volatile static DBAccess instance;

    public static DBAccess getInstanceOf() {
        if (instance == null) {
            try {
                instance = new DBAccess();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    public static DBAccess asInstanceOf(DBAccess db) {
        if (instance != null) {
            try {
                instance.close();
            } catch (Exception ignored) {
            }
        }

        if (instance == null) {
            instance = db;
        }

        return instance;
    }

    private final Connection DB;

    DBAccess() throws SQLException {
        String dbUrl = System.getProperty("shop.ru.itmo.sd.db.url");
        if (dbUrl == null || dbUrl.length() == 0) {
            String text = "Property `shop.ru.itmo.sd.db.url` has invalid value (EMPTY)";
            throw new IllegalStateException(text);
        }

        String url = String.join(":", "jdbc", "sqlite", dbUrl);
        this.DB = DriverManager.getConnection(url);
    }

    public ResultSet execute(String query) throws SQLException {
        return DB.createStatement().executeQuery(query);
    }

    public void update(String query) throws SQLException {
        DB.createStatement().executeUpdate(query);
    }

    @Override
    public void close() throws Exception {
        if (this.DB != null) {
            this.DB.close();
            instance = null;
        }
    }
}
