/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.connection;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class Connect {
    Connection conn;
    public Connection GetConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/app-sence","root","");
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Connection Fail : " + e);
        }
        return conn;
    }
}
