package model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author AUXPLANTA
 */
public class Lote {
    
    private int idEntrada;
    private Cliente cliente;
    private Ciudad ciudad;
    private Conductor conductor;
    private String idEntradaAlmacen;
    private String lote;
    private String contrato;
    private String op;
    private String centroDeCostos;
    private Date fechaRecepcion;
    private Date fechaRegistro;
    private Date fechaActualizado;
    private Date fechaLiberado;
    private boolean estado;
    private String observacion;

    public Lote(int idEntrada, Cliente cliente, Ciudad ciudad, Conductor conductor, String idEntradaAlmacen, String lote, String contrato, String op, String centroDeCostos, Date fechaRecepcion, Date fechaRegistro, Date fechaActualizado, Date fechaLiberado, boolean estado, String observacion) {
        this.idEntrada = idEntrada;
        this.cliente = cliente;
        this.ciudad = ciudad;
        this.conductor = conductor;
        this.idEntradaAlmacen = idEntradaAlmacen;
        this.lote = lote;
        this.contrato = contrato;
        this.op = op;
        this.centroDeCostos = centroDeCostos;
        this.fechaRecepcion = fechaRecepcion;
        this.fechaRegistro = fechaRegistro;
        this.fechaActualizado = fechaActualizado;
        this.fechaLiberado = fechaLiberado;
        this.estado = estado;
        this.observacion = observacion;
    }        

    public int getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(int idEntrada) {
        this.idEntrada = idEntrada;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public void setConductor(Conductor conductor) {
        this.conductor = conductor;
    }

    public String getIdEntradaAlmacen() {
        return idEntradaAlmacen;
    }

    public void setIdEntradaAlmacen(String idEntradaAlmacen) {
        this.idEntradaAlmacen = idEntradaAlmacen;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getCentroDeCostos() {
        return centroDeCostos;
    }

    public void setCentroDeCostos(String centroDeCostos) {
        this.centroDeCostos = centroDeCostos;
    }

    public Date getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(Date fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaActualizado() {
        return fechaActualizado;
    }

    public void setFechaActualizado(Date fechaActualizado) {
        this.fechaActualizado = fechaActualizado;
    }

    public Date getFechaLiberado() {
        return fechaLiberado;
    }

    public void setFechaLiberado(Date fechaLiberado) {
        this.fechaLiberado = fechaLiberado;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
    
    public static void guardarLote(){
        
    }
    
    public static String[] getColumnNames(){
        return new String[]{"ID",
            "CLIENTE",
            "CIUDAD",
            "CONDUCTOR",
            "N° ENTRADA",
            "LOTE",
            "CONTRATO",
            "OP",
            "CENTRO DE COSTOS",
            "FECHA RECEPCION",
            "FECHA REGISTRO",
            "FECHA REGISTRO",
            "FECHA LIBERADO",
            "ESTADO"};
    }
    
    public static Class[] getColumnClass(){
        return new Class[]{Integer.class,
            Cliente.class,
            Ciudad.class,
            Conductor.class,
            String.class,
            String.class,
            String.class,
            String.class,
            String.class,
            Date.class,
            Date.class,
            Date.class,
            Date.class,
            Boolean.class};
    }
    
    public static void cargarLotes(DefaultTableModel modelo, String nombreCliente){
        try {
            ResultSet rs = model.ConexionBD.CONSULTAR("SELECT \n" +
                    "	e.identrada, e.idcliente, e.idciudad, e.idconductor, e.identradaAlmacen, e.lote, \n" +
                    "	e.contrato, e.op, e.centrodecostos, e.fecharecepcion, e.fecharegistrado, e.fechaactualizado, e.fechaliberado, e.estado, e.observacion,\n" +
                    "	ciu.nombreciudad, ciu.direccionciudad, ciu.telefonociudad,\n" +
                    "	cli.nombrecliente, cli.nitcliente,\n" +
                    "	con.cedulaconductor, con.nombreconductor\n" +
                    "FROM entrada e\n" +
                    "INNER JOIN ciudad ciu\n" +
                    "ON (e.idciudad = ciu.idciudad)\n" +
                    "INNER JOIN cliente cli\n" +
                    "ON (e.idcliente = cli.idcliente)\n" +
                    "INNER JOIN conductor con\n" +
                    "ON (e.idconductor = con.idconductor) WHERE nombrecliente ILIKE '%"+nombreCliente+"%' \n" +
                    "ORDER BY nombrecliente ASC, fecharecepcion ASC");
            while(rs.next()){
                modelo.addRow(
                        new Object[]{rs.getInt("identrada"), 
                                new Cliente(rs.getInt("idcliente"), rs.getString("nombrecliente"), rs.getString("nitcliente")), 
                                new Ciudad(rs.getInt("idciudad"), rs.getString("nombreciudad"), rs.getString("direccionciudad"), rs.getString("telefonociudad")), 
                                new Conductor(rs.getInt("idconductor"), rs.getString("cedulaconductor"), rs.getString("nombreconductor")), 
                                rs.getString("identradaAlmacen"),
                                rs.getString("lote"), 
                                rs.getString("contrato"),
                                rs.getString("op"), 
                                rs.getString("centrodecostos"),
                                rs.getDate("fecharecepcion"), 
                                rs.getDate("fecharegistrado"),
                                rs.getDate("fechaactualizado"),
                                rs.getDate("fechaliberado"),
                                rs.getBoolean("estado"), 
                                rs.getString("observacion")}
                            );                        
            }
        } catch (SQLException ex) {
            Logger.getLogger(Lote.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
