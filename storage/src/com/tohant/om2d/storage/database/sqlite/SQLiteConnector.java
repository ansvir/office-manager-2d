package com.tohant.om2d.storage.database.sqlite;

import com.badlogic.gdx.Gdx;

import java.sql.*;

import static com.tohant.om2d.storage.constant.TableConstant.DB_LOCAL_PATH;

public class SQLiteConnector {

    private final String DB_NAME = "om2d.db";
    private final String DB_URI = "jdbc:sqlite:/" + DB_LOCAL_PATH + DB_NAME;

    private static SQLiteConnector instance;
    private final Connection connection;

    private SQLiteConnector() throws ClassNotFoundException, SQLException {
        super();
        Class.forName("org.sqlite.JDBC");
        if (Gdx.files.isLocalStorageAvailable() && !Gdx.files.local(DB_NAME).exists()) {
            System.out.println("[LOG] INITIALIZING NEW GAME DATABASE");
        } else if (Gdx.files.isLocalStorageAvailable() && Gdx.files.local(DB_NAME).exists()) {
            System.out.println("[LOG] CONNECTING TO EXISTING GAME DATABASE");
        } else if (!Gdx.files.isLocalStorageAvailable()) {
            System.err.println("[WARN] ATTEMPTING TO CONNECT TO UNAVAILABLE DATABASE");
        }
        this.connection = DriverManager.getConnection(DB_URI);
    }

    public static SQLiteConnector getInstance() {
        if (instance == null) {
            try {
                instance = new SQLiteConnector();
            } catch (ClassNotFoundException e) {
                System.err.println("CONNECTION TO DATABASE CANNOT BE ESTABLISHED. CAUSE: CLASS_NOT_FOUND_EXCEPTION");
            } catch (SQLException e) {
                System.err.println("CONNECTION TO DATABASE CANNOT BE ESTABLISHED. CAUSE: " + e.getMessage());
            }
        }
        return instance;
    }

    public Statement getNewStatement() {
        try {
            return this.connection.createStatement();
        } catch (SQLException e) {
            System.err.println("CANNOT CREATE NEW STATEMENT. CAUSE: " + e.getMessage());
            return null;
        }
    }

}
