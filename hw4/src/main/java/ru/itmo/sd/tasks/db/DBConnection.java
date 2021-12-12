package ru.itmo.sd.tasks.db;

import java.sql.*;

public class DBConnection implements AutoCloseable {
    private final Connection DB;

    private DBConnection() throws SQLException {
        this.DB = connect();
    }

    private volatile static DBConnection instance;

    public static DBConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    private Connection connect() throws SQLException {
        String dbUrl = System.getProperty("tasks.db.url");
        if (dbUrl == null || dbUrl.length() == 0) {
            String text = "Property `tasks.db.url` has invalid value (EMPTY)";
            throw new IllegalStateException(text);
        }

        String login = System.getProperty("tasks.db.login"),
                password = System.getProperty("tasks.db.password");

        String url = String.join(":", "jdbc", "postgresql", dbUrl);
        if (login != null && password != null) {
            return DriverManager.getConnection(url, login, password);
        }
        return DriverManager.getConnection(url);
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return DB.prepareStatement(query);
    }

    @Override
    public void close() throws Exception {
        if (this.DB != null) {
            this.DB.close();
            instance = null;
        }
    }

}
