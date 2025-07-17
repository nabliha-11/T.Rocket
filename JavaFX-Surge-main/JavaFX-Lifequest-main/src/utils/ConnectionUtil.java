/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 *
 * @author USER
 */
public class ConnectionUtil {
    Connection conn = null;
    public static Connection conDB(){
        try {
            
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/abc","root","256999pokemon");
            return con;
        } catch (Exception ex) {
             return null;
        }
    }
}
