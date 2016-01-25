package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AUXPLANTA
 */
public class ConexionBD {
    
    private static Connection conexion;
    private static Statement statement;
    private static String url = "localhost:5432";
    private static String BD = "CDM2";
    private static String user = "postgres";
    private static String pass = "cdm";

    public static Connection conectar(){
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection("jdbc:postgresql://"+url+"/"+BD, user, pass);
            statement = conexion.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);            
        } catch (Exception ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conexion;
    }
    
    public static ResultSet CONSULTAR(String sql){
        try {
            conectar();
            System.out.println("CONSULTA EXITOSA: "+sql);
            return statement.executeQuery(sql);            
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return null;
    }
    
    public boolean GUARDAR(String sql){
        try {
            conectar();            
            if(statement.executeUpdate(sql)>0){
                System.out.println("SE EJECUTÓ EL QUERY -> "+sql);
                return true;
            }else{
                System.err.println("NO SE EJECUTÓ EL QUERY -> "+sql);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            CERRAR();
        }
        return false;
    }
    
    public void CERRAR(){
        try {
            System.out.println("CONEXION CERRADA.");
            conexion.close();
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
        

    
}
