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
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;
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
        
       DayOfWeek lunes = DayOfWeek.of(7);
       Locale l = Locale.getDefault();
       System.out.println("TextStyle.FULL:" + lunes.getDisplayName(TextStyle.FULL, l)); 
       System.out.println("TextStyle.NARROW:" + lunes.getDisplayName(TextStyle.NARROW, l)); 
       System.out.println("TextStyle.SHORT:" + lunes.getDisplayName(TextStyle.SHORT, l)); 
       
       String[] items = {"REP","MAN","GAR"};
        for (String item : items) {
            if(item.equals("REP")||item.equals("MAN")){
                continue;
            }
            System.out.println(item);
        }
    }

    public NewMain() {
        
        
    }
    
}
