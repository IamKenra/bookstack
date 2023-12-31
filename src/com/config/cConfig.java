package com.config;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class cConfig {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/dbperpus";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection connect;

    public static void connection() {
        try {
            // Registrasi driver yang akan digunakan
            Class.forName(JDBC_DRIVER);
            // Buat koneksi ke database
            connect = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Koneksi berhasil");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Koneksi gagal");
        }
    }

    public static void disconnect() {
        try {
            if (connect != null) {
                connect.close();
                System.out.println("Koneksi terputus");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
