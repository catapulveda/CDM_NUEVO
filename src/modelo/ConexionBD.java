package modelo;

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
//    private static String url = "localhost:5432", BD = "CDM2", user = "postgres", pass = "cdm";
    private static String url = "PRODUCCION:5432", BD = "CDM2", user = "postgres", pass = "cdm";

    public ConexionBD() {
        
    }
    
    public Connection conectar(){
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection("jdbc:postgresql://"+url+"/"+BD, user, pass);
            statement = conexion.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);            
        } catch (Exception ex){
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
            Metodos.ERROR(ex, "ERROR AL CONECTARSE A LA BASE DE DATOS");
            System.exit(0);
        }
        return conexion;
    }
    
    public ResultSet CONSULTAR(String sql){
        ResultSet rs = null;
        try {     
            rs = statement.executeQuery(sql);
            System.out.println("CONSULTA EXITOSA: "+sql);
        }catch (SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("QUERY ERRONE0: "+sql+"\n");
            Metodos.ERROR(ex, "ERROR AL REALIZAR LA CONSULTA A LA BASE DE DATOS");            
        }
        return rs;
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
        }catch (SQLException ex){
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("NO SE EJECUTÓ EL QUERY -> "+sql);
            Metodos.ERROR(ex, "NO SE PUDO EJECUTAR EL QUERY CORRECTAMENTE");            
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
