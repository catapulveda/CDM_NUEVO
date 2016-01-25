package model;

public class Cliente {

    private int idCliente;
    private String nombreCliente;
    private String nitCliente;
    
    public Cliente(int idCliente, String nombreCliente, String nitCliente) {
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.nitCliente = nitCliente;
    }
    
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNitCliente() {
        return nitCliente;
    }

    public void setNitCliente(String nitCliente) {
        this.nitCliente = nitCliente;
    }
    
    @Override
    public String toString(){
        return nombreCliente;
    }
    
    public static void guardarCliente(){
        
    }       
    
}
