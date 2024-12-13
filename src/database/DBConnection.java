/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection connect() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;database=quanlycuahang";
            String username = "sa";
            String password = "123";
            Connection conn = DriverManager.getConnection(url,username,password);
//            System.out.println("connect thanh cong");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(String[] args) {
        
        connect();
    }
}
