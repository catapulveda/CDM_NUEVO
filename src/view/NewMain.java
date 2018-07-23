/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AUXPLANTA
 */
public class NewMain {

    /**
     * @param args the command line arguments
     * @throws java.net.UnknownHostException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws UnknownHostException, ClassNotFoundException, SQLException {
        
        NewMain newMain = new NewMain();
    }

    public NewMain() throws SQLException, ClassNotFoundException {
        
        Class.forName("org.postgresql.Driver");
        PreparedStatement pst;
        try (Connection conexion = DriverManager.getConnection("jdbc:postgresql://gymsoft.zapto.org:5432/REGISTROS", "postgres", "hacker2780")) {            
            System.out.println(conexion);
        }
        
    }
    
}
