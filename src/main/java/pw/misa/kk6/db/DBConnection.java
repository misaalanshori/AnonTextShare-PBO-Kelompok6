/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pw.misa.kk6.db;

/**
 *
 * @author Isabu
 */
import java.sql.CallableStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
    private static Connection connection;

    private DBConnection() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection() {
        if (connection == null) {
            if (connection == null) {
                try {
                    Class.forName("oracle.jdbc.OracleDriver");
                    String jdbcUrl = "jdbc:oracle:thin:@db.kk6.misa.pw:1251:xe";
                    String username = "public_user";
                    String password = "helloanonymous";
                    connection = DriverManager.getConnection(jdbcUrl, username, password);
                    connection.setAutoCommit(false);
                } catch (ClassNotFoundException | SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return connection;
    }
}

