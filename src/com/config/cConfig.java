package com.config;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;


public class cConfig {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/perpus";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection connect;

    public static void connection() {
        try{
            //untuk regristrasi driver yang akan dipakai
            Class.forName(JDBC_DRIVER);
            //buat kon ke database
            connect = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("koneksi berhasil");
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("koneksi gagal");
        }
    }
}

