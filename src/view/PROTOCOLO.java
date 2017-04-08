package view;

//import MUNDO.GeneraExcel;
//import MUNDO.ColorTablaFases;
import Dialogos.DialogoRegistrarCliente;
import Dialogos.DialogoRegistrarLiquidoAislante;
import Dialogos.DialogoRegistrarMarcaDeTransformadores;
import Dialogos.DialogoRegistrarReferenciaAceite;
import Dialogos.DialogoTrafosRepetidos;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import modelo.ConexionBD;
import modelo.FORMULAS;
import modelo.Metodos;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public final class PROTOCOLO extends javax.swing.JFrame {

    public Icon bien = new ImageIcon(getClass().getResource("/recursos/images/bien.png"));
    public Icon mal = new ImageIcon(getClass().getResource("/recursos/images/error.png"));    
    DialogoRegistrarMarcaDeTransformadores drmt;
    DialogoRegistrarLiquidoAislante drla;
    DialogoRegistrarReferenciaAceite drra;
    DialogoRegistrarCliente drc;
//    Locale spanish = new Locale("es", "ES");
//    NumberFormat nf = NumberFormat.getInstance(spanish);
    public String refriferacion = "ONAN";
    public String calentamiendodevanado = "65 ºC";
    public String alturadiseno = "1000 MSNM";
    private DefaultTableModel modeloTabla;
    int con = 0;
    public double valores[][] = new double[5][2];
    public double kc = 234.5;
    public String estado = null;
    private final String ruta = System.getProperties().getProperty("user.dir");
//    private final xBusqueda buscar = new xBusqueda();
    public String anoactual = String.valueOf(new GregorianCalendar().get(Calendar.YEAR));
    String mesactual = String.valueOf(new GregorianCalendar().get(Calendar.MONTH));
    public String ultimosdosanos = anoactual.substring(2, 4);
    public String ultimosdosanosaactualizar = null;
    Date myDate = new Date();
    public String fechaexpedicion = new SimpleDateFormat("dd-MMM-yyyy").format(myDate);
    public String fechaexpedicionparaactualizar;
    public String fechaexpedicionparaactualizarBD;
    public boolean HayQueActualizar = false;
    public String estadoaactualizar = null;
    public int id_protocoloencontrado = 0;
    ConexionBD conexion = new ConexionBD();
    Clip sonido;
//    public String IP = "PLANTA";

    public PROTOCOLO() {
        initComponents();

        combogrupodeconexion.addItem("Ii6");
        combogrupodeconexion.addItem("Ii0");
        combogrupodeconexion.addItem("DYn5");
        combogrupodeconexion.addItem("DYn11");
        combogrupodeconexion.addItem("Yy0");
        combogrupodeconexion.addItem("Yy6");
        /**
         * ************************************************************
         */
        combopolaridad.addItem("SUSTRACTIVA");
        combopolaridad.addItem("ADITIVA");
        combopolaridad.addItem("NO APLICA");
        /**
         * ************************************************************
         */
        comborefrigeracion.addItem("ONAN");
        comborefrigeracion.addItem("ONAF");
        comborefrigeracion.addItem("AN");
        /**
         * ************************************************************
         */
        comboderivacionprimaria.addItem("(-4)*2.5%");
        comboderivacionprimaria.addItem("(+1-3)*2.5%");
        comboderivacionprimaria.addItem("(+2-2)*2.5%");
        comboderivacionprimaria.addItem("(+3-1)*2.5%");
        comboderivacionprimaria.addItem("(+4)*2.5%");
        /**
         * ************************************************************
         */

        /**
         * ************************************************************
         */
        SoloNumeros(cjtensionprimaria);
        SoloNumeros(cjtensionsecundaria);
        SoloNumerosYComa(cjpotencia);
        SoloNumeros(cjresistenciafaseu);
        SoloNumeros(cjresistenciafasev);
        SoloNumeros(cjresistenciafasew);
        SoloNumeros(cjresistenciaxy);
        SoloNumeros(cjresistenciayz);
        SoloNumeros(cjresistenciazx);
        SoloNumeros(cjfrecuencia);
        SoloNumeros(cjtiempodeprueba);
        SoloNumeros(cjcorrientefaseIu);
        SoloNumeros(cjcorrientefaseIv);
        SoloNumeros(cjcorrientefaseIw);
        SoloNumeros(cjpromediocorrienteI);
        SoloNumeros(cjvcc);
        /**
         * ************************************************************
         */
//        String CargarMarcas = "SELECT * FROM marcatransformador";
//        ResultSet rs = Metodos.BuscarResult(CargarMarcas);
//        try{
//            while(rs.next()){
//                combomarcatransformador.addItem(rs.getString(""));
//            }
//        }catch(SQLException e){}
        /**
         * ************************************************************
         */
//        Cargarcombo(combomarcatransformador, "marcatransformador", "marca_marcatransformador");
//        Cargarcombo(comboservicio, "tipotransformador", "tipo_tipotransformador");
//        Cargarcombo(comboliquidoaislante, "liquidoaislantetransformador", "nombre_liquidoaislante");
        Cargarcombo(comboreferenciaaceite, "referenciadeaceite", "nombre_referenciaaceite");
        /**
         * ************************************************************
         */
//        ValidarSalida(cjpotencia, "Ingrese la potencia del transformafor");
        /**
         * ************************************************************
         */
        BordeGris(cjresistenciafaseu);
        BordeGris(cjresistenciafasev);
        BordeGris(cjresistenciafasew);
        BordeGris(cjresistenciaxy);
        BordeGris(cjresistenciayz);
        BordeGris(cjresistenciazx);
        BordeGris(cjcorrientefaseIu);
        BordeGris(cjcorrientefaseIv);
        BordeGris(cjcorrientefaseIw);
        BordeGris(cjpotencia);
        BordeGris(cjcorrienteprimaria);
        BordeGris(cjpotencia);
        BordeGris(cjvcc);
        BordeGris(cjpromedioresistenciaprimaria);
        BordeGris(cjpromedioresistenciasecundaria);
        /**
         * ***************************************************************************************
         */
        Enter(cjresistenciafaseu, cjresistenciafasev);
        Enter(cjresistenciafasev, cjresistenciafasew);
        Enter(cjresistenciaxy, cjresistenciayz);
        Enter(cjresistenciayz, cjresistenciazx);
        /**
         * ***************************************************************************************
         */
//        Borrar(cjnoempresa);Borrar(cjserieno);Borrar(cjpotencia);Borrar(cjfechafabricacion);Borrar(cjtensionprimaria);
//        Borrar(cjtensionsecundaria);Borrar(cjATcontraBT);Borrar(cjATcontraTierra);Borrar(cjBTcontraTierra);
//        Borrar(cjresistenciafaseu);Borrar(cjresistenciafasev);Borrar(cjresistenciafasew);
//        Borrar(cjresistenciaxy);Borrar(cjresistenciayz);Borrar(cjresistenciazx);
//        Borrar(cjcorrientefaseIu);Borrar(cjcorrientefaseIv);Borrar(cjcorrientefaseIw);
//        Borrar(cjpomedido);Borrar(cjvcc);Borrar(cjmasatotal);Borrar(cjvolliquido);Borrar(cjlargodimension);
//        Borrar(cjanchodimension);Borrar(cjaltodimension);Borrar(cjcolor);Borrar(cjespesor);Borrar(cjnumerodelementos);
//        Borrar(cjlargorefrigeracion);Borrar(cjaltorefrigeracion);Borrar(cjtemperaturadeprueba);Borrar(cjperdidasdecobre);

        comboderivacionprimaria.setSelectedIndex((int) cjposicionconmutador.getValue() - 1);
        
        combofasestransformador.setSelectedItem("1");
        cjcliente.grabFocus();
//        jList1.setModel(buscar.buscar(""));
        jList1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int posicion = jList1.locationToIndex(e.getPoint());
                    if (posicion >= 0) {
                        try {
                            //File path = new File("\\\\"+IP+"\\Mis documentos\\REPORTES PDF\\" + jList1.getModel().getElementAt(posicion));
                            File path = new File(RutaPDF() + jList1.getModel().getElementAt(posicion));
                            Desktop.getDesktop().open(path);
                        } catch (IOException | IllegalArgumentException ex) {
                            M("ERROR AL ABRIR EL ARCHIVO", mal);
                        }
                    }
                }
            }
        });
//        jList1.setCellRenderer(new RenderLista());
        Cargar("");
        AutoCompletar(cjcliente, "cliente", "nombre_cliente");
        AutoCompletar(cjbuscarporcliente, "cliente", "nombre_cliente");
//        AutoCompletar(cjbuscarclientepersonalizada, "cliente", "nombre_cliente");       
        AutoCompletar(cjbuscarpormarca, "marcatransformador", "marca_marcatransformador");
//        AutoCompletarLote();
        setExtendedState(MAXIMIZED_BOTH);

//        PanelBusquedaCompleta.setName("Busqueda Completa");
//        PanelBusquedaNormal.setName("Panel Busqueda Normal");    
        comboservicio.setSelectedIndex(1);
//        ajTextField.autocompleterText autocompleterText = new ajTextField.autocompleterText(cjserieno, "noplaca", "datosentrada");
        TextAutoCompleter tserie = new TextAutoCompleter(cjserieno);
        conexion.conectar();
        ResultSet rsSerie = conexion.CONSULTAR("SELECT noplaca, lote FROM datosentrada, entrada WHERE identrada=identrada_datos");
        try {
            while(rsSerie.next()){
                tserie.addItem(rsSerie.getString("noplaca"));
            }
        } catch (SQLException ex){
            Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            conexion.CERRAR();
        }
    }

    public void AutoCompletar(JTextField caja, String tabla, String columna) {
        TextAutoCompleter t = new TextAutoCompleter(caja);
        conexion.conectar();
        ResultSet rs = conexion.CONSULTAR("SELECT * FROM " + tabla);
        try {
            while (rs.next()) {
                t.addItem(rs.getString(columna));
            }
        } catch (SQLException e) {
        }finally{
            conexion.CERRAR();
        }
    }

    public void AutoCompletarLote(String cliente){
        //TextAutoCompleter t = new TextAutoCompleter(cjbuscarporlote);
        conexion.conectar();
        ResultSet rs = conexion.CONSULTAR("SELECT DISTINCT (lote) As Lotes FROM protocolo WHERE cliente='" + cliente + "' ORDER BY lote ASC");
        try {
            comboBuscarPorLote.removeAllItems();
            comboBuscarPorLote.addItem("TODOS");
            while (rs.next()) {
                comboBuscarPorLote.addItem(rs.getString("Lotes"));
            }
        } catch (SQLException e) {
        }finally{
            conexion.CERRAR();
        }
    }

    public int getIdProtocolo() {
//        int id = 0;
//        try {
//            conexion.conectar();
//            ResultSet rs = conexion.CONSULTAR("SELECT MAX(id_protocolo) AS maximo FROM protocolo");
            return modelo.Metodos.getConsecutivoRemision("idprotocolo", false);
//            boolean si = false;
//            if (rs.next()) {
//                cjnoprotocolo.setText("A-" + (rs.getInt("maximo") + 1) + "-" + ultimosdosanos);
//                si = true;
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
//            M("Error al buscar el Id del protocolo\n" + ex, mal);
//        }finally{
//            conexion.CERRAR();
//        }
//        return id;
    }

    String busca = "";

    public void Cargar(String t) {
        getIdProtocolo();
        String data[][] = {};
        String col[] = {"N° Protocolo", "CLIENTE", "TRANSFORMADOR", "SERIE No", "MARCA", "POTENCIA", "FASES", "TENSION PRIMARIA", "TENSION SECUNDARIA", "LOTE No", "FECHA"};
        modeloTabla = new DefaultTableModel(data, col) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        datos.setModel(modeloTabla);
//        String sql = "SELECT * FROM protocolo p INNER JOIN marcatransformador m ON p.idmarca=m.id_marcatransformador WHERE (p.cliente || ' ' || p.lote || ' ' || p.fechaexpedicion || p.id_protocolo || '-' || p.anocreacion || p.cliente || p.serieno || p.lote || ' ' || p.id_protocolo || ' ' || p.anocreacion || ' ' || fechaexpedicion || ' ' || m.marca_marcatransformador) ILIKE '%"+t+"%' ORDER BY fechaexpedicion DESC ";                
        try {
            busca = "SELECT id_protocolo,anocreacion,cliente,servicio,serieno,marca_marcatransformador,potencia,fase,";
            busca += "tensionprimaria,tensionsecundaria,lote,fechaexpedicion FROM protocolo p INNER JOIN marcatransformador m ON p.idmarca=m.id_marcatransformador  ";
            if (!cjbuscarplaca.getText().isEmpty()) {
                busca += " WHERE serieno ILIKE '%" + cjbuscarplaca.getText() + "%' ";
            } else {
                busca += (!cjbuscarporcliente.getText().isEmpty()) ? " WHERE cliente='" + cjbuscarporcliente.getText() + "'" : "";
                busca += (comboBuscarPorLote.getSelectedIndex() > 0) ? " AND lote='" + comboBuscarPorLote.getSelectedItem() + "'" : "";
                busca += (!cjbuscarpormarca.getText().isEmpty()) ? " AND idmarca='" + getIdMarca(cjbuscarpormarca.getText().trim()) + "' " : " ";
                if (cjFechaDesde.getDate() != null && cjFechaHasta.getDate() != null) {
                    busca += " AND fechaexpedicion BETWEEN '" + cjFechaDesde.getDate() + "' AND '" + cjFechaHasta.getDate() + "' ";
                }
                busca += "ORDER BY id_protocolo, fase DESC, potencia, idmarca";
            }

//            busca += (!cjbuscarplaca.getText().isEmpty()) ? "serieno ILIKE '%"+cjbuscarplaca.getText()+"%' "
//                : (!cjbuscarporcliente.getText().isEmpty() && cjbuscarporlote.getText().isEmpty()) ? "cliente='" + cjbuscarporcliente.getText().trim() + "' "
//                : (!cjbuscarporcliente.getText().isEmpty() && !cjbuscarporlote.getText().isEmpty() && !cjbuscarpormarca.getText().isEmpty()) ? "cliente='" + cjbuscarporcliente.getText().trim() + "' AND lote='" + cjbuscarporlote.getText().trim() + "' AND  idmarca='" + getIdMarca(cjbuscarpormarca.getText().trim()) + "' "
//                : (!cjbuscarporcliente.getText().isEmpty() && !cjbuscarporlote.getText().isEmpty()) ? "cliente='" + cjbuscarporcliente.getText().trim() + "' AND lote='" + cjbuscarporlote.getText().trim() + "' "
//                : "serieno ilike '%" + cjbuscarplaca.getText().trim() + "%' ORDER BY id_protocolo, fase DESC, potencia, idmarca ";
            conexion.conectar();
            ResultSet rs = conexion.CONSULTAR(busca);
            while (rs.next()) {
                modeloTabla.insertRow(con, new Object[]{});
                modeloTabla.setValueAt("A-" + rs.getInt("id_protocolo") + "-" + rs.getString("anocreacion"), con, 0);
                modeloTabla.setValueAt(rs.getString("cliente"), con, 1);
                modeloTabla.setValueAt(rs.getString("servicio"), con, 2);
                modeloTabla.setValueAt(rs.getString("serieno"), con, 3);
                modeloTabla.setValueAt(rs.getString("marca_marcatransformador"), con, 4);
                modeloTabla.setValueAt(rs.getString("potencia").replace(".", ","), con, 5);
                modeloTabla.setValueAt(rs.getString("fase"), con, 6);
                modeloTabla.setValueAt(rs.getString("tensionprimaria"), con, 7);
                modeloTabla.setValueAt(rs.getString("tensionsecundaria"), con, 8);
                modeloTabla.setValueAt(rs.getString("lote"), con, 9);
                modeloTabla.setValueAt(rs.getDate("fechaexpedicion"), con, 10);
                LISTAPROTOCOLOS.setTitleAt(0, "Protocolos - " + rs.getRow());
            }
            conexion.CERRAR();
        } catch (SQLException ex) {
            Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
            M("ERROR AL BUSCAR Y MOSTRAR LOS PROTOCOLOS REGISTRADOS EN LA BASE DE DATOS\n" + ex, mal);
        }
    }

    public void CargarTablaUno(int Vp, int pos, JComboBox fase) {
        if (!fase.getSelectedItem().toString().equalsIgnoreCase("Añadir otro") && !fase.getSelectedItem().toString().equalsIgnoreCase("Actualizar lista")) {
            int faseseleccionada = Integer.parseInt(fase.getSelectedItem().toString());
            boolean si = (faseseleccionada == 1);
            modeloTabla = modeloTablao(faseseleccionada);
            tablauno.setModel(modeloTabla);
            int c = 1;
            for (int i = 1; i <= 5; i++) {
                modeloTabla.insertRow(con, new Object[]{});
                if (faseseleccionada == 1) {
                    modeloTabla.setValueAt("0", con, 3);
                    modeloTabla.setValueAt("0", con, 4);
                }
                if (pos == 2) {
                    modeloTabla.setValueAt("Pos : " + c, con, 0);
                    if (i == 1) {
                        modeloTabla.setValueAt(Math.round(Vp * 1.025), con, 1);
                    }
                    if (i == 2) {
                        modeloTabla.setValueAt(Math.round(Vp * 1.00), con, 1);
                    }
                    if (i == 3) {
                        modeloTabla.setValueAt(Math.round(Vp * 0.975), con, 1);
                    }
                    if (i == 4) {
                        modeloTabla.setValueAt(Math.round(Vp * 0.95), con, 1);
                    }
                    if (i == 5) {
                        modeloTabla.setValueAt(Math.round(Vp * 0.925), con, 1);
                    }
                } else {
                    modeloTabla.setValueAt("Pos : " + c, con, 0);
                    if (i == 1) {
                        modeloTabla.setValueAt(Math.round(Vp * 1.05), con, 1);
                    }
                    if (i == 2) {
                        modeloTabla.setValueAt(Math.round(Vp * 1.025), con, 1);
                    }
                    if (i == 3) {
                        modeloTabla.setValueAt(Math.round(Vp * 1.0), con, 1);
                    }
                    if (i == 4) {
                        modeloTabla.setValueAt(Math.round(Vp * 0.975), con, 1);
                    }
                    if (i == 5) {
                        modeloTabla.setValueAt(Math.round(Vp * 0.95), con, 1);
                    }
                }
                c = c + 1;
                con++;
            }
            con = 0;
//            tablauno.setDefaultRenderer(Object.class, new ColorTablaFases(valores, Integer.parseInt(combofasestransformador.getSelectedItem().toString())));
        }
    }

    public DefaultTableModel modeloTablao(int fase){
        DefaultTableModel modeloTablaomio = null;
        String data[][] = {};
        String col[] = {"POSC", "TENSION", "FASE U", "FASE V", "FASE W"};
        if (fase == 1) {
            modeloTablaomio = new DefaultTableModel(data, col) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    if (column == 3) {
                        return false;
                    }
                    return column != 4;
                }
            };
        } else if (fase == 3) {
            modeloTablaomio = new DefaultTableModel(data, col) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return true;
                }
            };
        }
        return modeloTablaomio;
    }

    public void Cargarcombo(JComboBox combo, String tabla, String col) {
        combo.removeAllItems();
        try {
            conexion.conectar();
            ResultSet rs = conexion.CONSULTAR("SELECT * FROM " + tabla);
            while (rs.next()) {
                combo.addItem(rs.getString(col));
            }
            combo.addItem("Añadir otro");
        } catch (SQLException ex) {
            Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
            M("ERROR AL CARGAR LOS COMBOS", mal);
        }finally{
            conexion.CERRAR();
        }
    }

    public void CargarcomboServicio(){
        conexion.conectar();
        ResultSet rs = conexion.CONSULTAR("SELECT * FROM tipotransformador");
        comboservicio.removeAllItems();
        AutoCompleteDecorator.decorate(comboservicio);
        try {
            while (rs.next()) {
                comboservicio.addItem(rs.getString(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
            M("Error al cargar el listado de servicios", mal);
        }finally{
            conexion.CERRAR();
        }
    }

    public void ValidarSalida(final JTextField t, String m) {
        t.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (t.getText().isEmpty()) {
                    t.grabFocus();
                }
            }
        });
    }

    public void ValidarSalidaANumeros(final JTextField t, final String m) {
        t.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {

                if (t.getText().isEmpty()) {
                    M(m, mal);
                    t.grabFocus();
                }
            }
        });
    }

    public void CalcularCorrientePrimaria() {
        if (!cjpotencia.getText().isEmpty() && !cjtensionprimaria.getText().isEmpty()) {
            try {
//                cjcorrienteprimaria.setText("" + FORMULAS.HallarIpOIs(nf.parse(cjpotencia.getText()).doubleValue(), Integer.parseInt(cjtensionprimaria.getText()), combofasestransformador));
//                cjcorrienteprimariadespacho.setText("" + FORMULAS.HallarIpOIs(nf.parse(cjpotencia.getText()).doubleValue(), Integer.parseInt(cjtensionprimaria.getText()), combofasestransformador));
                cjcorrienteprimaria.setText("" + FORMULAS.HallarIpOIs(Double(cjpotencia.getText()), Integer.parseInt(cjtensionprimaria.getText()), combofasestransformador));
                cjcorrienteprimariadespacho.setText("" + FORMULAS.HallarIpOIs(Double(cjpotencia.getText()), Integer.parseInt(cjtensionprimaria.getText()), combofasestransformador));
            } catch (Exception ex) {
                Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
                M("Error al obtener el valor de la potencia, hay un error en la escritura\n" + ex, mal);
            }
        }
    }

    public void CalcularCorrienteSecundaria() {
        if (!cjpotencia.getText().isEmpty() && !cjtensionsecundaria.getText().isEmpty()) {
            try {
                cjcorrientesecundaria.setText("" + FORMULAS.HallarIpOIs(Double(cjpotencia.getText()), Integer.parseInt(cjtensionsecundaria.getText()), combofasestransformador));
                cjcorrientesecundariadespacho.setText("" + FORMULAS.HallarIpOIs(Double(cjpotencia.getText()), Integer.parseInt(cjtensionsecundaria.getText()), combofasestransformador));
            } catch (Exception ex) {
                Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
                M("Error al obtener el valor de la potencia\n" + ex, mal);
            }
        }
    }

    public void MostrarNominalTablaDos() {
        try {
            if (tablauno.getRowCount() == 5 && !cjtensionsecundaria.getText().isEmpty() && Integer.parseInt(cjtensionsecundaria.getText()) > 0) {
                String data[][] = {};
                String col[] = {"NOMINAL", "MINIMA", "MAXIMA"};
                modeloTabla = new DefaultTableModel(data, col);
                tablados.setModel(modeloTabla);
                for (int i = 0; i < tablauno.getRowCount(); i++) {
                    Long n = (long) tablauno.getValueAt(i, 1);
                    double r = n.doubleValue();
                    modeloTabla.insertRow(con, new Object[]{});
                    double nominal = FORMULAS.HallarRn(r, Double.parseDouble(cjtensionsecundaria.getText()), Integer.parseInt(combofasestransformador.getSelectedItem().toString()));
                    double minimo = FORMULAS.HallarRnMinMax(nominal, 0.995);
                    double maximo = FORMULAS.HallarRnMinMax(nominal, 1.005);
                    modeloTabla.setValueAt(nominal, con, 0);
                    modeloTabla.setValueAt(minimo, con, 1);
                    modeloTabla.setValueAt(maximo, con, 2);
                    con++;
                }
                con = 0;
                for (int i = 0; i < tablados.getRowCount(); i++) {
                    for (int j = 1; j < tablados.getColumnCount(); j++) {
                        valores[i][j - 1] = (double) tablados.getValueAt(i, j);
                    }
                }
            }
//            tablados.setDefaultRenderer(Object.class, new ColorTablaNominales());
        } catch (NumberFormatException e) {
            cjtensionsecundaria.setBorder(new LineBorder(Color.red));
//            cjtensionsecundaria.grabFocus();
            M("ERROR!\nNo es posible calcular el valor Nominal\nDetalles:\n1- La tension secundaria esta mal escrita o esta vacia.\n2-No hay registros de las tensiones por posicion del conmutador\n", mal);
        }
    }

    public double getK(double kc, JTextField cjtemperaturadeprueba) {
        double K = 0;
        try {
            if (!cjtemperaturadeprueba.getText().isEmpty() || Double.parseDouble(cjtemperaturadeprueba.getText()) >= 0) {
                if (comborefrigeracion.getSelectedIndex() < 2) {
                    K = (kc + 85) / (kc + Double.parseDouble(cjtemperaturadeprueba.getText()));
                } else if (comborefrigeracion.getSelectedIndex() == 2) {
                    switch (comboclasedeaislamiento.getSelectedIndex()) {
                        case 0:
                            K = (kc + 75) / (kc + Double.parseDouble(cjtemperaturadeprueba.getText()));
                            break;
                        case 1:
                            K = (kc + 85) / (kc + Double.parseDouble(cjtemperaturadeprueba.getText()));
                            break;
                        case 2:
                            K = (kc + 100) / (kc + Double.parseDouble(cjtemperaturadeprueba.getText()));
                            break;
                        case 3:
                            K = (kc + 120) / (kc + Double.parseDouble(cjtemperaturadeprueba.getText()));
                            break;
                        case 4:
                            K = (kc + 145) / (kc + Double.parseDouble(cjtemperaturadeprueba.getText()));
                            break;
                    }
                }
            }
        } catch (NumberFormatException e){
            M("La temperatura de prueba esta vacia, imposible calcular (K)\n" + e, mal);
            cjtemperaturadeprueba.grabFocus();
            cjtemperaturadeprueba.setBorder(new LineBorder(Color.red));
        }
        return K;
    }

    public void HallarI2R85() {
        double k = getK(kc, cjtemperaturadeprueba);
        double i2r85 = FORMULAS.QuitarDecimales(getI2R85(k, cji2r), 1);
        cjI2Ra85.setText(String.valueOf(i2r85));
//        cjz85.grabFocus();
    }

    public double getI2R85(double k, JTextField cji2r) {
        double i2r85 = 0;
        try {
            if (!cji2r.getText().isEmpty() && Double.parseDouble(cji2r.getText()) >= 0) {
                double i2r = Double.parseDouble(cji2r.getText());
                i2r85 = i2r * k;
            }
        } catch (NumberFormatException e) {
            M("Error al hallar I2R referidas a 85ºC\nEl valor en I2r esta vacio o mal escrito\n" + e, mal);
            cji2r.grabFocus();
            cji2r.setBorder(new LineBorder(Color.red));
        }
        return i2r85;
    }

    public double getR(JTextField cjperdidasdecobre, JTextField cjpotencia) {
        double r = 0;
        try {
            if (Double(cjpotencia.getText()) >= 0) {
                try {
                    if (!cjperdidasdecobre.getText().isEmpty() && Double.parseDouble(cjperdidasdecobre.getText()) >= 0) {
                        double pcu = Double.parseDouble(cjperdidasdecobre.getText());
                        double kva = Double(cjpotencia.getText());
                        r = pcu / (10 * kva);
                    }
                } catch (NumberFormatException e) {
                    M("ERROR!\nNo se puede calcular %R\nEl valor de las perdidas en el cobre (Pcu) esta vacio o mal escrito\n" + e, mal);
                    cjperdidasdecobre.grabFocus();
                    cjperdidasdecobre.setBorder(new LineBorder(Color.red));
                }
            }
        } catch (Exception e) {
            cjpotencia.setBorder(new LineBorder(Color.red, 2));
        }
        return r;
    }

    public double getZ(JTextField cjvcc, JTextField cjtensionprimaria) {
        double z = 0;
        try {
            if (Double.parseDouble(cjvcc.getText()) >= 0 && !cjvcc.getText().isEmpty() && Double.parseDouble(cjvcc.getText()) >= 0) {
                try {
                    if (!cjtensionprimaria.getText().isEmpty() && Double.parseDouble(cjtensionprimaria.getText()) >= 0) {
                        double vcc = Double.parseDouble(cjvcc.getText());
                        double vp = Double.parseDouble(cjtensionprimaria.getText());
                        z = (vcc / vp) * 100;
                    }
                } catch (NumberFormatException e) {
                    M("ERROR!\nEl valor de la tension primaria es vacio o mal esccrito\n" + e, mal);
                }
            }
        } catch (NumberFormatException e) {
            cjvcc.setBorder(new LineBorder(Color.red, 2));/*M("ERROR EN EL VALOR DEL VOLTAJE DE CORTO CIRCUITO\nMAL ESCRITO O VACIO",mal);*/

        }
        return z;
    }

    public double getX(double Z, double R) {
        double x = 0;
        x = Math.sqrt((Math.pow(Z, 2)) - (Math.pow(R, 2)));
        return x;
    }

    public double getR85(double R, double K) {
        double r85 = 0;
        r85 = R * K;
        return r85;
    }

    public double getZ85(double R85, double X) {
        double Z85 = 0;
        Z85 = Math.sqrt((Math.pow(R85, 2)) + (Math.pow(X, 2)));
        return Z85;
    }

    public int getFase() {
        return Integer.parseInt(combofasestransformador.getSelectedItem().toString());
    }

    public double getKVA() {
        return Double.parseDouble(cjpotencia.getText());
    }

    public void CargarMasDatos() {
        try {
            switch (getFase()) {
                case 1:
                    if (Double.parseDouble(cjpotencia.getText().replace(",", ".")) == 3.0) {
                        cjanchodimension.setText("" + 250);
                        cjlargodimension.setText("" + 250);
                        cjaltodimension.setText("" + 450);
                        cjnumerodelementos.setText("" + 0);
                        cjlargorefrigeracion.setText("" + 0);
                        cjaltorefrigeracion.setText("" + 0);
                    }
                    if (Double.parseDouble(cjpotencia.getText().replace(",", ".")) == 5.0) {
                        cjanchodimension.setText("" + 280);
                        cjlargodimension.setText("" + 280);
                        cjaltodimension.setText("" + 500);
                        cjnumerodelementos.setText("" + 0);
                        cjlargorefrigeracion.setText("" + 0);
                        cjaltorefrigeracion.setText("" + 0);
                    }
                    if (Double.parseDouble(cjpotencia.getText().replace(",", ".")) == 10.0) {
                        cjanchodimension.setText("" + 320);
                        cjlargodimension.setText("" + 320);
                        cjaltodimension.setText("" + 550);
                        cjnumerodelementos.setText("" + 0);
                        cjlargorefrigeracion.setText("" + 0);
                        cjaltorefrigeracion.setText("" + 0);
                    }
                    if (Double.parseDouble(cjpotencia.getText().replace(",", ".")) == 15.0) {
                        cjanchodimension.setText("" + 350);
                        cjlargodimension.setText("" + 350);
                        cjaltodimension.setText("" + 550);
                        cjnumerodelementos.setText("" + 0);
                        cjlargorefrigeracion.setText("" + 0);
                        cjaltorefrigeracion.setText("" + 0);
                    }
                    if (Double.parseDouble(cjpotencia.getText().replace(",", ".")) == 25.0) {
                        cjanchodimension.setText("" + 380);
                        cjlargodimension.setText("" + 380);
                        cjaltodimension.setText("" + 550);
                        cjnumerodelementos.setText("" + 0);
                        cjlargorefrigeracion.setText("" + 0);
                        cjaltorefrigeracion.setText("" + 0);
                    }
                    if (Double.parseDouble(cjpotencia.getText().replace(",", ".")) == 37.5) {
                        cjanchodimension.setText("" + 420);
                        cjlargodimension.setText("" + 420);
                        cjaltodimension.setText("" + 600);
                        cjnumerodelementos.setText("" + 0);
                        cjlargorefrigeracion.setText("" + 0);
                        cjaltorefrigeracion.setText("" + 0);
                    }
                    if (Double.parseDouble(cjpotencia.getText().replace(",", ".")) == 50.0) {
                        cjanchodimension.setText("" + 450);
                        cjlargodimension.setText("" + 450);
                        cjaltodimension.setText("" + 650);
                        cjnumerodelementos.setText("" + 6);
                        cjlargorefrigeracion.setText("" + 300);
                        cjaltorefrigeracion.setText("" + 480);
                    }
                    if (Double.parseDouble(cjpotencia.getText().replace(",", ".")) == 75.0) {
                        cjanchodimension.setText("" + 480);
                        cjlargodimension.setText("" + 480);
                        cjaltodimension.setText("" + 700);
                        cjnumerodelementos.setText("" + 8);
                        cjlargorefrigeracion.setText("" + 300);
                        cjaltorefrigeracion.setText("" + 480);
                    }
                    break;

                case 3:
                    if (Double.parseDouble(cjpotencia.getText().replace(",", ".")) == 15.0) {
                        cjanchodimension.setText("" + 350);
                        cjlargodimension.setText("" + 280);
                        cjaltodimension.setText("" + 500);
                        cjnumerodelementos.setText("" + 0);
                        cjlargorefrigeracion.setText("" + 0);
                        cjaltorefrigeracion.setText("" + 0);
                    }
                    if (Double.parseDouble(cjpotencia.getText().replace(",", ".")) == 30.0) {
                        cjanchodimension.setText("" + 500);
                        cjlargodimension.setText("" + 320);
                        cjaltodimension.setText("" + 500);
                        cjnumerodelementos.setText("" + 0);
                        cjlargorefrigeracion.setText("" + 0);
                        cjaltorefrigeracion.setText("" + 0);
                    }
                    if (Double.parseDouble(cjpotencia.getText().replace(",", ".")) == 45.0) {
                        cjanchodimension.setText("" + 600);
                        cjlargodimension.setText("" + 350);
                        cjaltodimension.setText("" + 550);
                        cjnumerodelementos.setText("" + 0);
                        cjlargorefrigeracion.setText("" + 0);
                        cjaltorefrigeracion.setText("" + 0);
                    }
                    if (Double.parseDouble(cjpotencia.getText().replace(",", ".")) == 75.0) {
                        cjanchodimension.setText("" + 620);
                        cjlargodimension.setText("" + 380);
                        cjaltodimension.setText("" + 600);
                        cjnumerodelementos.setText("" + 6);
                        cjlargorefrigeracion.setText("" + 300);
                        cjaltorefrigeracion.setText("" + 480);
                    }
                    if (Double.parseDouble(cjpotencia.getText().replace(",", ".")) == 112.5) {
                        cjanchodimension.setText("" + 700);
                        cjlargodimension.setText("" + 430);
                        cjaltodimension.setText("" + 650);
                        cjnumerodelementos.setText("" + 10);
                        cjlargorefrigeracion.setText("" + 300);
                        cjaltorefrigeracion.setText("" + 480);
                    }
                    if (Double.parseDouble(cjpotencia.getText().replace(",", ".")) == 150.0) {
                        cjanchodimension.setText("" + 750);
                        cjlargodimension.setText("" + 480);
                        cjaltodimension.setText("" + 700);
                        cjnumerodelementos.setText("" + 14);
                        cjlargorefrigeracion.setText("" + 300);
                        cjaltorefrigeracion.setText("" + 480);
                    }
                    if (Double.parseDouble(cjpotencia.getText().replace(",", ".")) == 225) {
                        cjanchodimension.setText("" + 800);
                        cjlargodimension.setText("" + 520);
                        cjaltodimension.setText("" + 750);
                        cjnumerodelementos.setText("" + 18);
                        cjlargorefrigeracion.setText("" + 300);
                        cjaltorefrigeracion.setText("" + 480);
                    }
                    break;

            }
        } catch (Exception e) {
        }
    }
    
    public void FuncionVp() {
        if (!cjtensionprimaria.getText().isEmpty()) {
            cjrelacionuno.setText(cjtensionprimaria.getText());
            try {
                int VP = Integer.parseInt(cjtensionprimaria.getText());
                int fase = Integer.parseInt(combofasestransformador.getSelectedItem().toString());
                HallarPolaridad(combofasestransformador, cjtensionprimaria);
                if (VP <= 1200) {
                    cjtensionserieuno.setText("1.2");
                    cjnivelbasicodeaislamientouno.setText("30");
                    cjtensionseriedos.setText("1.2");
                }
                if (7000 <= VP && VP <= 15000) {
                    cjtensionserieuno.setText("15");
                    cjnivelbasicodeaislamientouno.setText("95");
                } else if (16000 <= VP && VP <= 25000) {
                    cjtensionserieuno.setText("25");
                    cjnivelbasicodeaislamientouno.setText("125");
                } else if (26000 <= VP && VP <= 38000) {
                    cjtensionserieuno.setText("36");
                    cjnivelbasicodeaislamientouno.setText("200");
                } else if (39000 <= VP && VP <= 52000) {
                    cjtensionserieuno.setText("52");
                    cjnivelbasicodeaislamientouno.setText("250");
                }
                CalcularCorrientePrimaria();
                HallarReg();
                CalcularCorrienteSecundaria();
                if (!cjtensionprimaria.getText().isEmpty() && !HayQueActualizar) {
                    CargarTablaUno(Integer.parseInt(cjtensionprimaria.getText()), (int) cjposicionconmutador.getValue(), combofasestransformador);
                }
                cjtensionsecundaria.grabFocus();
            } catch (NumberFormatException e) {
                M("Error en la escritura de la tension primaria, hay un caracter inconcluso\n" + e, mal);
            }
        } else {
            M("La tension primaria no puede estar vacia", mal);
        }
    }
    
    public void FuncionVs() {
        if (!cjtensionsecundaria.getText().isEmpty()) {
            try {
                if (Double.parseDouble(cjtensionsecundaria.getText()) <= 1200) {
                    cjnivelbasicodeaislamientodos.setText("30");
                } else if (7000 < Double.parseDouble(cjtensionsecundaria.getText()) && Double.parseDouble(cjtensionsecundaria.getText()) <= 15000) {
                    cjnivelbasicodeaislamientodos.setText("95");
                }
                cjrelaciondos.setText(cjtensionsecundaria.getText());
                int VS = Integer.parseInt(cjtensionsecundaria.getText());
                cjtensionseriedos.setText((VS <= 1200) ? "1.2" : "15");
                CalcularCorrientePrimaria();
                CalcularCorrienteSecundaria();
                MostrarNominalTablaDos();
                cjtensionBT.setText("" + FORMULAS.HallarTensionBT(cjtensionsecundaria));
                cjvoltajeBT.setText(cjtensionsecundaria.getText());
            } catch (NumberFormatException e) {
                M("ERROR!\nHay caracteres inconclusos en la tension que acabas de escribir\nverifica que este bien escrita", mal);
            }
            if(!cjpromedioresistenciaprimaria.getText().isEmpty() && !cjpromedioresistenciasecundaria.getText().isEmpty()) {
                FORMULAS.HallarI2r(combofasestransformador, cjcorrienteprimaria, cjcorrientesecundaria, cjpromedioresistenciaprimaria, cjpromedioresistenciasecundaria, cji2r);
            }
            try{
                if (!cjvcc.getText().isEmpty()){
                    double k = getK(kc, cjtemperaturadeprueba);
                    double I2r85 = getI2R85(k, cji2r);
                    FORMULAS.HallarPcu85(cjperdidasdecobremedidas, cji2r, k, I2r85, cjpcureferidoa85);
                    HallarI2R85();
                    double R = getR(cjperdidasdecobremedidas, cjpotencia);
                    double K = getK(kc, cjtemperaturadeprueba);
                    double R85 = getR85(R, K);
                    double Z = getZ(cjvcc, cjtensionprimaria);
                    double X = getX(Z, R);
                    double Z85 = FORMULAS.QuitarDecimales(getZ85(R85, X), 2);
                    cjz85.setText(String.valueOf(Z85));
                    HallarEf();
                    HallarReg();
                }
            } catch (NumberFormatException | NullPointerException e) {
                M("IMPOSIBLE REALIZAR LOS CALCULOS - LA TEMPERATURA DE PRUEBA ESTA VACIA O ESTA MAL ESCRITA", mal);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SubMenuVerifica = new javax.swing.JPopupMenu();
        SubMenuVerificarErrores = new javax.swing.JMenuItem();
        SubMenuVerProtocolo = new javax.swing.JPopupMenu();
        VerProtocolo = new javax.swing.JMenuItem();
        EliminarProtocolo = new javax.swing.JMenuItem();
        SubMenuJList = new javax.swing.JPopupMenu();
        SubMenuAbrir = new javax.swing.JMenuItem();
        SubMenuAgregarCliente = new javax.swing.JPopupMenu();
        SubMenuNuevoCliente = new javax.swing.JMenuItem();
        CONTENEDOR = new javax.swing.JTabbedPane();
        TODO = new javax.swing.JTabbedPane();
        PANELTODO = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cjnoprotocolo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cjcliente = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        comboservicio = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cjserieno = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cjpotencia = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        combofasestransformador = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        cjfrecuencia1 = new javax.swing.JSpinner();
        jLabel15 = new javax.swing.JLabel();
        comborefrigeracion = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        cjtensionserieuno = new javax.swing.JTextField();
        cjtensionseriedos = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cjcalentamientodevanado = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        cjalturadiseno = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        cjfechafabricacion = new javax.swing.JFormattedTextField();
        jLabel19 = new javax.swing.JLabel();
        comboderivacionprimaria = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        cjtensionprimaria = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        cjtensionsecundaria = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        cjderivacionsecundaria = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        cjcorrienteprimaria = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        cjcorrientesecundaria = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        cjcorrienteprimariadespacho = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        cjcorrientesecundariadespacho = new javax.swing.JTextField();
        cjnivelbasicodeaislamientouno = new javax.swing.JTextField();
        cjnivelbasicodeaislamientodos = new javax.swing.JTextField();
        comboclasedeaislamiento = new javax.swing.JComboBox();
        cjnoempresa = new javax.swing.JTextField();
        cjmarca = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        cjvoltajeBT = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        cjcorrientefaseIu = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        cjcorrientefaseIv = new javax.swing.JTextField();
        lblIv = new javax.swing.JLabel();
        lblIw = new javax.swing.JLabel();
        cjcorrientefaseIw = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        cjpromediocorrienteI = new javax.swing.JTextField();
        cjiogarantizado = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        cjpomedido = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        cjpogarantizado = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        cjperdidasdecobremedidas = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        cji2r = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        cjvcc = new javax.swing.JTextField();
        lblmostrarpcu = new javax.swing.JLabel();
        cjpcureferidoa85 = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        cjI2Ra85 = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        cjz85 = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        cjpccgarantizado = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        cjuzgarantizado = new javax.swing.JTextField();
        cjz = new javax.swing.JTextField();
        jLabel79 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablauno = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablados = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        cjposicionconmutador = new javax.swing.JSpinner();
        jLabel26 = new javax.swing.JLabel();
        cjtemperaturadeprueba = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        PanelLiquidoAislante = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        comboliquidoaislante = new javax.swing.JComboBox();
        jLabel28 = new javax.swing.JLabel();
        comboreferenciaaceite = new javax.swing.JComboBox();
        jLabel29 = new javax.swing.JLabel();
        cjruptura = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        cjmetodo = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        cjratl = new javax.swing.JSpinner();
        jLabel31 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        cjtensiondeprueba = new javax.swing.JSpinner();
        jLabel30 = new javax.swing.JLabel();
        cjATcontraBT = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        cjATcontraTierra = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        cjBTcontraTierra = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        cjrelacionuno = new javax.swing.JTextField();
        cjrelaciondos = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        combogrupodeconexion = new javax.swing.JComboBox();
        jLabel40 = new javax.swing.JLabel();
        combopolaridad = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        lblresfasev = new javax.swing.JLabel();
        lblresfasew = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        lblyz = new javax.swing.JLabel();
        cjresistenciayz = new javax.swing.JTextField();
        lblzx = new javax.swing.JLabel();
        cjresistenciazx = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        cjresistenciaxy = new javax.swing.JTextField();
        combomaterialconductor = new javax.swing.JComboBox();
        cjresistenciafasev = new javax.swing.JTextField();
        cjresistenciafasew = new javax.swing.JTextField();
        cjpromedioresistenciaprimaria = new javax.swing.JTextField();
        cjpromedioresistenciasecundaria = new javax.swing.JTextField();
        cjresistenciafaseu = new javax.swing.JTextField();
        combomaterialconductordos = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        cjBTcontraATytierra = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        cjATcontraBTytierra = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        cjtiempoestandar = new javax.swing.JTextField();
        cjtensionBT = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        cjfrecuencia = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        cjtiempodeprueba = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        cjreg = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        cjef = new javax.swing.JTextField();
        INTERNOPANELCARACTERISTICAS = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        cjmasatotal = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        cjvolliquido = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        cjlargodimension = new javax.swing.JTextField();
        jLabel71 = new javax.swing.JLabel();
        cjanchodimension = new javax.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        cjaltodimension = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        cjespesor = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        cjcolor = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel75 = new javax.swing.JLabel();
        cjnumerodelementos = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        cjaltorefrigeracion = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        cjlargorefrigeracion = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jLabel78 = new javax.swing.JLabel();
        cjnumerodelote = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        garantia = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel80 = new javax.swing.JLabel();
        cjdiseno = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        cjobservaciones = new javax.swing.JTextArea();
        jLabel84 = new javax.swing.JLabel();
        cjfechadesalida = new javax.swing.JFormattedTextField();
        LISTAPROTOCOLOS = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        datos = new javax.swing.JTable();
        barra = new javax.swing.JProgressBar();
        jLabel36 = new javax.swing.JLabel();
        cjbuscarporcliente = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        cjbuscarplaca = new javax.swing.JTextField();
        cjbuscarpormarca = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        comboBuscarPorLote = new javax.swing.JComboBox<>();
        jLabel81 = new javax.swing.JLabel();
        cjFechaDesde = new com.toedter.calendar.JDateChooser();
        cjFechaHasta = new com.toedter.calendar.JDateChooser();
        jLabel82 = new javax.swing.JLabel();
        PDFPROTOCOLOS = new javax.swing.JTabbedPane();
        jPanel17 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        cjbuscarpdf = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        btnbuscarpdf = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        submenuProtocoloExcel = new javax.swing.JMenuItem();
        SubMenuExcelCompleto = new javax.swing.JMenuItem();
        SubMenuExcelControl = new javax.swing.JMenuItem();
        inteligente = new javax.swing.JCheckBoxMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        checkMostrarReporte = new javax.swing.JCheckBoxMenuItem();
        jMenu4 = new javax.swing.JMenu();
        subMenuItemResultadosDeEnsayo = new javax.swing.JMenuItem();
        subMenuItemCaracteristicasTrafo = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();

        SubMenuVerificarErrores.setText("jMenuItem2");
        SubMenuVerificarErrores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubMenuVerificarErroresActionPerformed(evt);
            }
        });
        SubMenuVerifica.add(SubMenuVerificarErrores);

        VerProtocolo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/ver.png"))); // NOI18N
        VerProtocolo.setText("Abrir protocolo");
        VerProtocolo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VerProtocoloActionPerformed(evt);
            }
        });
        SubMenuVerProtocolo.add(VerProtocolo);

        EliminarProtocolo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/Borrar.png"))); // NOI18N
        EliminarProtocolo.setText("Eliminar Protocolo");
        EliminarProtocolo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarProtocoloActionPerformed(evt);
            }
        });
        SubMenuVerProtocolo.add(EliminarProtocolo);

        SubMenuAbrir.setText("Abrir Protocolo");
        SubMenuAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubMenuAbrirActionPerformed(evt);
            }
        });
        SubMenuJList.add(SubMenuAbrir);

        SubMenuNuevoCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/cliente.png"))); // NOI18N
        SubMenuNuevoCliente.setText("Registrar Nuevo Cliente");
        SubMenuNuevoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubMenuNuevoClienteActionPerformed(evt);
            }
        });
        SubMenuAgregarCliente.add(SubMenuNuevoCliente);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PROTOCOLO 1.0");
        setIconImage(new ImageIcon(getClass().getResource("/recursos/images/logo.png")).getImage());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        CONTENEDOR.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N

        TODO.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DATOS DEL TRANSFORMADOR", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel1.setText("Protocolo Nº:");

        cjnoprotocolo.setEditable(false);
        cjnoprotocolo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjnoprotocolo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjnoprotocoloKeyTyped(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel2.setText("Cliente:");

        cjcliente.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjcliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjcliente.setComponentPopupMenu(SubMenuAgregarCliente);
        cjcliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjclienteKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel3.setText("Transformador:");

        comboservicio.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        comboservicio.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NUEVO", "RECONSTRUIDO", "REPARADO", "MANTENIMIENTO" }));
        comboservicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboservicioActionPerformed(evt);
            }
        });
        comboservicio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                comboservicioKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel4.setText("Serie Nº:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel5.setText("No. Empresa:");

        cjserieno.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjserieno.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjserieno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjserienoKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel6.setText("Marca:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel7.setText("Potencia:");

        cjpotencia.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjpotencia.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjpotencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjpotenciaKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel8.setText("Fases:");

        combofasestransformador.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        combofasestransformador.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "3" }));
        combofasestransformador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combofasestransformadorActionPerformed(evt);
            }
        });
        combofasestransformador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                combofasestransformadorKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel9.setText("Frecuencia(Hz):");

        cjfrecuencia1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjfrecuencia1.setModel(new javax.swing.SpinnerListModel(new String[] {"60", "50"}));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel15.setText("Refrigera.");

        comborefrigeracion.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        comborefrigeracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comborefrigeracionActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Ten. Serie en /Kv");

        cjtensionserieuno.setEditable(false);
        cjtensionserieuno.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjtensionserieuno.setForeground(new java.awt.Color(0, 102, 255));
        cjtensionserieuno.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        cjtensionseriedos.setEditable(false);
        cjtensionseriedos.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjtensionseriedos.setForeground(new java.awt.Color(0, 102, 255));
        cjtensionseriedos.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel11.setText("NBA AT/BT:");

        cjcalentamientodevanado.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjcalentamientodevanado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjcalentamientodevanado.setText("65");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel16.setText("Calen. Devan.");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel17.setText("Clase Aisl:");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel18.setText("Alt. Diseño:");

        cjalturadiseno.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjalturadiseno.setText("1000 MSNM");
        cjalturadiseno.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel12.setText("Fecha Fab.");

        cjfechafabricacion.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("MM/yyyy"))));
        cjfechafabricacion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjfechafabricacion.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjfechafabricacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjfechafabricacionKeyTyped(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel19.setText("Deriv. Prim.");

        comboderivacionprimaria.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel13.setText("Ten. Prim:");

        cjtensionprimaria.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjtensionprimaria.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjtensionprimaria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjtensionprimariaKeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel14.setText("Ten. Sec:");

        cjtensionsecundaria.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjtensionsecundaria.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjtensionsecundaria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjtensionsecundariaKeyTyped(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel20.setText("Deriv. Sec.");

        cjderivacionsecundaria.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjderivacionsecundaria.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjderivacionsecundaria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjderivacionsecundariaKeyTyped(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel21.setText("Corr. Pri.");

        cjcorrienteprimaria.setEditable(false);
        cjcorrienteprimaria.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjcorrienteprimaria.setForeground(new java.awt.Color(0, 102, 255));
        cjcorrienteprimaria.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjcorrienteprimaria.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cjcorrienteprimariaFocusGained(evt);
            }
        });
        cjcorrienteprimaria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjcorrienteprimariaKeyTyped(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel22.setText("Corr. Sec.");

        cjcorrientesecundaria.setEditable(false);
        cjcorrientesecundaria.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjcorrientesecundaria.setForeground(new java.awt.Color(0, 102, 255));
        cjcorrientesecundaria.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjcorrientesecundaria.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cjcorrientesecundariaFocusGained(evt);
            }
        });
        cjcorrientesecundaria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjcorrientesecundariaKeyTyped(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel23.setText("Corr. Pri. Desp:");

        cjcorrienteprimariadespacho.setEditable(false);
        cjcorrienteprimariadespacho.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjcorrienteprimariadespacho.setForeground(new java.awt.Color(0, 102, 255));
        cjcorrienteprimariadespacho.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjcorrienteprimariadespacho.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjcorrienteprimariadespachoKeyTyped(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel24.setText("Corr. Sec. Desp:");

        cjcorrientesecundariadespacho.setEditable(false);
        cjcorrientesecundariadespacho.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjcorrientesecundariadespacho.setForeground(new java.awt.Color(0, 102, 255));
        cjcorrientesecundariadespacho.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjcorrientesecundariadespacho.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjcorrientesecundariadespachoKeyTyped(evt);
            }
        });

        cjnivelbasicodeaislamientouno.setEditable(false);
        cjnivelbasicodeaislamientouno.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjnivelbasicodeaislamientouno.setForeground(new java.awt.Color(0, 102, 255));
        cjnivelbasicodeaislamientouno.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        cjnivelbasicodeaislamientodos.setEditable(false);
        cjnivelbasicodeaislamientodos.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjnivelbasicodeaislamientodos.setForeground(new java.awt.Color(0, 102, 255));
        cjnivelbasicodeaislamientodos.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        comboclasedeaislamiento.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        comboclasedeaislamiento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboclasedeaislamientoItemStateChanged(evt);
            }
        });

        cjnoempresa.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjnoempresa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjnoempresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjnoempresaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjnoempresaKeyTyped(evt);
            }
        });

        cjmarca.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjmarca.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cjfrecuencia1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(comboservicio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cjserieno, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cjpotencia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(cjnoempresa, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cjmarca, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(combofasestransformador, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(16, 16, 16)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cjnoprotocolo, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cjcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22)
                            .addComponent(jLabel19))
                        .addGap(11, 11, 11))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15))
                                .addGap(21, 21, 21)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(comborefrigeracion, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(cjtensionserieuno, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cjnivelbasicodeaislamientouno, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cjtensionseriedos, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cjnivelbasicodeaislamientodos, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24))))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cjcorrientesecundariadespacho)
                            .addComponent(cjcorrienteprimariadespacho)
                            .addComponent(cjderivacionsecundaria)
                            .addComponent(comboderivacionprimaria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(1, 1, 1))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cjalturadiseno, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                            .addComponent(cjcalentamientodevanado, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cjcorrienteprimaria)
                            .addComponent(cjtensionsecundaria)
                            .addComponent(cjtensionprimaria)
                            .addComponent(cjfechafabricacion)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(cjcorrientesecundaria)
                                .addGap(1, 1, 1))
                            .addComponent(comboclasedeaislamiento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(cjcalentamientodevanado, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(comboclasedeaislamiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjalturadiseno, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjfechafabricacion, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(cjtensionprimaria, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjtensionsecundaria, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjcorrienteprimaria, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjcorrientesecundaria, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboderivacionprimaria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjderivacionsecundaria, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjcorrienteprimariadespacho, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjcorrientesecundariadespacho, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(cjnoprotocolo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cjcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(comboservicio, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cjserieno, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jLabel13))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cjnoempresa, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel14)
                            .addComponent(cjmarca, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cjpotencia, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(combofasestransformador, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cjfrecuencia1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comborefrigeracion, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cjtensionserieuno, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cjtensionseriedos, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10)
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cjnivelbasicodeaislamientouno, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cjnivelbasicodeaislamientodos, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "6) ENSAYO SIN CARGA", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 10))); // NOI18N

        cjvoltajeBT.setEditable(false);
        cjvoltajeBT.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjvoltajeBT.setForeground(new java.awt.Color(0, 102, 255));
        cjvoltajeBT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjvoltajeBT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjvoltajeBTKeyTyped(evt);
            }
        });

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel50.setText("B.T");

        cjcorrientefaseIu.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjcorrientefaseIu.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjcorrientefaseIu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjcorrientefaseIuKeyTyped(evt);
            }
        });

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel51.setText("Iu");

        cjcorrientefaseIv.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjcorrientefaseIv.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjcorrientefaseIv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjcorrientefaseIvKeyTyped(evt);
            }
        });

        lblIv.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        lblIv.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIv.setText("Iv");

        lblIw.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        lblIw.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIw.setText("Iw");

        cjcorrientefaseIw.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjcorrientefaseIw.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjcorrientefaseIw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjcorrientefaseIwKeyTyped(evt);
            }
        });

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel54.setText("Promedio %");

        cjpromediocorrienteI.setEditable(false);
        cjpromediocorrienteI.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjpromediocorrienteI.setForeground(new java.awt.Color(0, 102, 255));
        cjpromediocorrienteI.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjpromediocorrienteI.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjpromediocorrienteIKeyTyped(evt);
            }
        });

        cjiogarantizado.setEditable(false);
        cjiogarantizado.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjiogarantizado.setForeground(new java.awt.Color(0, 102, 255));
        cjiogarantizado.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel55.setText("IoGarantizado");

        cjpomedido.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjpomedido.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjpomedido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjpomedidoKeyTyped(evt);
            }
        });

        jLabel56.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel56.setText("Po Medido");

        cjpogarantizado.setEditable(false);
        cjpogarantizado.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjpogarantizado.setForeground(new java.awt.Color(0, 102, 255));
        cjpogarantizado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjpogarantizado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjpogarantizadoKeyTyped(evt);
            }
        });

        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel57.setText("Po Garantizado");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel50)
                        .addGap(12, 12, 12)
                        .addComponent(cjvoltajeBT, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel54))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblIv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblIw, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cjcorrientefaseIu, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                            .addComponent(cjcorrientefaseIv)
                            .addComponent(cjcorrientefaseIw))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel57)
                            .addComponent(jLabel55)
                            .addComponent(jLabel56))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cjpromediocorrienteI, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cjiogarantizado, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cjpomedido, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cjpogarantizado, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cjvoltajeBT, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cjpromediocorrienteI, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cjiogarantizado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cjcorrientefaseIu, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIv, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cjcorrientefaseIv, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cjpomedido, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIw, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cjcorrientefaseIw, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cjpogarantizado, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "7) ENSAYO DE CORTO CIRCUITO", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 10))); // NOI18N

        cjperdidasdecobremedidas.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjperdidasdecobremedidas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjperdidasdecobremedidas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cjperdidasdecobremedidasFocusLost(evt);
            }
        });
        cjperdidasdecobremedidas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjperdidasdecobremedidasKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjperdidasdecobremedidasKeyTyped(evt);
            }
        });

        jLabel58.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel58.setText("Perdidas Pcu:");

        cji2r.setEditable(false);
        cji2r.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cji2r.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cji2r.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cji2rKeyTyped(evt);
            }
        });

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel59.setText("Vcc");

        cjvcc.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjvcc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjvcc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjvccKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjvccKeyTyped(evt);
            }
        });

        lblmostrarpcu.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        lblmostrarpcu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblmostrarpcu.setText("Pcu a 85º");

        cjpcureferidoa85.setEditable(false);
        cjpcureferidoa85.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjpcureferidoa85.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjpcureferidoa85.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjpcureferidoa85KeyTyped(evt);
            }
        });

        jLabel61.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel61.setText("Pcu Garan.");

        cjI2Ra85.setEditable(false);
        cjI2Ra85.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjI2Ra85.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjI2Ra85.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjI2Ra85KeyTyped(evt);
            }
        });

        jLabel62.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel62.setText("I2r a 85º");

        jLabel63.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel63.setText("Impe. Z(85º)");

        cjz85.setEditable(false);
        cjz85.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjz85.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjz85.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjz85KeyTyped(evt);
            }
        });

        jLabel64.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel64.setText("I2r");

        cjpccgarantizado.setEditable(false);
        cjpccgarantizado.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjpccgarantizado.setForeground(new java.awt.Color(0, 102, 255));
        cjpccgarantizado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjpccgarantizado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjpccgarantizadoKeyTyped(evt);
            }
        });

        jLabel65.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel65.setText("Uz Garan.");

        cjuzgarantizado.setEditable(false);
        cjuzgarantizado.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjuzgarantizado.setForeground(new java.awt.Color(0, 102, 255));
        cjuzgarantizado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjuzgarantizado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjuzgarantizadoKeyTyped(evt);
            }
        });

        cjz.setEditable(false);
        cjz.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjz.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjz.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjzKeyTyped(evt);
            }
        });

        jLabel79.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel79.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel79.setText("Impe. Z%");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jLabel62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(23, 23, 23))
                                    .addComponent(lblmostrarpcu))
                                .addGap(3, 3, 3))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel58)
                                .addGap(7, 7, 7)))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cjperdidasdecobremedidas, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                            .addComponent(cjI2Ra85, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                            .addComponent(cjpcureferidoa85, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel64)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cjpccgarantizado)
                                    .addComponent(cji2r, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel59)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                                .addComponent(cjvcc, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel63)
                                .addGap(7, 7, 7))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel79)
                                .addGap(25, 25, 25)))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cjz)
                            .addComponent(cjz85, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
                        .addGap(21, 21, 21)
                        .addComponent(jLabel65, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjuzgarantizado, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(cjperdidasdecobremedidas, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel59)
                    .addComponent(cjvcc, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblmostrarpcu)
                    .addComponent(jLabel61)
                    .addComponent(cjpcureferidoa85, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cjpccgarantizado, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cjI2Ra85, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel62)
                        .addComponent(jLabel64)
                        .addComponent(cji2r, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63)
                    .addComponent(cjz85, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cjuzgarantizado, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel79)
                    .addComponent(cjz, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tablauno.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tablauno.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ));
        tablauno.setComponentPopupMenu(SubMenuVerifica);
        tablauno.getTableHeader().setReorderingAllowed(false);
        tablauno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tablaunoKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(tablauno);

        tablados.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tablados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        tablados.getTableHeader().setReorderingAllowed(false);
        tablados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabladosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablados);

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ENSAYOS A", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10))); // NOI18N

        cjposicionconmutador.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjposicionconmutador.setModel(new javax.swing.SpinnerNumberModel(2, 1, 5, 1));
        cjposicionconmutador.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                cjposicionconmutadorStateChanged(evt);
            }
        });
        cjposicionconmutador.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cjposicionconmutadorFocusLost(evt);
            }
        });
        cjposicionconmutador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjposicionconmutadorKeyTyped(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setText("Posicion de Conmutador:");

        cjtemperaturadeprueba.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjtemperaturadeprueba.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjtemperaturadeprueba.setText("32");
        cjtemperaturadeprueba.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cjtemperaturadepruebaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cjtemperaturadepruebaFocusLost(evt);
            }
        });
        cjtemperaturadeprueba.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjtemperaturadepruebaKeyTyped(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel25.setText("Temp. ºC:");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cjtemperaturadeprueba)
                .addGap(18, 18, 18)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cjposicionconmutador, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cjtemperaturadeprueba, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(cjposicionconmutador, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelLiquidoAislante.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "1) LIQUIDO AISLANTE", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10))); // NOI18N

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel27.setText("Liq. Aislante");

        comboliquidoaislante.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ACEITE MINERAL", "ACEITE VEGETAL", "NO APLICA" }));
        comboliquidoaislante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboliquidoaislanteActionPerformed(evt);
            }
        });
        comboliquidoaislante.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                comboliquidoaislanteKeyTyped(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText("Referencia:");

        comboreferenciaaceite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboreferenciaaceiteActionPerformed(evt);
            }
        });
        comboreferenciaaceite.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                comboreferenciaaceiteKeyTyped(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel29.setText("Ruptura(Kv):");

        cjruptura.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjruptura.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjruptura.setText("40");
        cjruptura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjrupturaKeyTyped(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel32.setText("Metodo:");

        cjmetodo.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjmetodo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjmetodo.setText("ASTM 877");
        cjmetodo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjmetodoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout PanelLiquidoAislanteLayout = new javax.swing.GroupLayout(PanelLiquidoAislante);
        PanelLiquidoAislante.setLayout(PanelLiquidoAislanteLayout);
        PanelLiquidoAislanteLayout.setHorizontalGroup(
            PanelLiquidoAislanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelLiquidoAislanteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelLiquidoAislanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PanelLiquidoAislanteLayout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(comboliquidoaislante, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelLiquidoAislanteLayout.createSequentialGroup()
                        .addGroup(PanelLiquidoAislanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28)
                            .addComponent(jLabel32)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(PanelLiquidoAislanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cjmetodo, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(PanelLiquidoAislanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(comboreferenciaaceite, javax.swing.GroupLayout.Alignment.TRAILING, 0, 78, Short.MAX_VALUE)
                                .addComponent(cjruptura, javax.swing.GroupLayout.Alignment.TRAILING)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelLiquidoAislanteLayout.setVerticalGroup(
            PanelLiquidoAislanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelLiquidoAislanteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelLiquidoAislanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(comboliquidoaislante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelLiquidoAislanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboreferenciaaceite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelLiquidoAislanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cjruptura, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PanelLiquidoAislanteLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelLiquidoAislanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(cjmetodo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "2) RESISTENCIA DE AISLAMIENTO", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10))); // NOI18N

        cjratl.setModel(new javax.swing.SpinnerNumberModel(60, null, null, 1));

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel31.setText("Tiempo:");

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel33.setText("Ten de Prueba(V):");

        cjtensiondeprueba.setModel(new javax.swing.SpinnerListModel(new String[] {"5000/500", "5000/5000", "500/500"}));

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel30.setText("AT contra BT:");

        cjATcontraBT.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjATcontraBT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjATcontraBT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjATcontraBTKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjATcontraBTKeyTyped(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel34.setText("AT contra Tierra:");

        cjATcontraTierra.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjATcontraTierra.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjATcontraTierra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjATcontraTierraKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjATcontraTierraKeyTyped(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel35.setText("BT contra Tierra");

        cjBTcontraTierra.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjBTcontraTierra.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjBTcontraTierra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjBTcontraTierraKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjBTcontraTierraKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                            .addComponent(jLabel35)
                            .addGap(4, 4, 4))
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel30)
                    .addComponent(jLabel33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cjratl)
                    .addComponent(cjtensiondeprueba)
                    .addComponent(cjATcontraBT)
                    .addComponent(cjATcontraTierra)
                    .addComponent(cjBTcontraTierra))
                .addGap(6, 6, 6))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cjratl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cjtensiondeprueba, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cjATcontraBT, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cjATcontraTierra, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cjBTcontraTierra, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "3) RELACION TRANSFORMACION", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10))); // NOI18N

        cjrelacionuno.setEditable(false);
        cjrelacionuno.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjrelacionuno.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        cjrelaciondos.setEditable(false);
        cjrelaciondos.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjrelaciondos.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel37.setText("Grupo Conexion:");

        combogrupodeconexion.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel40.setText("Polaridad:");

        combopolaridad.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        combopolaridad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combopolaridadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cjrelacionuno, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjrelaciondos, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(combopolaridad, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(combogrupodeconexion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cjrelacionuno, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cjrelaciondos, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(combogrupodeconexion, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(combopolaridad, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "4) RESISTENCIAS ENTRE TERMINALES", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 10))); // NOI18N
        jPanel3.setToolTipText("");

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel41.setText("Res. U-V OHM");

        lblresfasev.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        lblresfasev.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblresfasev.setText("Res. V-W OHM");

        lblresfasew.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        lblresfasew.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblresfasew.setText("Res. W-U OHM");

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel44.setText("Promedio:");

        lblyz.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        lblyz.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblyz.setText("Y-Z mohm");

        cjresistenciayz.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        cjresistenciayz.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjresistenciayz.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjresistenciayzKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjresistenciayzKeyTyped(evt);
            }
        });

        lblzx.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        lblzx.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblzx.setText("Z-X mohm");

        cjresistenciazx.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        cjresistenciazx.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjresistenciazx.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjresistenciazxKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjresistenciazxKeyTyped(evt);
            }
        });

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel48.setText("Promedio:");

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel45.setText("X-Y moh");

        cjresistenciaxy.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        cjresistenciaxy.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjresistenciaxy.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjresistenciaxyKeyTyped(evt);
            }
        });

        combomaterialconductor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "COBRE", "ALUMINIO" }));
        combomaterialconductor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combomaterialconductorActionPerformed(evt);
            }
        });
        combomaterialconductor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                combomaterialconductorKeyTyped(evt);
            }
        });

        cjresistenciafasev.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        cjresistenciafasev.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjresistenciafasev.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjresistenciafasevKeyTyped(evt);
            }
        });

        cjresistenciafasew.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        cjresistenciafasew.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjresistenciafasew.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjresistenciafasewKeyTyped(evt);
            }
        });

        cjpromedioresistenciaprimaria.setEditable(false);
        cjpromedioresistenciaprimaria.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        cjpromedioresistenciaprimaria.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjpromedioresistenciaprimaria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjpromedioresistenciaprimariaKeyTyped(evt);
            }
        });

        cjpromedioresistenciasecundaria.setEditable(false);
        cjpromedioresistenciasecundaria.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        cjpromedioresistenciasecundaria.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjpromedioresistenciasecundaria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjpromedioresistenciasecundariaKeyTyped(evt);
            }
        });

        cjresistenciafaseu.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        cjresistenciafaseu.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjresistenciafaseu.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cjresistenciafaseuFocusLost(evt);
            }
        });
        cjresistenciafaseu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjresistenciafaseuKeyTyped(evt);
            }
        });

        combomaterialconductordos.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "COBRE", "ALUMINIO" }));
        combomaterialconductordos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combomaterialconductordosActionPerformed(evt);
            }
        });
        combomaterialconductordos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                combomaterialconductordosKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(combomaterialconductor, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(combomaterialconductordos, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblresfasev, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblresfasew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel44)
                            .addComponent(jLabel41))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cjresistenciafasew, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cjpromedioresistenciaprimaria, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cjresistenciafasev, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(cjresistenciafaseu, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel45)
                                    .addComponent(lblyz, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cjresistenciaxy, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cjresistenciayz, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblzx)
                                    .addComponent(jLabel48))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cjpromedioresistenciasecundaria, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cjresistenciazx, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jLabel45)
                    .addComponent(cjresistenciaxy, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cjresistenciafaseu, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblresfasev)
                    .addComponent(lblyz)
                    .addComponent(cjresistenciayz, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cjresistenciafasev, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblresfasew)
                    .addComponent(lblzx)
                    .addComponent(cjresistenciazx, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cjresistenciafasew, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel44)
                        .addComponent(cjpromedioresistenciaprimaria, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel48)
                        .addComponent(cjpromedioresistenciasecundaria, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(combomaterialconductor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(combomaterialconductordos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "5) ENSAYO DE AISLAMIENTO", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 10))); // NOI18N

        cjBTcontraATytierra.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjBTcontraATytierra.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjBTcontraATytierra.setText("10");

        jLabel42.setFont(new java.awt.Font("Arial", 1, 9)); // NOI18N
        jLabel42.setText("BT contra AT y Tierra");

        cjATcontraBTytierra.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjATcontraBTytierra.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjATcontraBTytierra.setText("34.5");

        jLabel43.setFont(new java.awt.Font("Arial", 1, 9)); // NOI18N
        jLabel43.setText("AT contra BT y Tierra");

        jLabel52.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel52.setText("Tiempo");

        cjtiempoestandar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjtiempoestandar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjtiempoestandar.setText("60 sg");

        cjtensionBT.setEditable(false);
        cjtensionBT.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjtensionBT.setForeground(new java.awt.Color(0, 102, 255));
        cjtensionBT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjtensionBT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjtensionBTKeyTyped(evt);
            }
        });

        jLabel46.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        jLabel46.setText("Tension B.T");

        cjfrecuencia.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjfrecuencia.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjfrecuencia.setText("414");
        cjfrecuencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjfrecuenciaKeyTyped(evt);
            }
        });

        jLabel47.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        jLabel47.setText("Frecuencia Hz");

        cjtiempodeprueba.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjtiempodeprueba.setForeground(new java.awt.Color(0, 102, 255));
        cjtiempodeprueba.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjtiempodeprueba.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cjtiempodepruebaFocusGained(evt);
            }
        });
        cjtiempodeprueba.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjtiempodepruebaKeyTyped(evt);
            }
        });

        jLabel49.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        jLabel49.setText("Tiem. Prueba");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cjBTcontraATytierra)
                    .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cjATcontraBTytierra)
                    .addComponent(cjtiempoestandar)
                    .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel46)
                    .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cjfrecuencia)
                    .addComponent(jLabel49)
                    .addComponent(cjtiempodeprueba)
                    .addComponent(cjtensionBT))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjBTcontraATytierra, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel46)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjtensionBT, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(cjATcontraBTytierra, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(cjfrecuencia, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(jLabel49))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cjtiempoestandar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cjtiempodeprueba, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "9) REGULACION Y EFICIENCIA", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 10))); // NOI18N

        jLabel69.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel69.setText("Regulacion a Plena Carga Y FP 0.8 (%Reg):");

        cjreg.setEditable(false);
        cjreg.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        cjreg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjreg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjregKeyTyped(evt);
            }
        });

        jLabel70.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel70.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel70.setText("Eficiencia a Plena Caga y Fp 0.8(%Ef):");

        cjef.setEditable(false);
        cjef.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        cjef.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjef.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjefKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel69)
                    .addComponent(jLabel70))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cjreg, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                    .addComponent(cjef))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel69)
                    .addComponent(cjreg, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel70)
                    .addComponent(cjef, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PANELTODOLayout = new javax.swing.GroupLayout(PANELTODO);
        PANELTODO.setLayout(PANELTODOLayout);
        PANELTODOLayout.setHorizontalGroup(
            PANELTODOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PANELTODOLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PANELTODOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PANELTODOLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(PANELTODOLayout.createSequentialGroup()
                        .addGroup(PANELTODOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PANELTODOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(PanelLiquidoAislante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PANELTODOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PANELTODOLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PANELTODOLayout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        PANELTODOLayout.setVerticalGroup(
            PANELTODOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PANELTODOLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PANELTODOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PANELTODOLayout.createSequentialGroup()
                        .addGroup(PANELTODOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(PANELTODOLayout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(PANELTODOLayout.createSequentialGroup()
                                .addComponent(PanelLiquidoAislante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PANELTODOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(14, 14, 14))
                    .addGroup(PANELTODOLayout.createSequentialGroup()
                        .addGroup(PANELTODOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PANELTODOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        TODO.addTab("Resultados y Ensayos", PANELTODO);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "10) CARACTERISTICAS MECANICAS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jLabel66.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel66.setText("Masa Total:");

        cjmasatotal.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjmasatotal.setForeground(new java.awt.Color(0, 102, 255));
        cjmasatotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjmasatotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjmasatotalKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjmasatotalKeyTyped(evt);
            }
        });

        jLabel67.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel67.setText("Vol. Liqui. Aisl(Lts)");

        cjvolliquido.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjvolliquido.setForeground(new java.awt.Color(0, 102, 255));
        cjvolliquido.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjvolliquido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjvolliquidoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjvolliquidoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel66)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cjmasatotal, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel67)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cjvolliquido, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66)
                    .addComponent(cjmasatotal, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel67)
                    .addComponent(cjvolliquido, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "11) DIMENSION APROXIMADA DEL TANQUE PPAL: (mm)", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jLabel68.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel68.setText("Largo:");

        cjlargodimension.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjlargodimension.setForeground(new java.awt.Color(0, 102, 255));
        cjlargodimension.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjlargodimension.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjlargodimensionKeyTyped(evt);
            }
        });

        jLabel71.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel71.setText("Ancho:");

        cjanchodimension.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjanchodimension.setForeground(new java.awt.Color(0, 102, 255));
        cjanchodimension.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjanchodimension.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjanchodimensionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjanchodimensionKeyTyped(evt);
            }
        });

        jLabel72.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel72.setText("Alto:");

        cjaltodimension.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjaltodimension.setForeground(new java.awt.Color(0, 102, 255));
        cjaltodimension.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjaltodimension.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjaltodimensionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjaltodimensionKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel68)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cjlargodimension, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel71)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cjanchodimension, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel72)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cjaltodimension, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel72)
                        .addComponent(cjaltodimension, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel71)
                        .addComponent(cjanchodimension, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel68)
                        .addComponent(cjlargodimension, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "12) PINTURA", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jLabel73.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel73.setText("Espesor:");

        cjespesor.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjespesor.setForeground(new java.awt.Color(0, 102, 255));
        cjespesor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjespesor.setText("110");
        cjespesor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjespesorKeyTyped(evt);
            }
        });

        jLabel74.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel74.setText("Color:");

        cjcolor.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjcolor.setForeground(new java.awt.Color(0, 102, 255));
        cjcolor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjcolor.setText("GRIS");
        cjcolor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjcolorKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel74)
                .addGap(22, 22, 22)
                .addComponent(cjcolor, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel73)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cjespesor, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel73)
                        .addComponent(cjespesor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel74)
                        .addComponent(cjcolor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "13) REFRIGERACION", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jLabel75.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel75.setText("Num. de Elementos:");

        cjnumerodelementos.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjnumerodelementos.setForeground(new java.awt.Color(0, 102, 255));
        cjnumerodelementos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjnumerodelementos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjnumerodelementosKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjnumerodelementosKeyTyped(evt);
            }
        });

        jLabel76.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel76.setText("Alto:");

        cjaltorefrigeracion.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjaltorefrigeracion.setForeground(new java.awt.Color(0, 102, 255));
        cjaltorefrigeracion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjaltorefrigeracion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjaltorefrigeracionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjaltorefrigeracionKeyTyped(evt);
            }
        });

        jLabel77.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel77.setText("Largo:");

        cjlargorefrigeracion.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjlargorefrigeracion.setForeground(new java.awt.Color(0, 102, 255));
        cjlargorefrigeracion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjlargorefrigeracion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjlargorefrigeracionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjlargorefrigeracionKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(cjnumerodelementos, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cjlargorefrigeracion, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cjaltorefrigeracion, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(822, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel75)
                    .addComponent(cjnumerodelementos, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel76)
                    .addComponent(cjaltorefrigeracion, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel77)
                    .addComponent(cjlargorefrigeracion, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " LOTE No", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jLabel78.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel78.setText("Numero de Lote:");

        cjnumerodelote.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjnumerodelote.setForeground(new java.awt.Color(0, 102, 255));
        cjnumerodelote.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjnumerodelote.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjnumerodeloteKeyTyped(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        garantia.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        garantia.setText("Garantia");

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel80.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel80.setText("Diseño N°");

        cjdiseno.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cjdiseno.setForeground(new java.awt.Color(0, 102, 255));
        cjdiseno.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjdiseno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjdisenoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjdisenoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel78)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cjnumerodelote, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(garantia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel80)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cjdiseno, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel80)
                            .addComponent(cjdiseno, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(garantia)
                            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel78)
                                .addComponent(cjnumerodelote, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "OBSERVACIONES", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N

        cjobservaciones.setColumns(20);
        cjobservaciones.setRows(2);
        cjobservaciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjobservacionesKeyTyped(evt);
            }
        });
        jScrollPane3.setViewportView(cjobservaciones);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel84.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel84.setText("Fecha De Salida:");

        try {
            cjfechadesalida.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        cjfechadesalida.setToolTipText("Fecha en la que sale de laboratorio");
        cjfechadesalida.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        cjfechadesalida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjfechadesalidaKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout INTERNOPANELCARACTERISTICASLayout = new javax.swing.GroupLayout(INTERNOPANELCARACTERISTICAS);
        INTERNOPANELCARACTERISTICAS.setLayout(INTERNOPANELCARACTERISTICASLayout);
        INTERNOPANELCARACTERISTICASLayout.setHorizontalGroup(
            INTERNOPANELCARACTERISTICASLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(INTERNOPANELCARACTERISTICASLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(INTERNOPANELCARACTERISTICASLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(INTERNOPANELCARACTERISTICASLayout.createSequentialGroup()
                        .addComponent(jLabel84)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjfechadesalida, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        INTERNOPANELCARACTERISTICASLayout.setVerticalGroup(
            INTERNOPANELCARACTERISTICASLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(INTERNOPANELCARACTERISTICASLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(INTERNOPANELCARACTERISTICASLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cjfechadesalida, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel84))
                .addContainerGap(127, Short.MAX_VALUE))
        );

        TODO.addTab("Caracteristicas del Transformador ( F2 )", INTERNOPANELCARACTERISTICAS);

        CONTENEDOR.addTab("Resultados y Ensayos ( F1 )", TODO);

        datos.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        datos.setComponentPopupMenu(SubMenuVerProtocolo);
        datos.setRowHeight(25);
        datos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datosMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(datos);

        barra.setStringPainted(true);

        jLabel36.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel36.setText("Cliente:");

        cjbuscarporcliente.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
        cjbuscarporcliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjbuscarporclienteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjbuscarporclienteKeyTyped(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel39.setText("Lote:");

        jLabel53.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel53.setText("No. Serie:");

        cjbuscarplaca.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
        cjbuscarplaca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjbuscarplacaKeyReleased(evt);
            }
        });

        cjbuscarpormarca.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
        cjbuscarpormarca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjbuscarpormarcaKeyReleased(evt);
            }
        });

        jLabel60.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel60.setText("Marca:");

        comboBuscarPorLote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBuscarPorLoteActionPerformed(evt);
            }
        });

        jLabel81.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel81.setText("Desde:");

        jLabel82.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel82.setText("Hasta:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1219, Short.MAX_VALUE)
                    .addComponent(barra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel53)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjbuscarplaca, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjbuscarporcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBuscarPorLote, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel60)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjbuscarpormarca, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel81)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjFechaDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel82)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjFechaHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addGap(14, 14, 14)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel36)
                                .addComponent(cjbuscarporcliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel53)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel60)
                                    .addComponent(cjbuscarpormarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel81))
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel39)
                                    .addComponent(comboBuscarPorLote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(cjFechaDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel82)
                                .addComponent(cjFechaHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(cjbuscarplaca, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        LISTAPROTOCOLOS.addTab("Protocolosas", jPanel6);

        CONTENEDOR.addTab("Lista de Protocolos ( F3 )", LISTAPROTOCOLOS);

        jLabel38.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel38.setText("Buscar:");

        cjbuscarpdf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cjbuscarpdfKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cjbuscarpdfKeyTyped(evt);
            }
        });

        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jList1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jList1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jList1KeyTyped(evt);
            }
        });
        jScrollPane5.setViewportView(jList1);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/carpeta.png"))); // NOI18N
        jButton1.setText("Abri Carpeta");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnbuscarpdf.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnbuscarpdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/buscar.png"))); // NOI18N
        btnbuscarpdf.setText("Buscar");
        btnbuscarpdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarpdfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1209, Short.MAX_VALUE)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjbuscarpdf)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnbuscarpdf)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(cjbuscarpdf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(btnbuscarpdf))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
                .addContainerGap())
        );

        PDFPROTOCOLOS.addTab("Protocolos", jPanel17);

        CONTENEDOR.addTab("Protocolos (.pdf) ( F4 )", PDFPROTOCOLOS);

        jMenu1.setText("Archivo");

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/nuevodocumento.png"))); // NOI18N
        jMenuItem5.setText("Nuevo");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/pdf.png"))); // NOI18N
        jMenuItem1.setText("Generar Protocolo .PDF");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/lock.png"))); // NOI18N
        jMenuItem4.setText("Salir");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Opciones");

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/excel.png"))); // NOI18N
        jMenu2.setText("Generar Excel de");

        submenuProtocoloExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/excel.png"))); // NOI18N
        submenuProtocoloExcel.setText("Protocolo Excel .XLSX");
        submenuProtocoloExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submenuProtocoloExcelActionPerformed(evt);
            }
        });
        jMenu2.add(submenuProtocoloExcel);

        SubMenuExcelCompleto.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        SubMenuExcelCompleto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/excel.png"))); // NOI18N
        SubMenuExcelCompleto.setText("Detalles de Transformador");
        SubMenuExcelCompleto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubMenuExcelCompletoActionPerformed(evt);
            }
        });
        jMenu2.add(SubMenuExcelCompleto);

        SubMenuExcelControl.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SPACE, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        SubMenuExcelControl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/excel.png"))); // NOI18N
        SubMenuExcelControl.setText("Consecutivo Protocolo");
        SubMenuExcelControl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubMenuExcelControlActionPerformed(evt);
            }
        });
        jMenu2.add(SubMenuExcelControl);

        jMenu3.add(jMenu2);

        inteligente.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        inteligente.setSelected(true);
        inteligente.setText("Modo Inteligente");
        inteligente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/ver.png"))); // NOI18N
        inteligente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inteligenteActionPerformed(evt);
            }
        });
        jMenu3.add(inteligente);

        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/pdf.png"))); // NOI18N
        jMenuItem6.setText("Comprimir PDF Online");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        checkMostrarReporte.setSelected(true);
        checkMostrarReporte.setText("Mostrar protocolo al guardar");
        checkMostrarReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/ver.png"))); // NOI18N
        jMenu3.add(checkMostrarReporte);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Ir a");

        subMenuItemResultadosDeEnsayo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        subMenuItemResultadosDeEnsayo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/estrella.png"))); // NOI18N
        subMenuItemResultadosDeEnsayo.setText("Resultados de Ensayo");
        subMenuItemResultadosDeEnsayo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subMenuItemResultadosDeEnsayoActionPerformed(evt);
            }
        });
        jMenu4.add(subMenuItemResultadosDeEnsayo);

        subMenuItemCaracteristicasTrafo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        subMenuItemCaracteristicasTrafo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/estrella.png"))); // NOI18N
        subMenuItemCaracteristicasTrafo.setText("Caracteristicas del Transformador");
        subMenuItemCaracteristicasTrafo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subMenuItemCaracteristicasTrafoActionPerformed(evt);
            }
        });
        jMenu4.add(subMenuItemCaracteristicasTrafo);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/estrella.png"))); // NOI18N
        jMenuItem8.setText("Lista de Protocolos");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem8);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/estrella.png"))); // NOI18N
        jMenuItem9.setText("Ver Protocolos en .PDF");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/mini logo.png"))); // NOI18N
        jMenuItem10.setText(" Menu Principal");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem10);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CONTENEDOR)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CONTENEDOR)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public void HallarPolaridad(JComboBox combofasetransformador, JTextField cjtensionprimaria) {
        int fase = Integer.parseInt(combofasetransformador.getSelectedItem().toString());
        int vp = Integer.parseInt(cjtensionprimaria.getText());
        if (fase == 1) {
            if (vp <= 8000) {
                combogrupodeconexion.setSelectedItem("Ii6");
                combopolaridad.setSelectedIndex(1);
            } else {
                combogrupodeconexion.setSelectedItem("Ii0");
                combopolaridad.setSelectedIndex(0);
            }
        } else if (fase == 3) {
            combogrupodeconexion.setSelectedItem("DYn5");
            combopolaridad.setSelectedIndex(0);
        }
    }

    private void SubMenuVerificarErroresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubMenuVerificarErroresActionPerformed
//        try {
//            tablauno.setDefaultRenderer(Object.class, new ColorTablaFases(valores, Integer.parseInt(combofasestransformador.getSelectedItem().toString())));
//        } catch (Exception e) {
//            M("ERROR AL ACTIVAR A VERIFICACION DE LA TABLA UNO\n" + e, mal);
//        }
    }//GEN-LAST:event_SubMenuVerificarErroresActionPerformed

    public void HallarReg() {
        if (!cjperdidasdecobremedidas.getText().isEmpty() && 
            !cjpotencia.getText().isEmpty() && 
            !cjtemperaturadeprueba.getText().isEmpty() && 
            !cjvcc.getText().isEmpty() && 
            !cjtensionprimaria.getText().isEmpty()) {
            
            double R = getR(cjperdidasdecobremedidas, cjpotencia);
            double K = getK(kc, cjtemperaturadeprueba);
            double R85 = getR85(R, K);
            double R852 = Math.pow(getR85(R, K), 2);
            double Z = getZ(cjvcc, cjtensionprimaria);
            cjz.setText("" + FORMULAS.QuitarDecimales(Z, 3));
            double X = getX(Z, R);
            double X2 = Math.pow(X, 2);
            String m = "%R = " + R + "\n";
            m += "K = " + K + "\n";
            m += "R85 = " + getR85(R, K) + "\n";
            m += "Z = " + Z + "\n";
            m += "X = " + X + "\n";
//        JOptionPane.showMessageDialog(null, m);
            double REG = FORMULAS.QuitarDecimales(Math.sqrt(R85 + X2 + (200 * R85 * 0.8) + (200 * X * 0.6) + 10000) - 100, 2);
            REG = R852 + X2 + 200 * R85 * 0.8 + 200 * X * 0.6 + 10000;
            REG = Math.sqrt(REG);
            REG = REG - 100;
            REG = FORMULAS.QuitarDecimales(REG, 2);
            cjreg.setText(String.valueOf(REG));
        }
    }

    public void HallarEf() {
        if (!cjpotencia.getText().isEmpty() && !cjtemperaturadeprueba.getText().isEmpty() && !cjpomedido.getText().isEmpty() && !cjperdidasdecobremedidas.getText().isEmpty() && !cjpcureferidoa85.getText().isEmpty()) {
            try {
                double po = Double.parseDouble(cjpomedido.getText());
                double kva = Double(cjpotencia.getText());
                double K = getK(kc, cjtemperaturadeprueba);
                double I2R85 = getI2R85(K, cji2r);
                double PCU85 = FORMULAS.HallarPcu85(cjperdidasdecobremedidas, cji2r, K, I2R85, cjpcureferidoa85);
                double EF = (0.8 * kva * Math.pow(10, 5)) / (0.8 * kva * Math.pow(10, 3) + po + PCU85);
                String m = "KVA = " + kva + "\n";
                m += "K = " + K + "\n";
                m += "I2R85 = " + I2R85 + "\n";
                m += "PCU85 = " + PCU85 + "\n";
                cjef.setText(String.valueOf(FORMULAS.QuitarDecimales(EF, 2)));
//            M(m,bien);
            } catch (Exception e) {
            }
        }
    }

    public void GenerarReporte() {
        if (!cjnoprotocolo.getText().isEmpty() && !cjcliente.getText().isEmpty() && null != cjfechadesalida.getValue()) {
            HashMap p = new HashMap();
//        System.out.println(new SimpleDateFormat("dd-MMM-yyyy").format(myDate));                 
            p.put("noserie", cjnoprotocolo.getText().trim());
            p.put("cliente", cjcliente.getText().toUpperCase().trim());
            p.put("servicio", comboservicio.getSelectedItem().toString());
            p.put("noempresa", cjnoempresa.getText().trim());
            //p.put("serieno", cjserieno.getText().trim());
            p.put("serieno", cjserieno.getText().trim().split(" * ")[0]);
            p.put("marca", cjmarca.getText().trim());
            p.put("potencia", cjpotencia.getText().trim());
            p.put("fases", combofasestransformador.getSelectedItem().toString());
            p.put("frecuencia1", (String) cjfrecuencia1.getValue());
            p.put("refrigeracion", comborefrigeracion.getSelectedItem().toString());
            p.put("tenserie", cjtensionserieuno.getText() + "/" + cjtensionseriedos.getText());
            p.put("NBA", cjnivelbasicodeaislamientouno.getText() + "/" + cjnivelbasicodeaislamientodos.getText());
            p.put("calentamientodevanado", cjcalentamientodevanado.getText());
            p.put("claseaislamiento", comboclasedeaislamiento.getSelectedItem().toString());
            p.put("fechafabricacion", cjfechafabricacion.getText());
            p.put("altdiseño", cjalturadiseno.getText());
            p.put("vp", cjtensionprimaria.getText());
            p.put("vs", cjtensionsecundaria.getText());
            p.put("derivacionprimaria", comboderivacionprimaria.getSelectedItem().toString());
            p.put("ip", cjcorrienteprimaria.getText());
            p.put("is", cjcorrientesecundaria.getText());
            p.put("ip2", cjcorrienteprimariadespacho.getText());
            p.put("is2", cjcorrientesecundariadespacho.getText());
            /**
             * ***********************************************************************************
             */
            p.put("temperaturadeensayo", cjtemperaturadeprueba.getText() + " °C");
            p.put("posiciondeconmutador", String.valueOf(cjposicionconmutador.getValue()));

            p.put("liquidoaislante", comboliquidoaislante.getSelectedItem().toString());
            p.put("referencia", comboreferenciaaceite.getSelectedItem().toString());
            p.put("tensionderuptura", cjruptura.getText());
            p.put("metodo", cjmetodo.getText());

            p.put("ratl", String.valueOf(cjratl.getValue() + " Seg"));
            p.put("tensiondeprueba", String.valueOf(cjtensiondeprueba.getValue()));
            p.put("ATcontraBT_1", cjATcontraBT.getText());
            p.put("ATcontraTierra", cjATcontraTierra.getText());
            p.put("BTcontraTierra", cjBTcontraTierra.getText() + " Gohm");
            p.put("vp1", cjrelacionuno.getText());
            p.put("vs2", cjrelaciondos.getText());
            p.put("grupodeconexion", combogrupodeconexion.getSelectedItem().toString());
            p.put("polaridad", combopolaridad.getSelectedItem().toString());
            /**
             * *******************************************
             */
            for (int i = 0; i < tablauno.getRowCount(); i++) {
                String tension = String.valueOf(tablauno.getValueAt(i, 1));
                String faseu = String.valueOf(tablauno.getValueAt(i, 2));
                String fasev = String.valueOf(tablauno.getValueAt(i, 3));
                String fasew = String.valueOf(tablauno.getValueAt(i, 4));
                p.put("t" + i, tension);
                p.put("u" + i, faseu);
                p.put("v" + i, fasev);
                p.put("w" + i, fasew);
            }
            for (int i = 0; i < tablados.getRowCount(); i++) {
                String nom = String.valueOf(tablados.getValueAt(i, 0));
                String min = String.valueOf(tablados.getValueAt(i, 1));
                String max = String.valueOf(tablados.getValueAt(i, 2));
                p.put("nom" + i, nom);
                p.put("min" + i, min);
                p.put("max" + i, max);
            }
            /**
             * *******************************************
             */
            p.put("U-V", cjresistenciafaseu.getText());
            p.put("U-W", cjresistenciafasev.getText());
            p.put("V-W", cjresistenciafasew.getText());
            p.put("promedioresprimaria", cjpromedioresistenciaprimaria.getText());
            p.put("material", combomaterialconductor.getSelectedItem().toString());
            p.put("materialdos", combomaterialconductordos.getSelectedItem().toString());
            p.put("X-Y", cjresistenciaxy.getText());
            p.put("Y-Z", cjresistenciayz.getText());
            p.put("Z-X", cjresistenciazx.getText());
            p.put("primedioressecundaria", cjpromedioresistenciasecundaria.getText());
            /**
             * *******************************************
             */
            p.put("BTcontraAT", cjBTcontraATytierra.getText());
            p.put("ATcontraBT", cjATcontraBTytierra.getText());

            p.put("tensionBT", cjtensionBT.getText());
            p.put("frecuencia", cjfrecuencia.getText());
            p.put("tiempoprueba", cjtiempodeprueba.getText());
            /**
             * *******************************************
             */
            p.put("tension6", cjvoltajeBT.getText());
            p.put("Iu", cjcorrientefaseIu.getText());
            p.put("Iv", cjcorrientefaseIv.getText());
            p.put("Iw", cjcorrientefaseIw.getText());
            p.put("promedio6", cjpromediocorrienteI.getText());
            p.put("garantizado6", cjiogarantizado.getText());
            p.put("pomedido", cjpomedido.getText());
            p.put("pogarantizado", cjpogarantizado.getText());
            p.put("perdidasmedidas", cjperdidasdecobremedidas.getText());
            p.put("i2r", cji2r.getText());
            p.put("z", cjz.getText());
            p.put("vcc", cjvcc.getText());
            p.put("92", cjpcureferidoa85.getText());
            p.put("93", cjI2Ra85.getText());
            p.put("94", cjz85.getText());
            p.put("95", cjpccgarantizado.getText());
            p.put("96", cjuzgarantizado.getText());
            /**
             * *******************************************
             */
            String fechacalculada;
            if (HayQueActualizar) {
                fechacalculada = fechaexpedicionparaactualizar;
            } else {
                fechacalculada = fechaexpedicion;
            }
            p.put("fechaexpedicion", fechacalculada);
            p.put("reg", cjreg.getText());
            p.put("ef", cjef.getText());
            p.put("masatotal", cjmasatotal.getText());
            p.put("volumendeliquido", cjvolliquido.getText());
            p.put("largo", cjlargodimension.getText());
            p.put("ancho11", cjanchodimension.getText());
            p.put("alto11", cjaltodimension.getText());
            p.put("color", cjcolor.getText());
            p.put("espesor12", cjespesor.getText());
            p.put("numdeelementos", cjnumerodelementos.getText());
            p.put("alto13", cjaltorefrigeracion.getText());
            p.put("largo13", cjlargorefrigeracion.getText());
            p.put("observaciones", cjobservaciones.getText());
            if (comborefrigeracion.getSelectedIndex() < 2) {
                p.put("referidasa", "Referidas a 85°C");
            } else if (comborefrigeracion.getSelectedIndex() == 2) {
                switch (comboclasedeaislamiento.getSelectedIndex()) {
                    case 0:
                        p.put("referidasa", "Referidas a 75°C");
                        break;
                    case 1:
                        p.put("referidasa", "Referidas a 85°C");
                        break;
                    case 2:
                        p.put("referidasa", "Referidas a 100°C");
                        break;
                    case 3:
                        p.put("referidasa", "Referidas a 120°C");
                        break;
                    case 4:
                        p.put("referidasa", "Referidas a 145°C");
                        break;
                }
            }

            p.put("PROTOCOLONO", Integer.parseInt(cjnoprotocolo.getText().split("-")[1]));
            p.put("NUMEROPROTOCOLO", (cjnoprotocolo.getText()));

            String guardar = "";
            if (!HayQueActualizar) {
                guardar = "INSERT INTO protocolo (id_protocolo, cliente , servicio , estadoservicio , noempresa , serieno , ";
                guardar += "potencia , fase , frecuencia , refrigeracion , tensionserie , nba , nbados , calentamientodevanado , ";
                guardar += "claseaislamiento , altdiseno , fechafabricacion , tensionprimaria , tensionsecundaria , ";
                guardar += "derivacionprimaria , derivacionsecundaria , corrienteprimaria , corrientesecundaria , liquidoaislante , ";
                guardar += "temperaturadeensayo , posiciondeconmutador , referenciadeaceite , tensionderuptura , metodo , ratl , ";
                guardar += "tensiondeprueba , atcontrabt , atcontratierra , btcontratierra , grupodeconexion , polaridad , ";
                guardar += "punou , punov, punow , pdosu, pdosv , pdosw , ptresu , ptresv , ptresw , pcuatrou , pcuatrov , pcuatrow , ";
                guardar += "pcincou , pcincov , pcincow , resuv , resuw , resvw , proresuno , resxy , resyz , reszx , proresdos , ";
                guardar += "materialconductor , iu , iv , iw , promediocorriente , iogarantizado , pomedido , pogarantizado , perdidasdecobre , ";
                guardar += "i2r , vcc , pcua85 , i2r85 , impedancia85 , pcugarantizado , impedanciagarantizado , reg , ef , masatotal , ";
                guardar += "volumendeliquido , largotanque , anchotanque , altotanque , color , espesor , numeroelementos , ";
                guardar += "largorefrigeracion , altorefrigeracion , observaciones , fechaexpedicion , anocreacion , lote , fechasalida , garantia ) ";
                guardar += " VALUES ( '" + cjnoprotocolo.getText().split("-")[1] + "' , '" + cjcliente.getText().toUpperCase().trim() + "' , '" + comboservicio.getSelectedItem().toString() + "' , '" + estado + "' , '" + cjnoempresa.getText().trim() + "' , ";
                guardar += " '" + cjserieno.getText().trim() + "' , ";
                guardar += " '" + cjpotencia.getText().trim().replace(",", ".") + "' , '" + combofasestransformador.getSelectedItem().toString() + "' , '" + (String) cjfrecuencia1.getValue() + "' , ";
                guardar += " '" + comborefrigeracion.getSelectedItem().toString() + "' , '" + cjtensionserieuno.getText() + "/" + cjtensionseriedos.getText() + "' , ";
                guardar += " '" + cjnivelbasicodeaislamientouno.getText() + "' , '" + cjnivelbasicodeaislamientodos.getText() + "' , '" + cjcalentamientodevanado.getText() + "' , '" + comboclasedeaislamiento.getSelectedItem().toString() + "' ,  ";
                guardar += " '" + cjalturadiseno.getText() + "' , '" + cjfechafabricacion.getText() + "' , '" + cjtensionprimaria.getText() + "' , '" + cjtensionsecundaria.getText() + "' , ";
                guardar += " '" + comboderivacionprimaria.getSelectedItem().toString() + "' , '" + cjderivacionsecundaria.getText() + "' , '" + cjcorrienteprimaria.getText() + "' , ";
                guardar += " '" + cjcorrientesecundaria.getText() + "' , '" + comboliquidoaislante.getSelectedItem().toString() + "' , '" + cjtemperaturadeprueba.getText() + "' ,  ";
                guardar += " '" + (String) cjposicionconmutador.getValue().toString() + "' , '" + comboreferenciaaceite.getSelectedItem().toString() + "' , '" + cjruptura.getText() + "' ,  ";
                guardar += " '" + cjmetodo.getText() + "' , '" + (String) cjratl.getValue().toString() + "' , '" + (String) cjtensiondeprueba.getValue().toString() + "' , '" + cjATcontraBT.getText() + "' , ";
                guardar += " '" + cjATcontraTierra.getText() + "' , '" + cjBTcontraTierra.getText() + "' , '" + combogrupodeconexion.getSelectedItem().toString() + "' ,  ";
                guardar += " '" + combopolaridad.getSelectedItem().toString() + "' , '" + tablauno.getValueAt(0, 2) + "' , '" + tablauno.getValueAt(0, 3) + "' , '" + tablauno.getValueAt(0, 4) + "' , ";
                guardar += " '" + tablauno.getValueAt(1, 2) + "' , '" + tablauno.getValueAt(1, 3) + "' , '" + tablauno.getValueAt(1, 4) + "' , ";
                guardar += " '" + tablauno.getValueAt(2, 2) + "' , '" + tablauno.getValueAt(2, 3) + "' , '" + tablauno.getValueAt(2, 4) + "' , ";
                guardar += " '" + tablauno.getValueAt(3, 2) + "' , '" + tablauno.getValueAt(3, 3) + "' , '" + tablauno.getValueAt(3, 4) + "' , ";
                guardar += " '" + tablauno.getValueAt(4, 2) + "' , '" + tablauno.getValueAt(4, 3) + "' , '" + tablauno.getValueAt(4, 4) + "' , ";
                guardar += " '" + cjresistenciafaseu.getText() + "' , '" + cjresistenciafasev.getText() + "' , '" + cjresistenciafasew.getText() + "' , '" + cjpromedioresistenciaprimaria.getText() + "' , ";
                guardar += " '" + cjresistenciaxy.getText() + "' , '" + cjresistenciayz.getText() + "' , '" + cjresistenciazx.getText() + "' , '" + cjpromedioresistenciasecundaria.getText() + "' ,  '" + combomaterialconductor.getSelectedItem().toString() + "-" + combomaterialconductordos.getSelectedItem().toString() + "' , ";
                guardar += " '" + cjcorrientefaseIu.getText() + "' , '" + cjcorrientefaseIv.getText() + "' , '" + cjcorrientefaseIw.getText() + "' , '" + cjpromediocorrienteI.getText() + "' , ";
                guardar += " '" + cjiogarantizado.getText() + "' , '" + cjpomedido.getText() + "' , '" + cjpogarantizado.getText() + "' , '" + cjperdidasdecobremedidas.getText() + "' , ";
                guardar += " '" + cji2r.getText() + "' , '" + cjvcc.getText() + "' , '" + cjpcureferidoa85.getText() + "' , '" + cjI2Ra85.getText() + "' , '" + cjz85.getText() + "' , '" + cjpccgarantizado.getText() + "' , '" + cjuzgarantizado.getText() + "' , ";
                guardar += " '" + cjreg.getText() + "' , '" + cjef.getText() + "' , '" + cjmasatotal.getText() + "' , '" + cjvolliquido.getText() + "' , ";
                guardar += " '" + cjlargodimension.getText() + "' , '" + cjanchodimension.getText() + "' , '" + cjaltodimension.getText() + "' , '" + cjcolor.getText() + "' , ";
                guardar += " '" + cjespesor.getText() + "' , '" + cjnumerodelementos.getText() + "' , '" + cjlargorefrigeracion.getText() + "' , '" + cjaltorefrigeracion.getText() + "' , ";
                try {
                    guardar += " '" + cjobservaciones.getText() + "' , '" + new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + "' , '" + ultimosdosanos + "' , '" + cjnumerodelote.getText().trim() + "' , '" + new SimpleDateFormat("dd/MM/yyyy").parse(cjfechadesalida.getValue().toString()) + "' , ";
                } catch (ParseException ex) {
                    Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
                }
                guardar += " '" + (garantia.isSelected()) + "'  )";
            } else {
                guardar = "UPDATE protocolo SET  ";
                guardar += " cliente='" + cjcliente.getText().toUpperCase().trim() + "' , servicio='" + comboservicio.getSelectedItem().toString() + "' , noempresa='" + cjnoempresa.getText().trim() + "' , ";
                guardar += " serieno='" + cjserieno.getText().trim() + "' ,  ";
                guardar += " potencia='" + cjpotencia.getText().trim().replace(",", ".") + "' , fase='" + combofasestransformador.getSelectedItem().toString() + "' , frecuencia='" + (String) cjfrecuencia1.getValue() + "' , ";
                guardar += " refrigeracion='" + comborefrigeracion.getSelectedItem().toString() + "' , tensionserie='" + cjtensionserieuno.getText() + "/" + cjtensionseriedos.getText() + "' , ";
                guardar += " nba='" + cjnivelbasicodeaislamientouno.getText() + "' , nbados='" + cjnivelbasicodeaislamientodos.getText() + "' , calentamientodevanado='" + cjcalentamientodevanado.getText() + "' , claseaislamiento='" + comboclasedeaislamiento.getSelectedItem().toString() + "' ,  ";
                guardar += " altdiseno='" + cjalturadiseno.getText() + "' , fechafabricacion='" + cjfechafabricacion.getText() + "' , tensionprimaria='" + cjtensionprimaria.getText() + "' , tensionsecundaria='" + cjtensionsecundaria.getText() + "' , ";
                guardar += " derivacionprimaria='" + comboderivacionprimaria.getSelectedItem().toString() + "' , derivacionsecundaria='" + cjderivacionsecundaria.getText() + "' , corrienteprimaria='" + cjcorrienteprimaria.getText() + "' , ";
                guardar += " corrientesecundaria='" + cjcorrientesecundaria.getText() + "' , liquidoaislante='" + comboliquidoaislante.getSelectedItem().toString() + "' , temperaturadeensayo='" + cjtemperaturadeprueba.getText() + "' ,  ";
                guardar += " posiciondeconmutador='" + (String) cjposicionconmutador.getValue().toString() + "' , referenciadeaceite='" + comboreferenciaaceite.getSelectedItem().toString() + "' , tensionderuptura='" + cjruptura.getText() + "' ,  ";
                guardar += " metodo='" + cjmetodo.getText() + "' , ratl='" + (String) cjratl.getValue().toString() + "' , tensiondeprueba='" + (String) cjtensiondeprueba.getValue().toString() + "' , ATcontraBT='" + cjATcontraBT.getText() + "' , ";
                guardar += " ATcontraTierra='" + cjATcontraTierra.getText() + "' , BTcontraTierra='" + cjBTcontraTierra.getText() + "' , grupodeconexion='" + combogrupodeconexion.getSelectedItem().toString() + "' ,  ";
                guardar += " polaridad='" + combopolaridad.getSelectedItem().toString() + "' , punou='" + tablauno.getValueAt(0, 2).toString() + "' , punov='" + tablauno.getValueAt(0, 3) + "' , punow='" + tablauno.getValueAt(0, 4).toString() + "' , ";
                guardar += " pdosu='" + tablauno.getValueAt(1, 2) + "' , pdosv='" + tablauno.getValueAt(1, 3).toString() + "' , pdosw='" + tablauno.getValueAt(1, 4).toString() + "' , ";
                guardar += " ptresu='" + tablauno.getValueAt(2, 2).toString() + "' , ptresv='" + tablauno.getValueAt(2, 3).toString() + "' , ptresw='" + tablauno.getValueAt(2, 4).toString() + "' , ";
                guardar += " pcuatrou='" + tablauno.getValueAt(3, 2).toString() + "' , pcuatrov='" + tablauno.getValueAt(3, 3).toString() + "' , pcuatrow='" + tablauno.getValueAt(3, 4).toString() + "' , ";
                guardar += " pcincou='" + tablauno.getValueAt(4, 2).toString() + "' , pcincov='" + tablauno.getValueAt(4, 3).toString() + "' , pcincow='" + tablauno.getValueAt(4, 4).toString() + "'  , ";
                guardar += " resuv='" + cjresistenciafaseu.getText() + "' , resuw='" + cjresistenciafasev.getText() + "' , resvw='" + cjresistenciafasew.getText() + "' , proresuno='" + cjpromedioresistenciaprimaria.getText() + "' , ";
                guardar += " resxy='" + cjresistenciaxy.getText() + "' , resyz='" + cjresistenciayz.getText() + "' , reszx='" + cjresistenciazx.getText() + "' , proresdos='" + cjpromedioresistenciasecundaria.getText() + "' ,  materialconductor='" + combomaterialconductor.getSelectedItem().toString() + "-" + combomaterialconductordos.getSelectedItem().toString() + "' , ";
                guardar += " iu='" + cjcorrientefaseIu.getText() + "' , iv='" + cjcorrientefaseIv.getText() + "' , iw='" + cjcorrientefaseIw.getText() + "' , promediocorriente='" + cjpromediocorrienteI.getText() + "' , ";
                guardar += " iogarantizado='" + cjiogarantizado.getText() + "' , pomedido='" + cjpomedido.getText() + "' , pogarantizado='" + cjpogarantizado.getText() + "' , perdidasdecobre='" + cjperdidasdecobremedidas.getText() + "' , ";
                guardar += " i2r='" + cji2r.getText() + "' , vcc='" + cjvcc.getText() + "' , PCUa85='" + cjpcureferidoa85.getText() + "' , I2r85='" + cjI2Ra85.getText() + "' , impedancia85='" + cjz85.getText() + "' , pcugarantizado='" + cjpccgarantizado.getText() + "' , impedanciagarantizado='" + cjuzgarantizado.getText() + "' , ";
                guardar += " reg='" + cjreg.getText() + "' , ef='" + cjef.getText() + "' , masatotal='" + cjmasatotal.getText() + "' , volumendeliquido='" + cjvolliquido.getText() + "' , ";
                guardar += " largotanque='" + cjlargodimension.getText() + "' , anchotanque='" + cjanchodimension.getText() + "' , altotanque='" + cjaltodimension.getText() + "' , color='" + cjcolor.getText() + "' , ";
                guardar += " espesor='" + cjespesor.getText() + "' , numeroelementos='" + cjnumerodelementos.getText() + "' , largorefrigeracion='" + cjlargorefrigeracion.getText() + "' , altorefrigeracion='" + cjaltorefrigeracion.getText() + "' , ";
                try {
                    guardar += " observaciones='" + cjobservaciones.getText() + "' , fechaexpedicion='" + fechaexpedicionparaactualizarBD + "' , anocreacion='" + ultimosdosanosaactualizar + "' , lote='" + cjnumerodelote.getText().trim() + "' , ";
                    guardar += " fechasalida='" + new SimpleDateFormat("dd/MM/yyyy").parse(cjfechadesalida.getValue().toString()) + "' , garantia='" + garantia.isSelected() + "' WHERE id_protocolo=" + id_protocoloencontrado + " ";
                } catch (ParseException ex) {
                    Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            try {
                try {
                    if (conexion.CONSULTAR("SELECT serieno, idmarca FROM protocolo WHERE serieno='" + cjserieno.getText().trim() + "' ").next()) {
                        Object[] opciones = {"Continuar Guardando", "Verificar", "Cancelar"};
                        int n = JOptionPane.showOptionDialog(this, "EL NUMERO DE SERIE QUE SE VA A GUARDAR\nYA SE ENCUENTRA REGISTRADO CON LA MISMA MARCA Y EL MISMO NUMERO DE SERIE.", "Numero de Serie y Marca Duplicdos", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
                        switch (n) {
                            case (0):
                                if (conexion.GUARDAR(guardar)) {
                                    if (!HayQueActualizar) {
                                        M("EL PROTOCOLO SE HA REGISTRADO EXITOSAMENTE", bien);
                                    } else {
                                        M("SE EFECTURARON LOS CAMBIOS AL PROTOCOLO DEL CLIENTE " + cjcliente.getText(), bien);
                                    }
                                    JasperReport reporte = (JasperReport) JRLoader.loadObject(new URL(this.getClass().getResource("/REPORTES/PROTOCOLO.jasper").toString()));
                                    JasperPrint print = JasperFillManager.fillReport(reporte, p, conexion.conectar());
                                    JRExporter exporter = new JRPdfExporter();
                                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new java.io.File(RutaPDF() + cjnoprotocolo.getText() + "_" + cjcliente.getText().toUpperCase() + ".pdf"));
                                    exporter.exportReport();
                                    //JasperReport reporte = (JasperReport) JRLoader.loadObject(new File(Metodos.RutaJasperReporte()+"PROTOCOLO.jasper"));
                                    if (checkMostrarReporte.isSelected()) {
                                        JasperViewer jviewer = new JasperViewer(print, false);
                                        //                    print = JasperFillManager.fillReport(reporte, p, new Conexion().getConexion());
                                        jviewer.setTitle("Protocolo N° " + cjnoprotocolo.getText() + " de " + cjcliente.getText().toUpperCase());
                                        jviewer.setVisible(true);
                                    }
                                    Limpiar();
                                    HayQueActualizar = false;
                                    jList1.setModel(buscarPDF(""));
                                    estado = null;
                                    comboservicio.setSelectedIndex(1);
                                }
                                break;
                            case 1:
                                CONTENEDOR.setSelectedComponent(LISTAPROTOCOLOS);
                                Cargar(cjserieno.getText().trim());
                                cjbuscarporcliente.setText(cjserieno.getText().trim());
                                cjbuscarporcliente.grabFocus();
                                repaint();
                                validate();
                                break;
                            case 2:
                                break;
                            default:
                                break;
                        }
                    } else if (conexion.GUARDAR(guardar)) {
                        if (!HayQueActualizar) {
                            M("EL PROTOCOLO SE HA REGISTRADO EXITOSAMENTE", bien);
                        } else {
                            M("SE EFECTURARON LOS CAMBIOS AL PROTOCOLO DEL CLIENTE " + cjcliente.getText(), bien);
                        }
//                        JasperReport reporte = (JasperReport) JRLoader.loadObject(new File(Metodos.RutaJasperReporte() + "PROTOCOLO.jasper"));
                        JasperReport reporte = (JasperReport) JRLoader.loadObject(new URL(this.getClass().getResource("/REPORTES/PROTOCOLO.jasper").toString()));
                        JasperPrint print = JasperFillManager.fillReport(reporte, p, conexion.conectar());
                        JRExporter exporter = new JRPdfExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                        exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new java.io.File(RutaPDF() + cjnoprotocolo.getText() + "_" + cjcliente.getText().toUpperCase() + ".pdf"));
                        exporter.exportReport();
                        if (checkMostrarReporte.isSelected()) {
                            JasperViewer jviewer = new JasperViewer(print, false);
                            //                    print = JasperFillManager.fillReport(reporte, p, new Conexion().getConexion());
                            jviewer.setTitle("Protocolo N° " + cjnoprotocolo.getText() + " de " + cjcliente.getText().toUpperCase());
                            jviewer.setVisible(true);
                        }
                        Limpiar();
                        HayQueActualizar = false;
                        jList1.setModel(buscarPDF(""));
                        estado = null;
                        comboservicio.setSelectedIndex(1);
                    }

                } catch (JRException ex) {
                    Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
                    M("Error al abrir el reporte\n" + ex, mal);
                }
            } catch (Exception e) {
                Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, e);
                M("NO SE PUDO GENERAR EL ARCHIVO .PDF\nES PROBABLE QUE EL SERVIDOR ESTE APAGADO O\nMALA CONFIGURACION EN LA RED EN LA QUE SE ENCUENTRA.\n" + e, mal);
            }

        } else {
            M("INGRESE EL NOMBRE DEL CLIENTE O LA FECHA DE SALIDA DEL TRANSFORMADOR PARA PODER GENERAR EL PROTOCOLO\n", mal);
        }
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        GenerarReporte();
        Cargar("");
        cjfechafabricacion.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("MM/yyyy"))));
        cjfechafabricacion.setText(mesactual + "/" + anoactual);
        /**
         * *********************************GUARDANDO EN LA BASE DE
         * DATOS************************************
         */
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        new Thread(new Tempo()).start();
    }//GEN-LAST:event_formWindowOpened

    private void cjanchodimensionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjanchodimensionKeyTyped
        if (evt.getKeyChar() == 10) {
            cjaltodimension.grabFocus();
        }
    }//GEN-LAST:event_cjanchodimensionKeyTyped

    private void cjaltodimensionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjaltodimensionKeyTyped
        if (evt.getKeyChar() == 10) {
            cjcolor.grabFocus();
        }
    }//GEN-LAST:event_cjaltodimensionKeyTyped

    private void cjnumerodelementosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjnumerodelementosKeyTyped
        if (evt.getKeyChar() == 10) {
            cjlargorefrigeracion.grabFocus();
        }
    }//GEN-LAST:event_cjnumerodelementosKeyTyped

    private void cjlargorefrigeracionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjlargorefrigeracionKeyTyped
        if (evt.getKeyChar() == 10) {
            cjaltorefrigeracion.grabFocus();
        }
    }//GEN-LAST:event_cjlargorefrigeracionKeyTyped

    private void cjaltorefrigeracionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjaltorefrigeracionKeyTyped
        if (evt.getKeyChar() == 10) {
            cjnumerodelote.grabFocus();
        }
    }//GEN-LAST:event_cjaltorefrigeracionKeyTyped

    private void cjlargodimensionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjlargodimensionKeyTyped
        if (evt.getKeyChar() == 10) {
            cjanchodimension.grabFocus();
        }
    }//GEN-LAST:event_cjlargodimensionKeyTyped

    private void subMenuItemResultadosDeEnsayoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subMenuItemResultadosDeEnsayoActionPerformed
        CONTENEDOR.setSelectedComponent(TODO);
        TODO.setSelectedComponent(PANELTODO);
    }//GEN-LAST:event_subMenuItemResultadosDeEnsayoActionPerformed

    private void subMenuItemCaracteristicasTrafoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subMenuItemCaracteristicasTrafoActionPerformed
        TODO.setSelectedComponent(INTERNOPANELCARACTERISTICAS);
    }//GEN-LAST:event_subMenuItemCaracteristicasTrafoActionPerformed

    private void cjefKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjefKeyTyped
        if (evt.getKeyChar() == 10) {
            cjlargodimension.grabFocus();
            HallarEf();
        }
    }//GEN-LAST:event_cjefKeyTyped

    private void cjregKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjregKeyTyped
        if (evt.getKeyChar() == 10) {
            HallarReg();
            cjef.grabFocus();
        }
    }//GEN-LAST:event_cjregKeyTyped

    private void cjtiempodepruebaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjtiempodepruebaKeyTyped
        if (evt.getKeyChar() == 10) {
            cjtiempodeprueba.setText("" + FORMULAS.HallarTiempodePrueba(cjfrecuencia));
            cjreg.grabFocus();
        }
    }//GEN-LAST:event_cjtiempodepruebaKeyTyped

    private void cjtiempodepruebaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cjtiempodepruebaFocusGained
        cjtiempodeprueba.setText("" + FORMULAS.HallarTiempodePrueba(cjfrecuencia));
    }//GEN-LAST:event_cjtiempodepruebaFocusGained

    private void cjfrecuenciaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjfrecuenciaKeyTyped
        if (evt.getKeyChar() == 10) {
            cjtiempodeprueba.grabFocus();
        }
    }//GEN-LAST:event_cjfrecuenciaKeyTyped

    private void cjtensionBTKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjtensionBTKeyTyped
        if (evt.getKeyChar() == 10) {
            cjfrecuencia.grabFocus();
        }
    }//GEN-LAST:event_cjtensionBTKeyTyped

    private void cjpromedioresistenciasecundariaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjpromedioresistenciasecundariaKeyTyped
        if (evt.getKeyChar() == 10) {
            FORMULAS.HallarPromedioR1(cjresistenciaxy, cjresistenciayz, cjresistenciazx, combofasestransformador, cjpromedioresistenciasecundaria);
            combomaterialconductor.grabFocus();
        }
    }//GEN-LAST:event_cjpromedioresistenciasecundariaKeyTyped

    private void cjpromedioresistenciaprimariaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjpromedioresistenciaprimariaKeyTyped
        if (evt.getKeyChar() == 10) {
            FORMULAS.HallarPromedioR1(cjresistenciafaseu, cjresistenciafasev, cjresistenciafasew, combofasestransformador, cjpromedioresistenciaprimaria);
            cjresistenciaxy.grabFocus();
        }
    }//GEN-LAST:event_cjpromedioresistenciaprimariaKeyTyped

    private void cjresistenciafasewKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjresistenciafasewKeyTyped
        if (evt.getKeyChar() == 10) {
            FuncionPromedioResistenciasPrimaria();
            cjresistenciaxy.grabFocus();
        }
    }//GEN-LAST:event_cjresistenciafasewKeyTyped

    private void combomaterialconductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combomaterialconductorActionPerformed

        if (combomaterialconductor.getSelectedItem().toString().equalsIgnoreCase("COBRE")
                && combomaterialconductordos.getSelectedItem().equals("COBRE")) {
            kc = 234.5;
        } else if (combomaterialconductor.getSelectedItem().toString().equalsIgnoreCase("ALUMINIO")
                && combomaterialconductordos.getSelectedItem().equals("ALUMINIO")) {
            kc = 225;
        } else {
            kc = 229;
        }
    }//GEN-LAST:event_combomaterialconductorActionPerformed

    public void FuncionPromedioResistenciaSecundaria() {
        FORMULAS.HallarPromedioR1(cjresistenciaxy, cjresistenciayz, cjresistenciazx, combofasestransformador, cjpromedioresistenciasecundaria);
        cjtiempodeprueba.setText("" + FORMULAS.HallarTiempodePrueba(cjfrecuencia));
    }

    private void cjresistenciaxyKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjresistenciaxyKeyTyped
        if (evt.getKeyChar() == 10) {
            FuncionPromedioResistenciaSecundaria();
            if (combofasestransformador.getSelectedItem().toString().equalsIgnoreCase("1")) {
                combomaterialconductor.grabFocus();
            } else {
                cjresistenciayz.grabFocus();
            }
        }
    }//GEN-LAST:event_cjresistenciaxyKeyTyped

    private void cjresistenciazxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjresistenciazxKeyTyped
        if (evt.getKeyChar() == 10) {
            FuncionPromedioResistenciaSecundaria();
            FORMULAS.HallarI2r(combofasestransformador, cjcorrienteprimaria, cjcorrientesecundaria, cjpromedioresistenciaprimaria, cjpromedioresistenciasecundaria, cji2r);
            combomaterialconductor.grabFocus();
        }
    }//GEN-LAST:event_cjresistenciazxKeyTyped

    private void cjresistenciayzKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjresistenciayzKeyTyped
        if (evt.getKeyChar() == 10) {
            FuncionPromedioResistenciaSecundaria();
            cjresistenciazx.grabFocus();
        }
    }//GEN-LAST:event_cjresistenciayzKeyTyped

    private void combopolaridadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combopolaridadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combopolaridadActionPerformed

    private void cjBTcontraTierraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjBTcontraTierraKeyTyped
        if (!cjBTcontraTierra.getText().isEmpty() && evt.getKeyChar() == 10) {
            tablauno.setRowSelectionInterval(0, 0);
            tablauno.setColumnSelectionInterval(0, 2);
            tablauno.grabFocus();
        }
    }//GEN-LAST:event_cjBTcontraTierraKeyTyped

    private void cjATcontraTierraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjATcontraTierraKeyTyped
        if (!cjATcontraTierra.getText().isEmpty() && evt.getKeyChar() == 10) {
            cjBTcontraTierra.grabFocus();
        }
    }//GEN-LAST:event_cjATcontraTierraKeyTyped

    private void cjATcontraBTKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjATcontraBTKeyTyped
        if (!cjATcontraBT.getText().isEmpty() && evt.getKeyChar() == 10) {
            cjATcontraTierra.grabFocus();
        }
    }//GEN-LAST:event_cjATcontraBTKeyTyped

    private void cjmetodoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjmetodoKeyTyped
        if (evt.getKeyChar() == 10) {
            cjATcontraBT.grabFocus();
        }
    }//GEN-LAST:event_cjmetodoKeyTyped

    private void cjrupturaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjrupturaKeyTyped
        if (evt.getKeyChar() == 10) {
            cjmetodo.grabFocus();
        }
    }//GEN-LAST:event_cjrupturaKeyTyped

    private void comboreferenciaaceiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboreferenciaaceiteKeyTyped
        if (evt.getKeyChar() == 10) {
            cjATcontraBT.grabFocus();
        }
    }//GEN-LAST:event_comboreferenciaaceiteKeyTyped

    private void comboreferenciaaceiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboreferenciaaceiteActionPerformed
        try {
            if (comboreferenciaaceite.getSelectedItem().toString().equalsIgnoreCase("Añadir otro")) {
                if (drra == null) {
                    drra = new DialogoRegistrarReferenciaAceite(this, rootPaneCheckingEnabled);
                }
                drra.setVisible(true);
                Cargarcombo(comboreferenciaaceite, "referenciadeaceite", "nombre_referenciaaceite");
            } else if (comboreferenciaaceite.getSelectedItem().toString().equalsIgnoreCase("Actualizar listado")) {
                Cargarcombo(comboreferenciaaceite, "referenciadeaceite", "nombre_referenciaaceite");
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_comboreferenciaaceiteActionPerformed

    private void comboliquidoaislanteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboliquidoaislanteKeyTyped
        if (evt.getKeyChar() == 10) {
            comboreferenciaaceite.grabFocus();
        }
    }//GEN-LAST:event_comboliquidoaislanteKeyTyped

    private void comboliquidoaislanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboliquidoaislanteActionPerformed
        try {
            if (comboliquidoaislante.getSelectedItem().toString().equalsIgnoreCase("Añadir otro")) {
                if (drla == null) {
                    drla = new DialogoRegistrarLiquidoAislante(this, rootPaneCheckingEnabled);
                }
                drla.setVisible(true);
                Cargarcombo(comboliquidoaislante, "liquidoaislantetransformador", "nombre_liquidoaislante");
            } else if (comboliquidoaislante.getSelectedItem().toString().equalsIgnoreCase("Actualizar listado")) {
                Cargarcombo(comboliquidoaislante, "liquidoaislantetransformador", "nombre_liquidoaislante");
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_comboliquidoaislanteActionPerformed

    private void cjtemperaturadepruebaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjtemperaturadepruebaKeyTyped
        if (evt.getKeyChar() == 10) {
            HallarEf();
            HallarReg();
            cjposicionconmutador.grabFocus();
            cjposicionconmutador.setBorder(new LineBorder(Color.black, 2));
        }
    }//GEN-LAST:event_cjtemperaturadepruebaKeyTyped

    private void cjposicionconmutadorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjposicionconmutadorKeyTyped
        if (evt.getKeyChar() == 10) {
            comboliquidoaislante.grabFocus();
        }
    }//GEN-LAST:event_cjposicionconmutadorKeyTyped

    private void cjposicionconmutadorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cjposicionconmutadorFocusLost
        cjposicionconmutador.setBorder(new LineBorder(Color.gray, 1));
    }//GEN-LAST:event_cjposicionconmutadorFocusLost

    private void cjposicionconmutadorStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_cjposicionconmutadorStateChanged
        int pos = (int) cjposicionconmutador.getValue();
        switch (pos) {
            case 1:
                comboderivacionprimaria.setSelectedIndex(0);
                break;
            case 2:
                comboderivacionprimaria.setSelectedIndex(1);
                break;
            case 3:
                comboderivacionprimaria.setSelectedIndex(2);
                break;
            case 4:
                comboderivacionprimaria.setSelectedIndex(3);
                break;
            case 5:
                comboderivacionprimaria.setSelectedIndex(4);
                break;
        }
        if (!cjtensionprimaria.getText().isEmpty() && HayQueActualizar == false) {
            CargarTablaUno(Integer.parseInt(cjtensionprimaria.getText()), (int) cjposicionconmutador.getValue(), combofasestransformador);
        }
        try {
            MostrarNominalTablaDos();
        } catch (java.lang.ClassCastException | NullPointerException | NumberFormatException e) {
        }
    }//GEN-LAST:event_cjposicionconmutadorStateChanged

    private void tabladosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabladosMouseClicked
        MostrarNominalTablaDos();
    }//GEN-LAST:event_tabladosMouseClicked

    private void cjuzgarantizadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjuzgarantizadoKeyTyped
        if (evt.getKeyChar() == 10) {
            cjtensionBT.grabFocus();
        }
    }//GEN-LAST:event_cjuzgarantizadoKeyTyped

    private void cjpccgarantizadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjpccgarantizadoKeyTyped
        if (evt.getKeyChar() == 10) {
            cjuzgarantizado.grabFocus();
        }
    }//GEN-LAST:event_cjpccgarantizadoKeyTyped

    private void cjz85KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjz85KeyTyped
        if (evt.getKeyChar() == 10) {
            double R = getR(cjperdidasdecobremedidas, cjpotencia);
            double K = getK(kc, cjtemperaturadeprueba);
            double R85 = getR85(R, K);
            double Z = getZ(cjvcc, cjtensionprimaria);
            double X = getX(Z, R);
            double Z85 = FORMULAS.QuitarDecimales(getZ85(R85, X), 2);
            cjz85.setText(String.valueOf(Z85));
            cjpccgarantizado.grabFocus();
        }
    }//GEN-LAST:event_cjz85KeyTyped


    private void cjI2Ra85KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjI2Ra85KeyTyped
        if (evt.getKeyChar() == 10) {
            HallarI2R85();
        }
    }//GEN-LAST:event_cjI2Ra85KeyTyped

    private void cjpcureferidoa85KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjpcureferidoa85KeyTyped
        if(evt.getKeyChar() == 10){
            try {
                double k = getK(kc, cjtemperaturadeprueba);
                double I2r85 = getI2R85(k, cji2r);
                FORMULAS.HallarPcu85(cjperdidasdecobremedidas, cji2r, k, I2r85, cjpcureferidoa85);
                cjI2Ra85.grabFocus();
            } catch (NumberFormatException e){
            }
        }
    }//GEN-LAST:event_cjpcureferidoa85KeyTyped

    private void cjvccKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjvccKeyTyped
        if (evt.getKeyChar() == 10) {
            try {
                if (evt.getKeyChar() == 10 && Integer.parseInt(cjtemperaturadeprueba.getText()) >= 0) {
                    FORMULAS.HallarI2r(combofasestransformador, cjcorrienteprimaria, cjcorrientesecundaria, cjpromedioresistenciaprimaria, cjpromedioresistenciasecundaria, cji2r);
                    try {
                        double k = getK(kc, cjtemperaturadeprueba);
                        double I2r85 = getI2R85(k, cji2r);
                        FORMULAS.HallarPcu85(cjperdidasdecobremedidas, cji2r, k, I2r85, cjpcureferidoa85);
                        HallarI2R85();
                        double R = getR(cjperdidasdecobremedidas, cjpotencia);
                        double K = getK(kc, cjtemperaturadeprueba);
                        double R85 = getR85(R, K);
                        double Z = getZ(cjvcc, cjtensionprimaria);
                        double X = getX(Z, R);
                        double Z85 = FORMULAS.QuitarDecimales(getZ85(R85, X), 2);
                        cjz85.setText(String.valueOf(Z85));
                        cjz.setText("" + FORMULAS.QuitarDecimales(Z, 3));
                        HallarEf();
                        HallarReg();
                        this.cjvcc.setBorder(new LineBorder(Color.gray, 1));
                        if (Double.parseDouble(cjpcureferidoa85.getText()) > Double.parseDouble(cjpccgarantizado.getText())) {
                            try {
                                sonido = AudioSystem.getClip();
                                sonido.open(AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/recursos/audio/error.wav")));
                                sonido.start();
                            } catch (Exception ex) {
                                Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            M("ALERTA\nLAS PCU referidas a (85°C) es mayor a ls PCU Garantizadas", mal);
                        }
                    } catch (NumberFormatException e) {
                        M("IMPOSIBLE REALIZAR LOS CALCULOS - LA TEMPERATURA DE PRUEBA ESTA VACIA O ESTA MAL ESCRITA", mal);
                    }
                }
            } catch (NumberFormatException e) {
                M("ERRROR AL REALIZAR LOS CALCULOS - LA TEMPERATURA DE PRUEBA ESTA VACIA O MAL ESCRITA", mal);
                cjtemperaturadeprueba.setBorder(new LineBorder(Color.red, 2));
            }
        }
    }//GEN-LAST:event_cjvccKeyTyped

    private void cji2rKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cji2rKeyTyped
        if (evt.getKeyChar() == 10) {
            FORMULAS.HallarI2r(combofasestransformador, cjcorrienteprimaria, cjcorrientesecundaria, cjpromedioresistenciaprimaria, cjpromedioresistenciasecundaria, cji2r);
            cjvcc.grabFocus();
        }
    }//GEN-LAST:event_cji2rKeyTyped

    private void cjperdidasdecobremedidasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjperdidasdecobremedidasKeyTyped

        if (evt.getKeyChar() == 10 && !cjperdidasdecobremedidas.getText().isEmpty()) {
            HallarEf();
            HallarReg();
            FORMULAS.HallarI2r(combofasestransformador, cjcorrienteprimaria, cjcorrientesecundaria, cjpromedioresistenciaprimaria, cjpromedioresistenciasecundaria, cji2r);
            double perdidaspcu = Double.parseDouble(cjperdidasdecobremedidas.getText());
            double i2r = Double.parseDouble(cji2r.getText());
            if (perdidaspcu < i2r) {
                try {
                    sonido = AudioSystem.getClip();
                    sonido.open(AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/recursos/audio/error.wav")));
                    sonido.start();
                } catch (Exception ex) {
                    Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
                }
                cjvcc.grabFocus();
                if (JOptionPane.showConfirmDialog(this, "Las Perdidas Medidas es menor a I2r !!\nDesea continuar ? ", "Advertencia", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    cjvcc.grabFocus();
                } else {
                    cjperdidasdecobremedidas.grabFocus();
                    cjperdidasdecobremedidas.setBorder(new LineBorder(Color.red, 2));
                    cji2r.setBorder(new LineBorder(Color.red, 2));
                }
            } else {
                cjvcc.grabFocus();
            }
        }
    }//GEN-LAST:event_cjperdidasdecobremedidasKeyTyped

    private void cjpogarantizadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjpogarantizadoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_cjpogarantizadoKeyTyped

    public void FuncionPoMedido() {
        try {
            double po = Double.parseDouble(cjpomedido.getText());
            double pog = Double.parseDouble(cjpogarantizado.getText());
            if (po > pog) {
                try {
                    sonido = AudioSystem.getClip();
                    sonido.open(AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/recursos/audio/error.wav")));
                    sonido.start();
                } catch (Exception ex) {
                    Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
                }
                cjperdidasdecobremedidas.grabFocus();
                int confirmar = JOptionPane.showConfirmDialog(this, "Las Pérdidas medidas superan las Po Garantizadas!\nDesea Continuar ? ", "Advertencia!", JOptionPane.YES_NO_OPTION);
                if (confirmar == JOptionPane.YES_OPTION) {
                    cjperdidasdecobremedidas.grabFocus();
                } else {
                    cjpomedido.grabFocus();
                    cjpomedido.setBorder(new LineBorder(Color.red, 2));
                    cjpogarantizado.setBorder(new LineBorder(Color.red, 2));
                }
            } else {
                cjperdidasdecobremedidas.grabFocus();
                cjpomedido.setBorder(new LineBorder(Color.gray, 1));
                cjpogarantizado.setBorder(new LineBorder(Color.gray, 1));
            }
            FORMULAS.HallarI2r(combofasestransformador, cjcorrienteprimaria, cjcorrientesecundaria, cjpromedioresistenciaprimaria, cjpromedioresistenciasecundaria, cji2r);
        } catch (NumberFormatException e) {
            M("Error!\nPosibles Razones:\n-El valor de Po Garantizado es Nulo\n-Haz escrito mal el valor de Po Medido\n" + e, mal);
        }
    }
    private void cjpomedidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjpomedidoKeyTyped
        if (evt.getKeyChar() == 10) {
            FuncionPoMedido();
        }
    }//GEN-LAST:event_cjpomedidoKeyTyped

    private void cjpromediocorrienteIKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjpromediocorrienteIKeyTyped
        if (evt.getKeyChar() == 10) {
            FORMULAS.HallarCorrienteEnI(cjcorrientefaseIu, cjcorrientefaseIv, cjcorrientefaseIw, cjcorrientesecundaria, combofasestransformador, cjpromediocorrienteI);
            cjpomedido.grabFocus();
        }
    }//GEN-LAST:event_cjpromediocorrienteIKeyTyped

    private void cjcorrientefaseIwKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjcorrientefaseIwKeyTyped
        if (evt.getKeyChar() == 10) {
            FuncionPromedioCorrientes();
            cjpomedido.grabFocus();
        }
    }//GEN-LAST:event_cjcorrientefaseIwKeyTyped

    private void cjcorrientefaseIvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjcorrientefaseIvKeyTyped
        if (evt.getKeyChar() == 10) {
            FuncionPromedioCorrientes();
            cjcorrientefaseIw.grabFocus();
        }
    }//GEN-LAST:event_cjcorrientefaseIvKeyTyped

    public void FuncionPromedioCorrientes() {
        if (combofasestransformador.getSelectedItem().toString().equalsIgnoreCase("1")) {
            FORMULAS.HallarCorrienteEnI(cjcorrientefaseIu, cjcorrientefaseIv, cjcorrientefaseIw, cjcorrientesecundaria, combofasestransformador, cjpromediocorrienteI);
        } else if (combofasestransformador.getSelectedItem().toString().equalsIgnoreCase("3")) {
            FORMULAS.HallarCorrienteEnI(cjcorrientefaseIu, cjcorrientefaseIv, cjcorrientefaseIw, cjcorrientesecundaria, combofasestransformador, cjpromediocorrienteI);
        }
        FORMULAS.getValores(cjpotencia, combofasestransformador, comboservicio, cjfechafabricacion, estado, cjiogarantizado, cjpogarantizado, cjpccgarantizado, cjuzgarantizado, cjtensionprimaria, comborefrigeracion, comboclasedeaislamiento, lblmostrarpcu, cjcliente);
    }

    private void cjcorrientefaseIuKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjcorrientefaseIuKeyTyped
        if (evt.getKeyChar() == 10) {
            FuncionPromedioCorrientes();
            if (combofasestransformador.getSelectedItem().toString().equalsIgnoreCase("1")) {
                cjpomedido.grabFocus();
            } else {
                cjcorrientefaseIv.grabFocus();
            }
        }
    }//GEN-LAST:event_cjcorrientefaseIuKeyTyped

    private void cjvoltajeBTKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjvoltajeBTKeyTyped
        if (evt.getKeyChar() == 10) {
            cjcorrientefaseIu.grabFocus();
        }
    }//GEN-LAST:event_cjvoltajeBTKeyTyped

    private void cjcorrientesecundariadespachoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjcorrientesecundariadespachoKeyTyped
        if (evt.getKeyChar() == 10) {
            cjtemperaturadeprueba.grabFocus();
        }
    }//GEN-LAST:event_cjcorrientesecundariadespachoKeyTyped

    private void cjcorrienteprimariadespachoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjcorrienteprimariadespachoKeyTyped
        if (evt.getKeyChar() == 10) {
            cjcorrientesecundariadespacho.grabFocus();
        }
    }//GEN-LAST:event_cjcorrienteprimariadespachoKeyTyped

    private void cjcorrientesecundariaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjcorrientesecundariaKeyTyped
        if (evt.getKeyChar() == 10) {
            cjcorrienteprimariadespacho.grabFocus();
        }
    }//GEN-LAST:event_cjcorrientesecundariaKeyTyped

    private void cjcorrientesecundariaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cjcorrientesecundariaFocusGained
        CalcularCorrienteSecundaria();
    }//GEN-LAST:event_cjcorrientesecundariaFocusGained

    private void cjcorrienteprimariaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjcorrienteprimariaKeyTyped
        if (evt.getKeyChar() == 10) {
            cjcorrientesecundaria.grabFocus();
        }
    }//GEN-LAST:event_cjcorrienteprimariaKeyTyped

    private void cjcorrienteprimariaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cjcorrienteprimariaFocusGained
        CalcularCorrientePrimaria();
    }//GEN-LAST:event_cjcorrienteprimariaFocusGained

    private void cjderivacionsecundariaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjderivacionsecundariaKeyTyped
        if (evt.getKeyChar() == 10) {
            cjcorrienteprimaria.grabFocus();
        }
    }//GEN-LAST:event_cjderivacionsecundariaKeyTyped
   
    private void cjtensionsecundariaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjtensionsecundariaKeyTyped
        if (evt.getKeyChar() == 10) {
            FuncionVs();
            cjtemperaturadeprueba.grabFocus();
            CargarMasDatos();
        }
    }//GEN-LAST:event_cjtensionsecundariaKeyTyped
    
    private void cjtensionprimariaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjtensionprimariaKeyTyped
        if (evt.getKeyChar() == 10) {
            FuncionVp();
            FORMULAS.getValores(cjpotencia, combofasestransformador, comboservicio, cjfechafabricacion, estado, cjiogarantizado, cjpogarantizado, cjpccgarantizado, cjuzgarantizado, cjtensionprimaria, comborefrigeracion, comboclasedeaislamiento, lblmostrarpcu, cjcliente);
            CargarMasDatos();
        }
    }//GEN-LAST:event_cjtensionprimariaKeyTyped

    private void cjfechafabricacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjfechafabricacionKeyTyped
        if (evt.getKeyChar() == 10) {
//            FORMULAS.getValores(cjpotencia, combofasestransformador, comboservicio, cjfechafabricacion, estado, cjiogarantizado, cjpogarantizado, cjpccgarantizado, cjuzgarantizado, cjtensionprimaria, comborefrigeracion, comboclasedeaislamiento, lblmostrarpcu, cjcliente);
            if (cjfechafabricacion.getText().length() == 4 || cjfechafabricacion.getText().length() == 7 || cjfechafabricacion.getText().length() == 6) {
                cjtensionprimaria.grabFocus();
            }
        }
    }//GEN-LAST:event_cjfechafabricacionKeyTyped

    private void combofasestransformadorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_combofasestransformadorKeyTyped
        if (evt.getKeyChar() == 10) {
            cjfechafabricacion.grabFocus();
        }
    }//GEN-LAST:event_combofasestransformadorKeyTyped

    private void combofasestransformadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combofasestransformadorActionPerformed
        try {
            if (combofasestransformador.getSelectedItem().toString().equalsIgnoreCase("1")) {
                CalcularCorrientePrimaria();
                CalcularCorrienteSecundaria();
                if (!cjtensionprimaria.getText().isEmpty() && HayQueActualizar == false) {
                    CargarTablaUno(Integer.parseInt(cjtensionprimaria.getText()), (int) cjposicionconmutador.getValue(), combofasestransformador);
                }
                if (!cjtensionprimaria.getText().isEmpty()) {
                    HallarPolaridad(combofasestransformador, cjtensionprimaria);
                }
                MostrarNominalTablaDos();
                lblresfasev.setVisible(false);
                cjresistenciafasev.setVisible(false);
                lblresfasew.setVisible(false);
                cjresistenciafasew.setVisible(false);
                /**
                 * *********************************************************
                 */
                lblyz.setVisible(false);
                cjresistenciayz.setVisible(false);
                lblzx.setVisible(false);
                cjresistenciazx.setVisible(false);
                /**
                 * *********************************************************
                 */
                lblIv.setVisible(false);
                lblIw.setVisible(false);
                cjcorrientefaseIv.setVisible(false);
                cjcorrientefaseIw.setVisible(false);
                /**
                 * *********************************************************
                 */
                if (!cjpotencia.getText().isEmpty() && !cjtensionprimaria.getText().isEmpty() && !cjtensionsecundaria.getText().isEmpty()) {
                    FORMULAS.HallarPromedioR1(cjresistenciafaseu, cjresistenciafasev, cjresistenciafasew, combofasestransformador, cjpromedioresistenciaprimaria);
                    FORMULAS.HallarPromedioR1(cjresistenciaxy, cjresistenciayz, cjresistenciazx, combofasestransformador, cjpromedioresistenciasecundaria);
                    FORMULAS.HallarCorrienteEnI(cjcorrientefaseIu, cjcorrientefaseIv, cjcorrientefaseIw, cjcorrientesecundaria, combofasestransformador, cjpromediocorrienteI);
                }
                if (!cjpotencia.getText().isEmpty()) {
                    FORMULAS.getValores(cjpotencia, combofasestransformador, comboservicio, cjfechafabricacion, estado, cjiogarantizado, cjpogarantizado, cjpccgarantizado, cjuzgarantizado, cjtensionprimaria, comborefrigeracion, comboclasedeaislamiento, lblmostrarpcu, cjcliente);
                }
                FORMULAS.HallarI2r(combofasestransformador, cjcorrienteprimaria, cjcorrientesecundaria, cjpromedioresistenciaprimaria, cjpromedioresistenciasecundaria, cji2r);
                CargarMasDatos();
            } else if (combofasestransformador.getSelectedItem().toString().equalsIgnoreCase("3")){
                combopolaridad.setSelectedIndex(2);
                CalcularCorrientePrimaria();
                CalcularCorrienteSecundaria();
                if (!cjtensionprimaria.getText().isEmpty()) {
                    CargarTablaUno(Integer.parseInt(cjtensionprimaria.getText()), (int) cjposicionconmutador.getValue(), combofasestransformador);
                    HallarPolaridad(combofasestransformador, cjtensionprimaria);
                }
                MostrarNominalTablaDos();
                lblresfasev.setVisible(true);
                cjresistenciafasev.setVisible(true);
                lblresfasew.setVisible(true);
                cjresistenciafasew.setVisible(true);
                lblyz.setVisible(true);
                cjresistenciayz.setVisible(true);
                lblzx.setVisible(true);
                cjresistenciazx.setVisible(true);
                lblIv.setVisible(true);
                lblIw.setVisible(true);
                cjcorrientefaseIv.setVisible(true);
                cjcorrientefaseIw.setVisible(true);
                if (!cjpotencia.getText().isEmpty() && !cjtensionprimaria.getText().isEmpty() && !cjtensionsecundaria.getText().isEmpty()) {
                    FORMULAS.HallarPromedioR1(cjresistenciafaseu, cjresistenciafasev, cjresistenciafasew, combofasestransformador, cjpromedioresistenciaprimaria);
                }
                if (!cjpotencia.getText().isEmpty()) {
                    if (!HayQueActualizar) {
                        FORMULAS.getValores(cjpotencia, combofasestransformador, comboservicio, cjfechafabricacion, estado, cjiogarantizado, cjpogarantizado, cjpccgarantizado, cjuzgarantizado, cjtensionprimaria, comborefrigeracion, comboclasedeaislamiento, lblmostrarpcu, cjcliente);
                    }
                }
                FORMULAS.HallarI2r(combofasestransformador, cjcorrienteprimaria, cjcorrientesecundaria, cjpromedioresistenciaprimaria, cjpromedioresistenciasecundaria, cji2r);
                CargarMasDatos();
            }
        } catch (Exception e) {
            M("ERROR, INTENTE NUEVAMENTE.\n" + e, mal);
        }
    }//GEN-LAST:event_combofasestransformadorActionPerformed

    public void FuncionPotencia() {
        CalcularCorrientePrimaria();
        System.out.println("");
        CalcularCorrienteSecundaria();
        FORMULAS.getValores(cjpotencia, combofasestransformador, comboservicio, cjfechafabricacion, estado, cjiogarantizado, cjpogarantizado, cjpccgarantizado, cjuzgarantizado, cjtensionprimaria, comborefrigeracion, comboclasedeaislamiento, lblmostrarpcu, cjcliente);
        HallarEf();
        HallarReg();
//            if (!cjtensionprimaria.getText().isEmpty() && HayQueActualizar == false) {
//                CargarTablaUno(Integer.parseInt(cjtensionprimaria.getText()), (int) cjposicionconmutador.getValue(), combofasestransformador);
//            }            
        try {
            double k = getK(kc, cjtemperaturadeprueba);
            double I2r85 = getI2R85(k, cji2r);
            FORMULAS.HallarPcu85(cjperdidasdecobremedidas, cji2r, k, I2r85, cjpcureferidoa85);
        } catch (NumberFormatException e) {
        }
        HallarI2R85();
        double R = getR(cjperdidasdecobremedidas, cjpotencia);
        double K = getK(kc, cjtemperaturadeprueba);
        double R85 = getR85(R, K);
        if (!cjvcc.getText().isEmpty()) {
            double Z = getZ(cjvcc, cjtensionprimaria);
            double X = getX(Z, R);
            double Z85 = FORMULAS.QuitarDecimales(getZ85(R85, X), 2);
            cjz85.setText(String.valueOf(Z85));
        }
        FORMULAS.HallarI2r(combofasestransformador, cjcorrienteprimaria, cjcorrientesecundaria, cjpromedioresistenciaprimaria, cjpromedioresistenciasecundaria, cji2r);
        HallarEf();
        HallarReg();
    }

    private void cjpotenciaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjpotenciaKeyTyped
        if (evt.getKeyChar() == 10) {
            FuncionPotencia();
            combofasestransformador.grabFocus();
        }
    }//GEN-LAST:event_cjpotenciaKeyTyped

    private void cjserienoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjserienoKeyTyped
        if (evt.getKeyChar() == 10) {
            try {
                if (!cjserieno.getText().isEmpty()){
                    int total = 0;
                    conexion.conectar();
                    rsSerie = conexion.CONSULTAR(" SELECT count(*) FROM datosentrada, cliente, entrada WHERE "
                            + "identrada_datos=identrada AND idcliente=id_cliente AND "
                            + "noplaca='" + cjserieno.getText().trim() + "' ");
                    rsSerie.next();
                    total = rsSerie.getInt("count");
                    rsSerie = conexion.CONSULTAR("select tension, noplaca, noempresa, nombre_cliente, marca, kva, "
                            + "fase, ano, tension, peso, aceite, lote FROM datosentrada, cliente, entrada WHERE "
                            + "identrada_datos=identrada AND idcliente=id_cliente AND "
                            + "noplaca='" + cjserieno.getText().trim() + "'  ORDER BY lote ");
                    if (total == 1){
                        rsSerie.next();
                        cjnoempresa.setText(rsSerie.getString("noempresa"));
                        cjmarca.setText(rsSerie.getString("marca"));
                        cjpotencia.setText(String.valueOf(rsSerie.getString("kva").replace(".", ",")));
                        combofasestransformador.setSelectedIndex((rsSerie.getInt("fase") == 1) ? 0 : 1);
                        cjtensionprimaria.setText("" + rsSerie.getString("tension").split("/")[0]);
                        cjtensionsecundaria.setText("" + rsSerie.getString("tension").split("/")[1]);
                        cjfechafabricacion.setText("" + rsSerie.getInt("ano"));
                        cjmasatotal.setText(rsSerie.getString("peso"));
                        cjvolliquido.setText("" + rsSerie.getString("aceite"));
                        cjnumerodelote.setText(rsSerie.getString("lote"));
                        cjcliente.setText(rsSerie.getString("nombre_cliente"));
                        cjcolor.setText((rsSerie.getString("nombre_cliente").equals("EMPRESAS PUBLICAS DE MEDELLIN S.A E.S.P")) ? "VERDE" : "GRIS");
                        cjnoempresa.grabFocus();
                    } else if (total > 1){
                        DialogoTrafosRepetidos diag = new DialogoTrafosRepetidos(this, rootPaneCheckingEnabled);
                        diag.CargarDatos(rsSerie);
                        diag.jTable1.grabFocus();
                        diag.setVisible(true);
                        cjnoempresa.grabFocus();
                    } else if (total <= 0){
                        cjnoempresa.setText(null);
                        cjpotencia.setText(null);
                        cjtensionprimaria.setText(null);
                        cjtensionsecundaria.setText(null);
                        cjfechafabricacion.setText(null);
                        cjmasatotal.setText(null);
                        cjvolliquido.setText(null);
                        cjnumerodelote.setText(null);
                        cjcliente.setText(null);
                        cjnoempresa.grabFocus();
                    }
                    CargarMasDatos();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, e);
            } catch (Exception e) {
                M("OCURRIO UN ERROR INESPERADO, SI EL PROBLEMA PERSISTE PONGASE EN CONTACTO CON EL ADMINISTRADOR DEL SISTEMA.\n" + e, bien);
                Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }//GEN-LAST:event_cjserienoKeyTyped

    private void comboservicioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboservicioKeyTyped
        if (evt.getKeyChar() == 10) {
            cjserieno.grabFocus();
        }
    }//GEN-LAST:event_comboservicioKeyTyped

    boolean ESTA_ACTUALIZANDO = false;
    private void comboservicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboservicioActionPerformed

        if (comboservicio.getSelectedItem().toString().equalsIgnoreCase("NUEVO") || comboservicio.getSelectedItem().toString().equalsIgnoreCase("RECONSTRUIDO")) {
            cjmarca.setText("CDM");
            cjfechafabricacion.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("MM/yyyy"))));
            cjfechafabricacion.setText(mesactual + "/" + anoactual);
            try {
                if (!cjpotencia.getText().isEmpty()) {
                    FORMULAS.getValores(cjpotencia, combofasestransformador, comboservicio, cjfechafabricacion, estado, cjiogarantizado, cjpogarantizado, cjpccgarantizado, cjuzgarantizado, cjtensionprimaria, comborefrigeracion, comboclasedeaislamiento, lblmostrarpcu, cjcliente);
                }
            } catch (NullPointerException e) {
            }
        }else if (comboservicio.getSelectedItem().toString().equals("MANTENIMIENTO")){
            if (ESTA_ACTUALIZANDO && HayQueActualizar) {
                ESTA_ACTUALIZANDO = false;
            } else if ((!ESTA_ACTUALIZANDO && HayQueActualizar) || (!ESTA_ACTUALIZANDO && !HayQueActualizar)) {
                try {
                    while (true) {
                        String estado2 = JOptionPane.showInputDialog("Ingrese el numero correspondiente al estado del transformador\nORIGINAL - Digite (1)\nREPARADO - Digite (2)-");
                        if (estado2 != null) {
                            if (estado2.equalsIgnoreCase("1")) {
                                estado = "ORIGINAL";
                                break;
                            } else if (estado2.equalsIgnoreCase("2")) {
                                estado = "REPARADO";
                                break;
                            }
                        } else if (estado2 == null) {
                            break;
                        }
                    }
                } catch (Exception e) {
                }
            }
            cjfechafabricacion.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("yyyy"))));
            cjfechafabricacion.setText("");
            try {
                FORMULAS.getValores(cjpotencia, combofasestransformador, comboservicio, cjfechafabricacion, estado, cjiogarantizado, cjpogarantizado, cjpccgarantizado, cjuzgarantizado, cjtensionprimaria, comborefrigeracion, comboclasedeaislamiento, lblmostrarpcu, cjcliente);
            } catch (Exception e) {
            }
        } else {
            cjmarca.setText("");
            cjfechafabricacion.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("yyyy"))));
            cjfechafabricacion.setText("");
            if (!cjpotencia.getText().isEmpty()) {
                FORMULAS.getValores(cjpotencia, combofasestransformador, comboservicio, cjfechafabricacion, estado, cjiogarantizado, cjpogarantizado, cjpccgarantizado, cjuzgarantizado, cjtensionprimaria, comborefrigeracion, comboclasedeaislamiento, lblmostrarpcu, cjcliente);
            }
        }
    }//GEN-LAST:event_comboservicioActionPerformed

    private void cjnoprotocoloKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjnoprotocoloKeyTyped
        if (evt.getKeyChar() == 10) {
            cjcliente.grabFocus();
        }
    }//GEN-LAST:event_cjnoprotocoloKeyTyped

    private void tablaunoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaunoKeyTyped
        if (evt.getKeyChar() == 10) {
            work = true;
            if (combofasestransformador.getSelectedItem().toString().equalsIgnoreCase("1")) {
                if (tablauno.getSelectedRow() == 0) {
                    if (tablauno.getSelectedColumn() == 3) {
                        cjresistenciafaseu.grabFocus();
                        cjresistenciafaseu.setBorder(new LineBorder(Color.red, 2));
                    }
                }
            } else if (combofasestransformador.getSelectedItem().toString().equalsIgnoreCase("3")) {
                if (tablauno.getSelectedRow() == 0) {
                    if (tablauno.getSelectedColumn() == 0) {
                        cjresistenciafaseu.grabFocus();
                        cjresistenciafaseu.setBorder(new LineBorder(Color.red, 2));
                    }
                }
            }
        }
    }//GEN-LAST:event_tablaunoKeyTyped

    public void FuncionPromedioResistenciasPrimaria() {
        FORMULAS.HallarPromedioR1(cjresistenciafaseu, cjresistenciafasev, cjresistenciafasew, combofasestransformador, cjpromedioresistenciaprimaria);
    }

    private void cjresistenciafaseuKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjresistenciafaseuKeyTyped
        if (evt.getKeyChar() == 10) {
            FuncionPromedioResistenciasPrimaria();
            if (combofasestransformador.getSelectedItem().toString().equalsIgnoreCase("1")) {
                cjresistenciaxy.grabFocus();
            } else {
                cjresistenciafasev.grabFocus();
            }
        }
    }//GEN-LAST:event_cjresistenciafaseuKeyTyped

    private void cjresistenciafasevKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjresistenciafasevKeyTyped
        if (evt.getKeyChar() == 10) {
            FuncionPromedioResistenciasPrimaria();
            cjresistenciafasew.grabFocus();
        }
    }//GEN-LAST:event_cjresistenciafasevKeyTyped

    private void combomaterialconductorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_combomaterialconductorKeyTyped
        if (evt.getKeyChar() == 10) {
            combomaterialconductordos.grabFocus();
//            cjcorrientefaseIu.grabFocus();
        }
    }//GEN-LAST:event_combomaterialconductorKeyTyped

    private void cjtemperaturadepruebaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cjtemperaturadepruebaFocusGained
        this.cjtemperaturadeprueba.setBorder(new LineBorder(Color.red, 2));
    }//GEN-LAST:event_cjtemperaturadepruebaFocusGained

    private void cjtemperaturadepruebaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cjtemperaturadepruebaFocusLost
        this.cjtemperaturadeprueba.setBorder(new LineBorder(Color.gray, 1));
    }//GEN-LAST:event_cjtemperaturadepruebaFocusLost

    private void cjresistenciafaseuFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cjresistenciafaseuFocusLost
        cjresistenciafaseu.setBorder(new LineBorder(Color.gray, 1));
    }//GEN-LAST:event_cjresistenciafaseuFocusLost

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        if (JOptionPane.showConfirmDialog(this, "SEGURO QUE DESEA SALIR DEL SISTEMA?") == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        if (JOptionPane.showConfirmDialog(drmt, "Seguro que desea crear un nuevo Protocolo ? ", "Confirmar nuevo protocolo!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Limpiar();
            cjcliente.grabFocus();
            HayQueActualizar = false;
            ESTA_ACTUALIZANDO = false;
            Cargar("");
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {//C:\Users\Public\REPORTES
            Desktop.getDesktop().open(new File(RutaPDF()));
        } catch (IOException e) {
            M("NO SE ENCONTRO LA RUTA DE LA CARPETA\n" + e, mal);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cjbuscarpdfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjbuscarpdfKeyTyped
        jList1.setModel(buscarPDF(cjbuscarpdf.getText()));
    }//GEN-LAST:event_cjbuscarpdfKeyTyped

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        CONTENEDOR.setSelectedComponent(LISTAPROTOCOLOS);
        cjbuscarplaca.grabFocus();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        CONTENEDOR.setSelectedComponent(PDFPROTOCOLOS);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void cjbuscarpdfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjbuscarpdfKeyPressed
        if (evt.getKeyCode() == 40) {
            jList1.grabFocus();
            jList1.setSelectedIndex(0);
        }
    }//GEN-LAST:event_cjbuscarpdfKeyPressed

    private void jList1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList1KeyTyped
        if (evt.getKeyChar() == 10) {
            int posicion = jList1.getSelectedIndex();
            if (posicion >= 0) {
                try {
                    File path = new File(RutaPDF() + jList1.getModel().getElementAt(posicion));
                    Desktop.getDesktop().open(path);
                } catch (IOException | IllegalArgumentException ex) {
                    M("ERROR AL ABRIR EL ARCHIVO", mal);
                }
            }
        }
    }//GEN-LAST:event_jList1KeyTyped

    private void jList1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList1KeyPressed
        if (evt.getKeyChar() == 38) {
            if (jList1.getSelectedIndex() == 0) {
                cjbuscarpdf.grabFocus();
            }
        }
    }//GEN-LAST:event_jList1KeyPressed

    private void VerProtocoloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VerProtocoloActionPerformed
        Actualizar();
    }//GEN-LAST:event_VerProtocoloActionPerformed

    private void EliminarProtocoloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarProtocoloActionPerformed
        try {
            if (datos.getSelectedRow() != -1) {
                String no[] = datos.getValueAt(datos.getSelectedRow(), 0).toString().split("-");
                int id = Integer.parseInt(no[1]);
                if (JOptionPane.showConfirmDialog(null, "Seguro que desea eliminar el protocolo de la base de datos ? ", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    String sql = "DELETE FROM protocolo WHERE id_protocolo=" + id + " ";
                    if (conexion.GUARDAR(sql)) {
                        M("Protocolo eliminado con éxito.", bien);
                        Cargar("");
                    }
                }
            } else {
                M("SELECCIONE EL PROTOCOLO QUE DESEA ELIMINAR", mal);
            }
        } catch (HeadlessException | NumberFormatException e) {
            M("HA OCURRIDO UN ERROR AL ELIMINAR EL PROTOCOLO\nES IMPOSIBLE ELIMINAR", mal);
        }
    }//GEN-LAST:event_EliminarProtocoloActionPerformed

    private void cjperdidasdecobremedidasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cjperdidasdecobremedidasFocusLost
        cjperdidasdecobremedidas.setBorder(new LineBorder(Color.gray, 1));
        cji2r.setBorder(new LineBorder(Color.gray, 1));
    }//GEN-LAST:event_cjperdidasdecobremedidasFocusLost

    private void combomaterialconductordosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combomaterialconductordosActionPerformed
        if (combomaterialconductor.getSelectedItem().toString().equalsIgnoreCase("COBRE")
                && combomaterialconductordos.getSelectedItem().equals("COBRE")) {
            kc = 234.5;
        } else if (combomaterialconductor.getSelectedItem().toString().equalsIgnoreCase("ALUMINIO")
                && combomaterialconductordos.getSelectedItem().equals("ALUMINIO")) {
            kc = 225;
        } else {
            kc = 229;
        }
    }//GEN-LAST:event_combomaterialconductordosActionPerformed

    private void combomaterialconductordosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_combomaterialconductordosKeyTyped
        if (evt.getKeyChar() == 10) {
            cjcorrientefaseIu.grabFocus();
        }
    }//GEN-LAST:event_combomaterialconductordosKeyTyped

    private void datosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datosMouseClicked

//        Object[] obj = new Object[datos.getColumnCount()];
//        for(int i=0; i<datos.getColumnCount(); i++){
//            obj[i] = datos.getValueAt(0, i);
//        }   
//        DefaultTableModel mod = (DefaultTableModel)datos.getModel();
//        for(int i=0; i<100; i++){
//            mod.addRow(obj);
//        }
        if (evt.getClickCount() == 2) {
            Actualizar();
        }
    }//GEN-LAST:event_datosMouseClicked

    private void cjobservacionesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjobservacionesKeyTyped
        if (evt.getKeyChar() == 10) {
            cjfechadesalida.grabFocus();
        }
    }//GEN-LAST:event_cjobservacionesKeyTyped

    private void SubMenuAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubMenuAbrirActionPerformed
        if (jList1.getSelectedIndex() >= 0) {
            int posicion = jList1.getSelectedIndex();
            if (posicion >= 0) {
                try {
                    File path = new File(RutaPDF() + jList1.getModel().getElementAt(posicion));
                    Desktop.getDesktop().open(path);
                } catch (IOException | IllegalArgumentException ex) {
                    M("ERROR AL ABRIR EL ARCHIVO", mal);
                }
            }
        } else {
            M("SELECCIONE UN PROTOCOLO", mal);
        }
    }//GEN-LAST:event_SubMenuAbrirActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        if (jList1.getSelectedIndex() >= 0) {
            jList1.setComponentPopupMenu(SubMenuJList);
            SubMenuAbrir.setText("Abrir Protocolo " + jList1.getSelectedValue());
        }
    }//GEN-LAST:event_jList1MouseClicked

    private void cjresistenciayzKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjresistenciayzKeyReleased
        FuncionPromedioResistenciaSecundaria();
    }//GEN-LAST:event_cjresistenciayzKeyReleased

    private void cjresistenciazxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjresistenciazxKeyReleased
        FuncionPromedioResistenciaSecundaria();
    }//GEN-LAST:event_cjresistenciazxKeyReleased

    public static void createCell(Row row, int i, String value, CellStyle style) {
        Cell cell = row.createCell(i);
        value = value + " ";
        cell.setCellValue(value);
        // si no hay estilo, no se aplica
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    public void EstyloColumna(XSSFCell cell, XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        Font font = wb.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        cell.setCellStyle(style);
    }

    public void EstyloColumnaDetallada(HSSFCell cell, HSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        Font font = wb.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        cell.setCellStyle(style);
    }

    public void EstyloFilas(HSSFCell cell, HSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        cell.setCellStyle(style);
    }

    private void SubMenuExcelControlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubMenuExcelControlActionPerformed
        modelo.Metodos.JTableToExcel(datos, jButton1);
//        try {
//            Runnable miRunnable = new Runnable() {
//                @Override
//                public void run() {
//                    GeneraExcel ge = new GeneraExcel();
//                    ge.exportTable(datos);
////                    try {
////                        //FileInputStream file = new FileInputStream(new File(ruta + "//REPORTES EXCEL//PLANTILLAS//CONTROL DE PROTOCOLOS.xlsx"));
////                        FileInputStream file = new FileInputStream(new File(Metodos.RutaPlantillasExcel()+"CONTROL DE PROTOCOLOS.xlsx"));
////                        XSSFWorkbook workbook = new XSSFWorkbook(file);
////                        XSSFSheet hoja = workbook.getSheetAt(0);
////                        try {
////                            for (int j = 0; j <= hoja.getLastRowNum(); j++) {
////                                if (j >= 1) {
////                                    Row row = hoja.getRow(j);
////                                    hoja.removeRow(row);
////                                }
////                            }
////                        } catch (java.lang.NullPointerException e) {
////                        }
////                        barra.setMaximum(datos.getRowCount());
////                        for (int i = 0; i < datos.getRowCount(); i++) {
////                            XSSFRow filadatos = hoja.createRow(i + 1);
////                            for (int j = 0; j < datos.getColumnCount(); j++) {
////                                /**
////                                 * ********************************
////                                 */
////                                barra.setValue(i + 1);
////                                barra.setString("Copiando " + datos.getValueAt(i, j).toString());
////                                if (i == datos.getRowCount() - 1) {
////                                    barra.setString("GENERANDO EXCEL...");
////                                }
////                                /**
////                                 * ********************************
////                                 */
////                                XSSFCell cell = (XSSFCell) filadatos.createCell(j);
////                                try {
////                                    switch (j) {
////                                        case 0:
////                                            cell.setCellValue(String.valueOf(datos.getValueAt(i, j)));
////                                            break;
////                                        case 1:
////                                            cell.setCellValue(String.valueOf(datos.getValueAt(i, j)));
////                                            break;
////                                        case 2:
////                                            cell.setCellValue(String.valueOf(datos.getValueAt(i, j)));
////                                            break;
////                                        case 3:
////                                            cell.setCellValue((String.valueOf(datos.getValueAt(i, j))));
////                                            break;
////                                        case 4:
////                                            cell.setCellValue(String.valueOf(datos.getValueAt(i, j)));
////                                            break;
////                                        case 5:
////                                            cell.setCellValue(nf.parse(String.valueOf(datos.getValueAt(i, j))).doubleValue());
////                                            break;
////                                        case 6:
////                                            cell.setCellValue(Integer.parseInt(String.valueOf(datos.getValueAt(i, j))));
////                                            break;
////                                        case 7:
////                                            cell.setCellValue(Integer.parseInt(String.valueOf(datos.getValueAt(i, j))));
////                                            break;
////                                        case 8:
////                                            cell.setCellValue(Integer.parseInt(String.valueOf(datos.getValueAt(i, j))));
////                                            break;
////                                        case 9:
////                                            cell.setCellValue(String.valueOf(datos.getValueAt(i, j)));
////                                            break;
////                                        case 10:
////                                            cell.setCellValue((datos.getValueAt(i, 10).toString()));
////                                            break;
////                                    }
////                                } catch (ParseException | NumberFormatException e) {
////                                    M("ERROR AL OBTENER LOS VALORES DESEDE LA TABLA EN LA FILA " + i + " Y LA COLUMNA " + j + "\n" + e, mal);
////                                }
////                            }
////                        }
////                        for (int i = 0; i < datos.getColumnCount(); i++) {
////                            hoja.autoSizeColumn((short) i);
////                        }
////                        try {
////                            try {
////                                try {
////                                    workbook.write(new FileOutputStream(new File(Metodos.RutaExcelGenerados()+"CONTROL DE PROTOCOLOS.xlsx")));
////                                    file.close();
////                                    barra.setValue(0);
////                                    barra.setString("CDM SOFTWARE");
////                                    Desktop.getDesktop().open(new File(Metodos.RutaExcelGenerados()+"CONTROL DE PROTOCOLOS.xlsx"));
////                                } catch (FileNotFoundException ex) {
////                                    M("ERROR EN LA RUTA DEL ARCHIVO\nES PROBABLE QUE EL ARCHIVO EXCEL QUE ESTAS GENERANDO SE ENCUENTRA ABIERTO POR OTRO USUARIO, DEBES CERRARLO\n" + ex, mal);
////                                }
////                            } catch (IOException e) {
////                                M("ERROR AL CREAR Y ESCRIBIR EL ARCHIVO\n" + e, mal);
////                            }
////                        } catch (Exception e) {
////                            M("HA OCURRIDO UN ERROR INESPERADO VUELVE A INTENTARLO\n" + e, mal);
////                        }
////                    } catch (IOException ex) {
////                        Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
////                        M("ERROR AL ABRIR EL EXCEL PARA GENERARLO", mal);
////                    }
//                }
//            };
//            Thread hilo1 = new Thread(miRunnable);
//            hilo1.start();
//        } catch (Exception e) {
//            M("HA OCURRIDO UN ERROR INESPERADO GENERANDO EL EXCEL CON LOS PROTOCOLOS Y TODAS SUS CARACTERISTICAS\nNO SE GENERO CORRECTAMENTE", mal);
//        }
    }//GEN-LAST:event_SubMenuExcelControlActionPerformed

    //ASIGNA ESTYLO A CELDAS DEL EXCEL COMPLETO
    public XSSFCell CCC(XSSFRow rowcol, int pos, String valor, int fila, XSSFWorkbook workbook) {
        XSSFCellStyle estiloFilaPar = workbook.createCellStyle();
        XSSFColor colorFilaPar = new XSSFColor(new Color(217, 217, 217));
        estiloFilaPar.setFillForegroundColor(colorFilaPar);
        estiloFilaPar.setFillPattern(CellStyle.SOLID_FOREGROUND);
        XSSFCell cell = rowcol.createCell(pos);
        cell.setCellValue(valor);
        if (fila % 2 == 0) {
            cell.setCellStyle(estiloFilaPar);
        }
        return cell;
    }

    public int I(String n) throws NumberFormatException, NullPointerException {
        int x = 0;
        try {
            if (n != null) {
                if (!n.isEmpty()) {
                    x = Integer.parseInt(n);
                }
            }
        } catch (NumberFormatException | NullPointerException e) {
        }
        return x;
    }

    public double D(String n) throws NumberFormatException, NullPointerException {
        double d = 0;
        try {
            if (n == null || n.isEmpty()) {
                return d;
            } else {
                d = Double.parseDouble(n);
            }
        } catch (NumberFormatException | NullPointerException e) {
        }
        return d;
    }

    public double Double(String n) throws NumberFormatException, NullPointerException {
        double d = 0;
        try {
            if (n != null) {
                if (!n.isEmpty()) {
                    d = Double.parseDouble(n.replace(",", "."));
                }
            }
        } catch (NumberFormatException | NullPointerException e) {
        }
        return d;
    }


    private void SubMenuExcelCompletoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubMenuExcelCompletoActionPerformed
        try {
            Runnable miRunnable;
            miRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        int id = 0;
                        String idprotocolo[];
                        String sql;
                        ResultSet rs;
                        //FileInputStream file = new FileInputStream(new File(ruta + "//REPORTES EXCEL//PLANTILLAS//CARACTERISTICAS DE PROTOCOLOS.xlsx"));
                        FileInputStream file = new FileInputStream(new File(RutaPlantillasExcel() + "CARACTERISTICAS DE PROTOCOLOS.xlsx"));
                        XSSFWorkbook workbook = new XSSFWorkbook(file);
                        XSSFSheet sheet = workbook.getSheetAt(0);
                        try {
                            for (int j = 0; j <= sheet.getLastRowNum(); j++) {
                                if (j >= 4) {
                                    Row row = sheet.getRow(j);
                                    sheet.removeRow(row);
                                }
                            }
                        } catch (java.lang.NullPointerException e) {}
                        conexion.conectar();
                        rs = conexion.CONSULTAR("SELECT * FROM\n"
                                + "  public.entrada, public.cliente, public.protocolo, public.datosentrada, public.marcatransformador \n"
                                + "  WHERE \n"
                                + "  entrada.idcliente = cliente.id_cliente AND \n"
                                + "  cliente.id_cliente='" + getIdCliente(cjbuscarporcliente.getText()) + "' AND\n"
                                + "  protocolo.cliente = cliente.nombre_cliente AND\n"
                                + "  marcatransformador.id_marcatransformador = protocolo.idmarca AND"
                                + "  datosentrada.identrada_datos = entrada.identrada AND\n"
                                + "  datosentrada.noplaca = protocolo.serieno \n"
                                + "  " + ((cjFechaDesde.getDate() != null && cjFechaHasta.getDate() != null) ? " AND entrada.fechaderecepcion BETWEEN '" + cjFechaDesde.getDate() + "' AND '" + cjFechaHasta.getDate() + "' " : " ") + " "
                                + "   ORDER BY id_protocolo ASC ");
                        int i = 0;
                        while (rs.next()) {
                            XSSFRow row = sheet.createRow((4 + i));
                            barra.setValue(i + 1);
                            row.createCell(0).setCellValue("A-" + I(rs.getString("id_protocolo")));
                            row.createCell(1).setCellValue((rs.getString("serieno")));
                            row.createCell(2).setCellValue(rs.getString("marca_marcatransformador"));
                            row.createCell(3).setCellValue(Double(rs.getString("potencia").replace(".", ",")));
                            row.createCell(4).setCellValue(I(rs.getString("fase")));
                            row.createCell(5).setCellValue(rs.getString("fechafabricacion"));
                            row.createCell(6).setCellValue(I(rs.getString("tensionprimaria")));
                            row.createCell(7).setCellValue(I(rs.getString("tensionsecundaria")));
                            row.createCell(8).setCellValue(D(rs.getString("corrienteprimaria")));
                            row.createCell(9).setCellValue(D(rs.getString("corrientesecundaria")));
                            row.createCell(10).setCellValue(D(rs.getString("proresuno")));
                            row.createCell(11).setCellValue(D(rs.getString("proresdos")));
                            row.createCell(12).setCellValue(D(rs.getString("pomedido")));
                            row.createCell(13).setCellValue(D(rs.getString("iu")));
                            row.createCell(14).setCellValue(D(rs.getString("iv")));
                            row.createCell(15).setCellValue(D(rs.getString("iw")));
                            row.createCell(16).setCellValue(D(rs.getString("promediocorriente")));
                            row.createCell(17).setCellValue(D(rs.getString("iogarantizado")));
                            row.createCell(18).setCellValue(D(rs.getString("perdidasdecobre")));
                            row.createCell(19).setCellValue(D(rs.getString("vcc")));
                            row.createCell(20).setCellValue(D(rs.getString("temperaturadeensayo")));
                            row.createCell(21).setCellValue(D(rs.getString("i2r")));
                            row.createCell(22).setCellValue(D(rs.getString("i2r85")));
                            row.createCell(23).setCellValue(D(rs.getString("pcua85")));
                            row.createCell(24).setCellValue(D(rs.getString("impedancia85")));
                            row.createCell(25).setCellValue(D(rs.getString("impedanciagarantizado")));
                            row.createCell(26).setCellValue(D(rs.getString("reg")));
                            row.createCell(27).setCellValue(I(rs.getString("atcontrabt")));
                            row.createCell(28).setCellValue(I(rs.getString("atcontratierra")));
                            row.createCell(29).setCellValue(I(rs.getString("btcontratierra")));
                            row.createCell(30).setCellValue((rs.getString("grupodeconexion")));
                            row.createCell(31).setCellValue(D(rs.getString("punou")));
                            row.createCell(32).setCellValue(D(rs.getString("pdosu")));
                            row.createCell(33).setCellValue(D(rs.getString("ptresu")));
                            row.createCell(34).setCellValue(D(rs.getString("pcuatrou")));
                            row.createCell(35).setCellValue(D(rs.getString("pcincou")));
                            row.createCell(36).setCellValue(D(rs.getString("punov")));
                            row.createCell(37).setCellValue(D(rs.getString("pdosv")));
                            row.createCell(38).setCellValue(D(rs.getString("ptresv")));
                            row.createCell(39).setCellValue(D(rs.getString("pcuatrov")));
                            row.createCell(40).setCellValue(D(rs.getString("pcincov")));
                            row.createCell(41).setCellValue(D(rs.getString("punow")));
                            row.createCell(42).setCellValue(D(rs.getString("pdosw")));
                            row.createCell(43).setCellValue(D(rs.getString("ptresw")));
                            row.createCell(44).setCellValue(D(rs.getString("pcuatrow")));
                            row.createCell(45).setCellValue(D(rs.getString("pcincow")));
                            row.createCell(46).setCellValue(D(rs.getString("anchotanque")));
                            row.createCell(47).setCellValue(D(rs.getString("largotanque")));
                            row.createCell(48).setCellValue(D(rs.getString("altotanque")));
                            row.createCell(49).setCellValue((rs.getString("servicio")));
                            row.createCell(50).setCellValue(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("fechaexpedicion")));
                            row.createCell(51).setCellValue("");
                            row.createCell(52).setCellValue((rs.getString("liquidoaislante")));
                            row.createCell(53).setCellValue((rs.getString("volumendeliquido")));
                            row.createCell(54).setCellValue((rs.getString("color")));
                            row.createCell(55).setCellValue((rs.getString("masatotal")));
                            row.createCell(56).setCellValue((rs.getString("lote")));
                            row.createCell(57).setCellValue((rs.getString("op")));
                            row.createCell(58).setCellValue((rs.getString("noempresa")));
                            i++;
                        }
//                        barra.setMaximum(datos.getRowCount());
//                        for (int i = 0; i < datos.getRowCount(); i++) {
//                            barra.setValue(i + 1);
//                            try {
//                                idprotocolo = datos.getValueAt(i, 0).toString().split("-");
//                                id = Integer.parseInt(idprotocolo[1]);
//                            } catch (java.lang.NumberFormatException t) {
//                            }
//                            sql = "SELECT * FROM protocolo p INNER JOIN marcatransformador m ON p.idmarca=m.id_marcatransformador ";
//                            sql += "WHERE id_protocolo=" + id + " ORDER BY id_protocolo ASC";
//                            rs = Metodos.BuscarResult(sql);
//                            try {
//                                if (rs.next()) {
//                                    barra.setString("Copiando datos del cliente " + rs.getString("cliente") + " ...");
//                                    XSSFRow row = sheet.createRow((4 + i));
//                                    row.createCell(0).setCellValue(datos.getValueAt(i, 0).toString());
//                                    row.createCell(1).setCellValue((rs.getString("serieno")));
//                                    row.createCell(2).setCellValue(rs.getString("marca_marcatransformador"));
//                                    row.createCell(3).setCellValue(Double(rs.getString("potencia").replace(".", ",")));
//                                    row.createCell(4).setCellValue(I(rs.getString("fase")));
//                                    row.createCell(5).setCellValue(rs.getString("fechafabricacion"));
//                                    row.createCell(6).setCellValue(I(rs.getString("tensionprimaria")));
//                                    row.createCell(7).setCellValue(I(rs.getString("tensionsecundaria")));
//                                    row.createCell(8).setCellValue(D(rs.getString("corrienteprimaria")));
//                                    row.createCell(9).setCellValue(D(rs.getString("corrientesecundaria")));
//                                    row.createCell(10).setCellValue(D(rs.getString("proresuno")));
//                                    row.createCell(11).setCellValue(D(rs.getString("proresdos")));
//                                    row.createCell(12).setCellValue(D(rs.getString("pomedido")));
//                                    row.createCell(13).setCellValue(D(rs.getString("iu")));
//                                    row.createCell(14).setCellValue(D(rs.getString("iv")));
//                                    row.createCell(15).setCellValue(D(rs.getString("iw")));
//                                    row.createCell(16).setCellValue(D(rs.getString("promediocorriente")));
//                                    row.createCell(17).setCellValue(D(rs.getString("iogarantizado")));
//                                    row.createCell(18).setCellValue(D(rs.getString("perdidasdecobre")));
//                                    row.createCell(19).setCellValue(D(rs.getString("vcc")));
//                                    row.createCell(20).setCellValue(D(rs.getString("temperaturadeensayo")));
//                                    row.createCell(21).setCellValue(D(rs.getString("i2r")));
//                                    row.createCell(22).setCellValue(D(rs.getString("i2r85")));
//                                    row.createCell(23).setCellValue(D(rs.getString("pcua85")));
//                                    row.createCell(24).setCellValue(D(rs.getString("impedancia85")));
//                                    row.createCell(25).setCellValue(D(rs.getString("impedanciagarantizado")));
//                                    row.createCell(26).setCellValue(D(rs.getString("reg")));
//                                    row.createCell(27).setCellValue(I(rs.getString("atcontrabt")));
//                                    row.createCell(28).setCellValue(I(rs.getString("atcontratierra")));
//                                    row.createCell(29).setCellValue(I(rs.getString("btcontratierra")));
//                                    row.createCell(30).setCellValue((rs.getString("grupodeconexion")));
//                                    row.createCell(31).setCellValue(D(rs.getString("punou")));
//                                    row.createCell(32).setCellValue(D(rs.getString("pdosu")));
//                                    row.createCell(33).setCellValue(D(rs.getString("ptresu")));
//                                    row.createCell(34).setCellValue(D(rs.getString("pcuatrou")));
//                                    row.createCell(35).setCellValue(D(rs.getString("pcincou")));
//                                    row.createCell(36).setCellValue(D(rs.getString("punov")));
//                                    row.createCell(37).setCellValue(D(rs.getString("pdosv")));
//                                    row.createCell(38).setCellValue(D(rs.getString("ptresv")));
//                                    row.createCell(39).setCellValue(D(rs.getString("pcuatrov")));
//                                    row.createCell(40).setCellValue(D(rs.getString("pcincov")));
//                                    row.createCell(41).setCellValue(D(rs.getString("punow")));
//                                    row.createCell(42).setCellValue(D(rs.getString("pdosw")));
//                                    row.createCell(43).setCellValue(D(rs.getString("ptresw")));
//                                    row.createCell(44).setCellValue(D(rs.getString("pcuatrow")));
//                                    row.createCell(45).setCellValue(D(rs.getString("pcincow")));
//                                    row.createCell(46).setCellValue(D(rs.getString("anchotanque")));
//                                    row.createCell(47).setCellValue(D(rs.getString("largotanque")));
//                                    row.createCell(48).setCellValue(D(rs.getString("altotanque")));
//                                    row.createCell(49).setCellValue((rs.getString("servicio")));
//                                    row.createCell(50).setCellValue(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("fechaexpedicion")));
//                                    row.createCell(51).setCellValue("");
//                                    row.createCell(52).setCellValue((rs.getString("liquidoaislante")));
//                                    row.createCell(53).setCellValue((rs.getString("volumendeliquido")));
//                                    row.createCell(54).setCellValue((rs.getString("color")));
//                                    row.createCell(55).setCellValue((rs.getString("masatotal")));
//                                }
//                            } catch (SQLException ex) {
//                                Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
//                                M("ERROR AL GENERAR EL EXCEL DESDE LA BASE DE DATOS", mal);
//                            }
//                            if (i == datos.getRowCount() - 1) {
//                                barra.setString("GENERANDO EXCEL...");
//                            }
//                        }
                        workbook.write(new FileOutputStream(new File(RutaExcelGenerados() + "CARACTERISTICAS DE PROTOCOLOS.xlsx")));
                        file.close();
                        barra.setValue(0);
                        barra.setString("CDM SOFTWARE");
                        Desktop.getDesktop().open(new File(RutaExcelGenerados() + "CARACTERISTICAS DE PROTOCOLOS.xlsx"));
                    } catch (Exception e) {
                        M("ERROR AL ABRIR EL ARCHIVO\n" + e, mal);
                    }finally{
                        conexion.CERRAR();
                    }                    
                }
            };
            Thread hilo1 = new Thread(miRunnable);
            hilo1.start();
        } catch (Exception e) {
            M("HA OCURRIDO UN ERROR INESPERADO GENERANDO EL EXCEL CON LOS PROTOCOLOS Y TODAS SUS CARACTERISTICAS\nNO SE GENERO CORRECTAMENTE", mal);
        }
    }//GEN-LAST:event_SubMenuExcelCompletoActionPerformed

    private void cjclienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjclienteKeyTyped
        if (evt.getKeyChar() == 10) {
            comboservicio.grabFocus();
        }
    }//GEN-LAST:event_cjclienteKeyTyped

    private void SubMenuNuevoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubMenuNuevoClienteActionPerformed
        try {
            if (drc == null) {
                drc = new Dialogos.DialogoRegistrarCliente(this, true);
            }
            drc.setVisible(true);
            cjcliente.grabFocus();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_SubMenuNuevoClienteActionPerformed

    private void btnbuscarpdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarpdfActionPerformed
        jList1.setModel(buscarPDF(cjbuscarpdf.getText()));
    }//GEN-LAST:event_btnbuscarpdfActionPerformed
    Principal principal;
    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        dispose();
        if (principal == null) {
            principal = new Principal();
        }
        principal.setVisible(true);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    public void EX(XSSFSheet s, int r, int c, String v) {
        s.getRow(r).getCell(c).setCellValue(v);
    }

    private void submenuProtocoloExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submenuProtocoloExcelActionPerformed

        Runnable mirun = new Runnable() {
            @Override
            public void run() {

                FileInputStream file = null;
                try {
                    //file = new FileInputStream(new File(ruta + "//REPORTES EXCEL//PLANTILLAS//PROTOCOLO.xlsx"));
                    file = new FileInputStream(new File(RutaPlantillasExcel() + "PROTOCOLO.xlsx"));
                    XSSFWorkbook workbook = new XSSFWorkbook(file);
                    XSSFSheet s = workbook.getSheetAt(0);

                    EX(s, 3, 0, cjnoprotocolo.getText());
                    EX(s, 3, 2, cjcliente.getText());
                    EX(s, 3, 4, comboservicio.getSelectedItem().toString());
                    EX(s, 2, 7, cjnoempresa.getText());
                    EX(s, 3, 7, cjserieno.getText());
                    EX(s, 4, 7, cjmarca.getText());
                    EX(s, 6, 1, cjpotencia.getText());
                    EX(s, 6, 2, "Frecuencia: " + cjfrecuencia1.getValue());
                    EX(s, 6, 3, "Ten. Serie: " + cjtensionserieuno.getText() + "/" + cjtensionseriedos.getText() + " Kv");
                    EX(s, 6, 4, "Calentamiento Devanado: " + cjcalentamientodevanado.getText());
                    EX(s, 6, 7, cjalturadiseno.getText());
                    EX(s, 7, 1, combofasestransformador.getSelectedItem().toString());
                    EX(s, 7, 2, "Refrigeracion: " + comborefrigeracion.getSelectedItem().toString());
                    EX(s, 7, 3, "NBA AT/BT: " + cjnivelbasicodeaislamientouno.getText() + "/" + cjnivelbasicodeaislamientodos.getText() + " Kv");
                    EX(s, 7, 4, "Clase Aislamiento: " + comboclasedeaislamiento.getSelectedItem().toString());
                    EX(s, 7, 7, cjfechafabricacion.getText());
                    EX(s, 8, 3, cjtensionprimaria.getText());
                    EX(s, 9, 3, cjtensionsecundaria.getText());
                    EX(s, 8, 4, "Deriv: " + comboderivacionprimaria.getSelectedItem().toString());
                    EX(s, 9, 4, "Deriv: " + cjderivacionsecundaria.getText());
                    EX(s, 8, 6, cjcorrienteprimaria.getText());
                    EX(s, 9, 6, cjcorrientesecundaria.getText());
                    EX(s, 8, 8, cjcorrienteprimariadespacho.getText());
                    EX(s, 9, 8, cjcorrientesecundariadespacho.getText());
                    EX(s, 11, 3, cjtemperaturadeprueba.getText() + " °C");
                    EX(s, 11, 7, "" + cjposicionconmutador.getValue());
                    EX(s, 13, 2, comboliquidoaislante.getSelectedItem().toString());
                    EX(s, 13, 3, "Referencia: " + comboreferenciaaceite.getSelectedItem().toString());
                    EX(s, 13, 5, "Tensión de Ruptura: " + cjruptura.getText());
                    EX(s, 13, 7, "Metodo: " + cjmetodo.getText());
                    EX(s, 14, 4, cjratl.getValue() + " Seg");
                    EX(s, 14, 7, cjtensiondeprueba.getValue().toString());
                    EX(s, 15, 2, cjATcontraBT.getText() + " Gohm");
                    EX(s, 15, 6, cjATcontraTierra.getText() + " Gohm");
                    EX(s, 15, 8, cjBTcontraTierra.getText() + " Gohm");
                    EX(s, 16, 2, cjtensionprimaria.getText());
                    EX(s, 16, 3, cjtensionsecundaria.getText());
                    EX(s, 16, 5, combogrupodeconexion.getSelectedItem().toString());
                    EX(s, 16, 8, combopolaridad.getSelectedItem().toString());
                    for (int i = 0; i < tablauno.getRowCount(); i++) {
                        for (int j = 1; j < tablauno.getColumnCount(); j++) {
                            EX(s, i + 18, j, tablauno.getValueAt(i, j).toString());
                        }
                    }
                    for (int i = 0; i < tablados.getRowCount(); i++) {
                        for (int j = 0; j < tablados.getColumnCount(); j++) {
                            EX(s, i + 18, j + 6, tablados.getValueAt(i, j).toString());
                        }
                    }
                    EX(s, 24, 2, "U-V OHM: " + cjresistenciafaseu.getText());
                    EX(s, 24, 3, "U-W OHM: " + cjresistenciafasev.getText());
                    EX(s, 24, 5, "V-W OHM: " + cjresistenciafasew.getText());
                    EX(s, 24, 7, cjpromedioresistenciaprimaria.getText());
                    EX(s, 24, 8, combomaterialconductor.getSelectedItem().toString());
                    EX(s, 25, 2, "X-Y mohm: " + cjresistenciaxy.getText());
                    EX(s, 25, 3, "Y-Z mohm: " + cjresistenciayz.getText());
                    EX(s, 25, 5, "Z-X mohm: " + cjresistenciazx.getText());
                    EX(s, 25, 7, cjpromedioresistenciasecundaria.getText());
                    EX(s, 25, 8, combomaterialconductordos.getSelectedItem().toString());
                    EX(s, 26, 3, "B.T contra A.T. y Tierra: " + cjBTcontraATytierra.getText());
                    EX(s, 26, 5, "A.T contra B.T y Tierra: " + cjATcontraBTytierra.getText());
                    EX(s, 26, 8, cjtiempoestandar.getText());
                    EX(s, 27, 3, "Tension B.T: " + cjtensionBT.getText());
                    EX(s, 27, 5, "Frecuencia: " + cjfrecuencia.getText() + " Hz");
                    EX(s, 27, 8, cjtiempodeprueba.getText() + " Seg");
                    EX(s, 29, 1, cjvoltajeBT.getText());
                    EX(s, 29, 2, cjcorrientefaseIu.getText());
                    EX(s, 29, 3, cjcorrientefaseIv.getText());
                    EX(s, 29, 4, cjcorrientefaseIw.getText());
                    EX(s, 29, 5, cjpromediocorrienteI.getText());
                    EX(s, 29, 6, cjiogarantizado.getText());
                    EX(s, 29, 7, cjpomedido.getText());
                    EX(s, 29, 8, cjpogarantizado.getText());
                    EX(s, 31, 3, cjperdidasdecobremedidas.getText());
                    if (comborefrigeracion.getSelectedIndex() < 2) {
                        EX(s, 30, 4, "Referidas a 85°C");
                    } else if (comborefrigeracion.getSelectedIndex() == 2) {
                        switch (comboclasedeaislamiento.getSelectedIndex()) {
                            case 0:
                                EX(s, 30, 4, "Referidas a 75°C");
                                break;
                            case 1:
                                EX(s, 30, 4, "Referidas a 85°C");
                                break;
                            case 2:
                                EX(s, 30, 4, "Referidas a 100°C");
                                break;
                            case 3:
                                EX(s, 30, 4, "Referidas a 120°C");
                                break;
                            case 4:
                                EX(s, 30, 4, "Referidas a 145°C");
                                break;
                        }
                    }
                    EX(s, 31, 4, cjpcureferidoa85.getText());
                    EX(s, 31, 7, cjpccgarantizado.getText());
                    EX(s, 32, 3, cji2r.getText());
                    EX(s, 32, 4, cjI2Ra85.getText());
                    EX(s, 33, 3, cjvcc.getText());
                    EX(s, 33, 4, cjz85.getText());
                    EX(s, 33, 7, cjuzgarantizado.getText());
                    EX(s, 34, 3, cjreg.getText());
                    EX(s, 34, 8, cjef.getText());
                    EX(s, 35, 4, cjmasatotal.getText());
                    EX(s, 35, 8, cjvolliquido.getText());
                    EX(s, 36, 4, cjlargodimension.getText());
                    EX(s, 36, 6, cjanchodimension.getText());
                    EX(s, 36, 8, cjaltodimension.getText());
                    EX(s, 37, 4, cjcolor.getText());
                    EX(s, 37, 6, cjespesor.getText());
                    EX(s, 38, 4, cjnumerodelementos.getText());
                    EX(s, 38, 6, cjlargorefrigeracion.getText());
                    EX(s, 38, 8, cjaltorefrigeracion.getText());
                    EX(s, 40, 2, cjobservaciones.getText());
                    String fechacalculada;
                    if (HayQueActualizar) {
                        fechacalculada = fechaexpedicionparaactualizar;
                    } else {
                        fechacalculada = fechaexpedicion;
                    }
                    EX(s, 44, 8, fechacalculada);

                    String guardar = "";
                    if (!HayQueActualizar) {

                        guardar = "INSERT INTO protocolo (cliente , servicio , estadoservicio , noempresa , serieno , idmarca , ";
                        guardar += "potencia , fase , frecuencia , refrigeracion , tensionserie , nba , nbados , calentamientodevanado , ";
                        guardar += "claseaislamiento , altdiseno , fechafabricacion , tensionprimaria , tensionsecundaria , ";
                        guardar += "derivacionprimaria , derivacionsecundaria , corrienteprimaria , corrientesecundaria , liquidoaislante , ";
                        guardar += "temperaturadeensayo , posiciondeconmutador , referenciadeaceite , tensionderuptura , metodo , ratl , ";
                        guardar += "tensiondeprueba , atcontrabt , atcontratierra , btcontratierra , grupodeconexion , polaridad , ";
                        guardar += "punou , punov, punow , pdosu, pdosv , pdosw , ptresu , ptresv , ptresw , pcuatrou , pcuatrov , pcuatrow , ";
                        guardar += "pcincou , pcincov , pcincow , resuv , resuw , resvw , proresuno , resxy , resyz , reszx , proresdos , ";
                        guardar += "materialconductor , iu , iv , iw , promediocorriente , iogarantizado , pomedido , pogarantizado , perdidasdecobre , ";
                        guardar += "i2r , vcc , pcua85 , i2r85 , impedancia85 , pcugarantizado , impedanciagarantizado , reg , ef , masatotal , ";
                        guardar += "volumendeliquido , largotanque , anchotanque , altotanque , color , espesor , numeroelementos , ";
                        guardar += "largorefrigeracion , altorefrigeracion , observaciones , fechaexpedicion , anocreacion , lote , fechasalida ) ";
                        guardar += " VALUES ( '" + cjcliente.getText().toUpperCase().trim() + "' , '" + comboservicio.getSelectedItem().toString() + "' , '" + estado + "' , '" + cjnoempresa.getText().trim() + "' , ";
                        guardar += " '" + cjserieno.getText().trim() + "' , ";
                        guardar += " '" + cjpotencia.getText().trim() + "' , '" + combofasestransformador.getSelectedItem().toString() + "' , '" + (String) cjfrecuencia1.getValue() + "' , ";
                        guardar += " '" + comborefrigeracion.getSelectedItem().toString() + "' , '" + cjtensionserieuno.getText() + "/" + cjtensionseriedos.getText() + "' , ";
                        guardar += " '" + cjnivelbasicodeaislamientouno.getText() + "' , '" + cjnivelbasicodeaislamientodos.getText() + "' , '" + cjcalentamientodevanado.getText() + "' , '" + comboclasedeaislamiento.getSelectedItem().toString() + "' ,  ";
                        guardar += " '" + cjalturadiseno.getText() + "' , '" + cjfechafabricacion.getText() + "' , '" + cjtensionprimaria.getText() + "' , '" + cjtensionsecundaria.getText() + "' , ";
                        guardar += " '" + comboderivacionprimaria.getSelectedItem().toString() + "' , '" + cjderivacionsecundaria.getText() + "' , '" + cjcorrienteprimaria.getText() + "' , ";
                        guardar += " '" + cjcorrientesecundaria.getText() + "' , '" + comboliquidoaislante.getSelectedItem().toString() + "' , '" + cjtemperaturadeprueba.getText() + "' ,  ";
                        guardar += " '" + (String) cjposicionconmutador.getValue().toString() + "' , '" + comboreferenciaaceite.getSelectedItem().toString() + "' , '" + cjruptura.getText() + "' ,  ";
                        guardar += " '" + cjmetodo.getText() + "' , '" + (String) cjratl.getValue().toString() + "' , '" + (String) cjtensiondeprueba.getValue().toString() + "' , '" + cjATcontraBT.getText() + "' , ";
                        guardar += " '" + cjATcontraTierra.getText() + "' , '" + cjBTcontraTierra.getText() + "' , '" + combogrupodeconexion.getSelectedItem().toString() + "' ,  ";
                        guardar += " '" + combopolaridad.getSelectedItem().toString() + "' , '" + tablauno.getValueAt(0, 2) + "' , '" + tablauno.getValueAt(0, 3) + "' , '" + tablauno.getValueAt(0, 4) + "' , ";
                        guardar += " '" + tablauno.getValueAt(1, 2) + "' , '" + tablauno.getValueAt(1, 3) + "' , '" + tablauno.getValueAt(1, 4) + "' , ";
                        guardar += " '" + tablauno.getValueAt(2, 2) + "' , '" + tablauno.getValueAt(2, 3) + "' , '" + tablauno.getValueAt(2, 4) + "' , ";
                        guardar += " '" + tablauno.getValueAt(3, 2) + "' , '" + tablauno.getValueAt(3, 3) + "' , '" + tablauno.getValueAt(3, 4) + "' , ";
                        guardar += " '" + tablauno.getValueAt(4, 2) + "' , '" + tablauno.getValueAt(4, 3) + "' , '" + tablauno.getValueAt(4, 4) + "' , ";
                        guardar += " '" + cjresistenciafaseu.getText() + "' , '" + cjresistenciafasev.getText() + "' , '" + cjresistenciafasew.getText() + "' , '" + cjpromedioresistenciaprimaria.getText() + "' , ";
                        guardar += " '" + cjresistenciaxy.getText() + "' , '" + cjresistenciayz.getText() + "' , '" + cjresistenciazx.getText() + "' , '" + cjpromedioresistenciasecundaria.getText() + "' ,  '" + combomaterialconductor.getSelectedItem().toString() + "-" + combomaterialconductordos.getSelectedItem().toString() + "' , ";
                        guardar += " '" + cjcorrientefaseIu.getText() + "' , '" + cjcorrientefaseIv.getText() + "' , '" + cjcorrientefaseIw.getText() + "' , '" + cjpromediocorrienteI.getText() + "' , ";
                        guardar += " '" + cjiogarantizado.getText() + "' , '" + cjpomedido.getText() + "' , '" + cjpogarantizado.getText() + "' , '" + cjperdidasdecobremedidas.getText() + "' , ";
                        guardar += " '" + cji2r.getText() + "' , '" + cjvcc.getText() + "' , '" + cjpcureferidoa85.getText() + "' , '" + cjI2Ra85.getText() + "' , '" + cjz85.getText() + "' , '" + cjpccgarantizado.getText() + "' , '" + cjuzgarantizado.getText() + "' , ";
                        guardar += " '" + cjreg.getText() + "' , '" + cjef.getText() + "' , '" + cjmasatotal.getText() + "' , '" + cjvolliquido.getText() + "' , ";
                        guardar += " '" + cjlargodimension.getText() + "' , '" + cjanchodimension.getText() + "' , '" + cjaltodimension.getText() + "' , '" + cjcolor.getText() + "' , ";
                        guardar += " '" + cjespesor.getText() + "' , '" + cjnumerodelementos.getText() + "' , '" + cjlargorefrigeracion.getText() + "' , '" + cjaltorefrigeracion.getText() + "' , ";
                        try {
                            guardar += " '" + cjobservaciones.getText() + "' , '" + new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + "' , '" + ultimosdosanos + "' , '" + cjnumerodelote.getText().trim() + "' , '" + new SimpleDateFormat("dd/MM/yyyy").parse(cjfechadesalida.getValue().toString()) + "' , ";
                        } catch (ParseException ex) {
                            Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        guardar += " '" + (garantia.isSelected()) + "'  )";
                    } else {
                        guardar = "UPDATE protocolo SET  ";
                        guardar += " cliente='" + cjcliente.getText().toUpperCase() + "' , servicio='" + comboservicio.getSelectedItem().toString() + "' , noempresa='" + cjnoempresa.getText() + "' , ";
                        guardar += " serieno='" + cjserieno.getText() + "' , ";
                        guardar += " potencia='" + cjpotencia.getText() + "' , fase='" + combofasestransformador.getSelectedItem().toString() + "' , frecuencia='" + (String) cjfrecuencia1.getValue() + "' , ";
                        guardar += " refrigeracion='" + comborefrigeracion.getSelectedItem().toString() + "' , tensionserie='" + cjtensionserieuno.getText() + "/" + cjtensionseriedos.getText() + "' , ";
                        guardar += " nba='" + cjnivelbasicodeaislamientouno.getText() + "' , nbados='" + cjnivelbasicodeaislamientodos.getText() + "' , calentamientodevanado='" + cjcalentamientodevanado.getText() + "' , claseaislamiento='" + comboclasedeaislamiento.getSelectedItem().toString() + "' ,  ";
                        guardar += " altdiseno='" + cjalturadiseno.getText() + "' , fechafabricacion='" + cjfechafabricacion.getText() + "' , tensionprimaria='" + cjtensionprimaria.getText() + "' , tensionsecundaria='" + cjtensionsecundaria.getText() + "' , ";
                        guardar += " derivacionprimaria='" + comboderivacionprimaria.getSelectedItem().toString() + "' , derivacionsecundaria='" + cjderivacionsecundaria.getText() + "' , corrienteprimaria='" + cjcorrienteprimaria.getText() + "' , ";
                        guardar += " corrientesecundaria='" + cjcorrientesecundaria.getText() + "' , liquidoaislante='" + comboliquidoaislante.getSelectedItem().toString() + "' , temperaturadeensayo='" + cjtemperaturadeprueba.getText() + "' ,  ";
                        guardar += " posiciondeconmutador='" + (String) cjposicionconmutador.getValue().toString() + "' , referenciadeaceite='" + comboreferenciaaceite.getSelectedItem().toString() + "' , tensionderuptura='" + cjruptura.getText() + "' ,  ";
                        guardar += " metodo='" + cjmetodo.getText() + "' , ratl='" + (String) cjratl.getValue().toString() + "' , tensiondeprueba='" + (String) cjtensiondeprueba.getValue().toString() + "' , ATcontraBT='" + cjATcontraBT.getText() + "' , ";
                        guardar += " ATcontraTierra='" + cjATcontraTierra.getText() + "' , BTcontraTierra='" + cjBTcontraTierra.getText() + "' , grupodeconexion='" + combogrupodeconexion.getSelectedItem().toString() + "' ,  ";
                        guardar += " polaridad='" + combopolaridad.getSelectedItem().toString() + "' , punou='" + tablauno.getValueAt(0, 2).toString() + "' , punov='" + tablauno.getValueAt(0, 3) + "' , punow='" + tablauno.getValueAt(0, 4).toString() + "' , ";
                        guardar += " pdosu='" + tablauno.getValueAt(1, 2) + "' , pdosv='" + tablauno.getValueAt(1, 3).toString() + "' , pdosw='" + tablauno.getValueAt(1, 4).toString() + "' , ";
                        guardar += " ptresu='" + tablauno.getValueAt(2, 2).toString() + "' , ptresv='" + tablauno.getValueAt(2, 3).toString() + "' , ptresw='" + tablauno.getValueAt(2, 4).toString() + "' , ";
                        guardar += " pcuatrou='" + tablauno.getValueAt(3, 2).toString() + "' , pcuatrov='" + tablauno.getValueAt(3, 3).toString() + "' , pcuatrow='" + tablauno.getValueAt(3, 4).toString() + "' , ";
                        guardar += " pcincou='" + tablauno.getValueAt(4, 2).toString() + "' , pcincov='" + tablauno.getValueAt(4, 3).toString() + "' , pcincow='" + tablauno.getValueAt(4, 4).toString() + "'  , ";
                        guardar += " resuv='" + cjresistenciafaseu.getText() + "' , resuw='" + cjresistenciafasev.getText() + "' , resvw='" + cjresistenciafasew.getText() + "' , proresuno='" + cjpromedioresistenciaprimaria.getText() + "' , ";
                        guardar += " resxy='" + cjresistenciaxy.getText() + "' , resyz='" + cjresistenciayz.getText() + "' , reszx='" + cjresistenciazx.getText() + "' , proresdos='" + cjpromedioresistenciasecundaria.getText() + "' ,  materialconductor='" + combomaterialconductor.getSelectedItem().toString() + "-" + combomaterialconductordos.getSelectedItem().toString() + "' , ";
                        guardar += " iu='" + cjcorrientefaseIu.getText() + "' , iv='" + cjcorrientefaseIv.getText() + "' , iw='" + cjcorrientefaseIw.getText() + "' , promediocorriente='" + cjpromediocorrienteI.getText() + "' , ";
                        guardar += " iogarantizado='" + cjiogarantizado.getText() + "' , pomedido='" + cjpomedido.getText() + "' , pogarantizado='" + cjpogarantizado.getText() + "' , perdidasdecobre='" + cjperdidasdecobremedidas.getText() + "' , ";
                        guardar += " i2r='" + cji2r.getText() + "' , vcc='" + cjvcc.getText() + "' , PCUa85='" + cjpcureferidoa85.getText() + "' , I2r85='" + cjI2Ra85.getText() + "' , impedancia85='" + cjz85.getText() + "' , pcugarantizado='" + cjpccgarantizado.getText() + "' , impedanciagarantizado='" + cjuzgarantizado.getText() + "' , ";
                        guardar += " reg='" + cjreg.getText() + "' , ef='" + cjef.getText() + "' , masatotal='" + cjmasatotal.getText() + "' , volumendeliquido='" + cjvolliquido.getText() + "' , ";
                        guardar += " largotanque='" + cjlargodimension.getText() + "' , anchotanque='" + cjanchodimension.getText() + "' , altotanque='" + cjaltodimension.getText() + "' , color='" + cjcolor.getText() + "' , ";
                        guardar += " espesor='" + cjespesor.getText() + "' , numeroelementos='" + cjnumerodelementos.getText() + "' , largorefrigeracion='" + cjlargorefrigeracion.getText() + "' , altorefrigeracion='" + cjaltorefrigeracion.getText() + "' , ";
                        try {
                            guardar += " observaciones='" + cjobservaciones.getText() + "' , fechaexpedicion='" + fechaexpedicionparaactualizarBD + "' , anocreacion='" + ultimosdosanosaactualizar + "' , lote='" + cjnumerodelote.getText().trim() + "' , ";
                            guardar += " fechasalida='" + new SimpleDateFormat("dd/MM/yyyy").parse(cjfechadesalida.getValue().toString()) + "' , garantia='" + garantia.isSelected() + "' WHERE id_protocolo=" + id_protocoloencontrado + " ";
                        } catch (ParseException ex) {
                            Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (conexion.GUARDAR(guardar)) {
                        if (!HayQueActualizar) {
                            M("EL PROTOCOLO SE HA REGISTRADO EXITOSAMENTE", bien);
                        } else {
                            M("SE EFECTURARON LOS CAMBIOS AL PROTOCOLO DEL CLIENTE " + cjcliente.getText(), bien);
                        }
                        Limpiar();
                        HayQueActualizar = false;
                        jList1.setModel(buscarPDF(""));
                        estado = null;
                        workbook.write(new FileOutputStream(new File(RutaExcelGenerados() + "PROTOCOLO EN EXCEL.xlsx")));
                        file.close();
                        Cargar("");
                        Desktop.getDesktop().open(new File(RutaExcelGenerados() + "PROTOCOLO EN EXCEL.xlsx"));
                        cjfechafabricacion.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("MM/yyyy"))));
                        cjfechafabricacion.setText(mesactual + "/" + anoactual);
                        comboservicio.setSelectedIndex(1);
                    }
                } catch (IOException e) {
                    M("NO SE ENCONTRO LA PLANTILLA DE PROTOCOLOS\n" + e, mal);
                }
            }
        };
        Thread hilo = new Thread(mirun);
        hilo.start();
    }//GEN-LAST:event_submenuProtocoloExcelActionPerformed

    private void comborefrigeracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comborefrigeracionActionPerformed
        try {
            if (comborefrigeracion.getSelectedIndex() == 2) {
                comboclasedeaislamiento.removeAllItems();
                comboclasedeaislamiento.addItem("A:(75°C)");
                comboclasedeaislamiento.addItem("A:(85°C)");
                comboclasedeaislamiento.addItem("A:(100°C)");
                comboclasedeaislamiento.addItem("A:(120°C)");
                comboclasedeaislamiento.addItem("A:(145°C)");
//            PanelLiquidoAislante.setVisible(false);
                //comboliquidoaislante.removeAllItems();
                comboliquidoaislante.setSelectedItem("NO APLICA");
                comboreferenciaaceite.removeAllItems();
                comboreferenciaaceite.addItem("NO APLICA");
                cjruptura.setText("NO APLICA");
                cjmetodo.setText("NO APLICA");
                repaint();
                validate();
            } else {
                comboclasedeaislamiento.removeAllItems();
                comboclasedeaislamiento.addItem("Ao");
                PanelLiquidoAislante.setVisible(true);
//                comboliquidoaislante.removeAllItems();
                Cargarcombo(comboreferenciaaceite, "referenciadeaceite", "nombre_referenciaaceite");
                cjruptura.setText("40");
                cjmetodo.setText("ASTM 877");
            }
            FORMULAS.getValores(cjpotencia, combofasestransformador, comboservicio, cjfechafabricacion, estado, cjiogarantizado, cjpogarantizado, cjpccgarantizado, cjuzgarantizado, cjtensionprimaria, comborefrigeracion, comboclasedeaislamiento, lblmostrarpcu, cjcliente);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_comborefrigeracionActionPerformed

    ResultSet rsSerie;
    private void cjnoempresaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjnoempresaKeyTyped
        if (evt.getKeyChar() == 10) {
            cjmarca.grabFocus();
        }
    }//GEN-LAST:event_cjnoempresaKeyTyped

    private void cjnoempresaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjnoempresaKeyReleased
        if (evt.getKeyCode() == 38) {
            cjnoprotocolo.grabFocus();
        }
        if (evt.getKeyCode() == 40) {
            cjmarca.grabFocus();
        }
    }//GEN-LAST:event_cjnoempresaKeyReleased

    private void cjaltodimensionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjaltodimensionKeyReleased
        if (evt.getKeyCode() == 37) {
            cjanchodimension.grabFocus();
        }
    }//GEN-LAST:event_cjaltodimensionKeyReleased

    private void cjnumerodelementosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjnumerodelementosKeyReleased
        if (evt.getKeyCode() == 37) {
            cjlargorefrigeracion.grabFocus();
        }
    }//GEN-LAST:event_cjnumerodelementosKeyReleased

    private void cjlargorefrigeracionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjlargorefrigeracionKeyReleased
        if (evt.getKeyCode() == 37) {
            cjnumerodelementos.grabFocus();
        }
        if (evt.getKeyCode() == 39) {
            cjaltorefrigeracion.grabFocus();
        }
    }//GEN-LAST:event_cjlargorefrigeracionKeyReleased

    private void cjaltorefrigeracionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjaltorefrigeracionKeyReleased
        if (evt.getKeyCode() == 37) {
            cjlargorefrigeracion.grabFocus();
        }
    }//GEN-LAST:event_cjaltorefrigeracionKeyReleased

    private void cjanchodimensionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjanchodimensionKeyReleased
        if (evt.getKeyCode() == 37) {
            cjlargodimension.grabFocus();
        }
        if (evt.getKeyCode() == 39) {
            cjaltodimension.grabFocus();
        }
    }//GEN-LAST:event_cjanchodimensionKeyReleased

    private void cjperdidasdecobremedidasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjperdidasdecobremedidasKeyReleased
        if (evt.getKeyCode() == 39) {
            cjvcc.grabFocus();
        }
        if (evt.getKeyCode() == 37) {
            cjpomedido.grabFocus();
        }
    }//GEN-LAST:event_cjperdidasdecobremedidasKeyReleased

    private void cjvccKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjvccKeyReleased
        if (evt.getKeyCode() == 37) {
            cjperdidasdecobremedidas.grabFocus();
        }
    }//GEN-LAST:event_cjvccKeyReleased

    private void cjATcontraTierraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjATcontraTierraKeyReleased
        if (evt.getKeyCode() == 38) {
            cjATcontraBT.grabFocus();
        }
        if (evt.getKeyCode() == 40) {
            cjBTcontraTierra.grabFocus();
        }
    }//GEN-LAST:event_cjATcontraTierraKeyReleased

    private void cjBTcontraTierraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjBTcontraTierraKeyReleased
        if (evt.getKeyCode() == 38) {
            cjATcontraTierra.grabFocus();
        }
    }//GEN-LAST:event_cjBTcontraTierraKeyReleased

    private void cjATcontraBTKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjATcontraBTKeyReleased
        if (evt.getKeyCode() == 40) {
            cjATcontraTierra.grabFocus();
        }
    }//GEN-LAST:event_cjATcontraBTKeyReleased

    private void cjfechadesalidaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjfechadesalidaKeyTyped
        if (evt.getKeyChar() == 10) {
            if (null == cjfechadesalida.getValue()) {
                M("EL FORMATO DE FECHA SE ENCUENTRA MAL ESCRITO", mal);
            } else {
                CONTENEDOR.setSelectedComponent(TODO);
                TODO.setSelectedComponent(PANELTODO);
                if (JOptionPane.showConfirmDialog(this, "Desea generar el reporte ? ", "CONNFIRMAR GENERAR REPORTE", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    GenerarReporte();
                    Cargar("");
                }
            }
        }
    }//GEN-LAST:event_cjfechadesalidaKeyTyped

    private void cjbuscarporclienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjbuscarporclienteKeyReleased
        cjbuscarplaca.setText(null);
        Cargar(cjbuscarporcliente.getText().trim());
    }//GEN-LAST:event_cjbuscarporclienteKeyReleased

    private void cjbuscarplacaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjbuscarplacaKeyReleased
        if (!cjbuscarporcliente.getText().isEmpty()) {
            cjbuscarporcliente.setText(null);
            comboBuscarPorLote.setSelectedIndex(0);//.setText(null);
            cjbuscarpormarca.setText(null);
            cjbuscarplaca.grabFocus();
        }
        Cargar(cjbuscarplaca.getText().trim());
    }//GEN-LAST:event_cjbuscarplacaKeyReleased

    private void cjbuscarpormarcaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjbuscarpormarcaKeyReleased
        cjbuscarplaca.setText(null);
        Cargar(cjbuscarporcliente.getText().trim());
    }//GEN-LAST:event_cjbuscarpormarcaKeyReleased

    private void cjnumerodeloteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjnumerodeloteKeyTyped
        if (evt.getKeyChar() == 10) {
            cjobservaciones.grabFocus();
        }
    }//GEN-LAST:event_cjnumerodeloteKeyTyped

    private void cjespesorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjespesorKeyTyped
        if (evt.getKeyChar() == 10) {
            cjnumerodelementos.grabFocus();
        }
    }//GEN-LAST:event_cjespesorKeyTyped

    private void cjcolorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjcolorKeyTyped
        if (evt.getKeyChar() == 10) {
            cjespesor.grabFocus();
        }
    }//GEN-LAST:event_cjcolorKeyTyped

    private void cjvolliquidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjvolliquidoKeyTyped
        if (evt.getKeyChar() == 10) {
            cjlargodimension.grabFocus();
        }
    }//GEN-LAST:event_cjvolliquidoKeyTyped

    private void cjvolliquidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjvolliquidoKeyReleased
        if (evt.getKeyCode() == 37) {
            cjmasatotal.grabFocus();
        }
    }//GEN-LAST:event_cjvolliquidoKeyReleased

    private void cjmasatotalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjmasatotalKeyTyped
        if (evt.getKeyChar() == 10) {
            cjvolliquido.grabFocus();
        }
    }//GEN-LAST:event_cjmasatotalKeyTyped

    private void cjmasatotalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjmasatotalKeyReleased
        if (evt.getKeyCode() == 39) {
            cjvolliquido.grabFocus();
        }
    }//GEN-LAST:event_cjmasatotalKeyReleased

    private void cjzKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjzKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_cjzKeyTyped

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        try {
            Desktop.getDesktop().browse(new URI("http://smallpdf.com/es/comprimir-pdf"));

        } catch (Exception ex) {
            Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "ERROR AL ABRIR LA PAGINA.\n" + ex);
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void cjdisenoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjdisenoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_cjdisenoKeyReleased

    private void cjdisenoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjdisenoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_cjdisenoKeyTyped

    private void inteligenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inteligenteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inteligenteActionPerformed

    private void cjbuscarporclienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjbuscarporclienteKeyTyped
        if (evt.getKeyChar() == 10) {
            AutoCompletarLote(cjbuscarporcliente.getText());
        }
    }//GEN-LAST:event_cjbuscarporclienteKeyTyped

    private void comboBuscarPorLoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBuscarPorLoteActionPerformed
        Cargar("");
    }//GEN-LAST:event_comboBuscarPorLoteActionPerformed

    private void comboclasedeaislamientoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboclasedeaislamientoItemStateChanged
        if(evt.getStateChange() == ItemEvent.DESELECTED){
            FORMULAS.getValores(cjpotencia, combofasestransformador, comboservicio, cjfechafabricacion, estado, cjiogarantizado, cjpogarantizado, cjpccgarantizado, cjuzgarantizado, cjtensionprimaria, comborefrigeracion, comboclasedeaislamiento, lblmostrarpcu, cjcliente);
        }
    }//GEN-LAST:event_comboclasedeaislamientoItemStateChanged

    public void M(String m, Icon i) {
        try {
            JOptionPane.showMessageDialog(this, m, "Aviso", JOptionPane.INFORMATION_MESSAGE, i);
        } catch (NoSuchElementException e) {
        }
    }

    public void Subir(JTextField arriba, JTextField abajo) {
        arriba.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

            }
        });
    }

    public void Bajar() {

    }

    public void Borrar(final JTextField t) {
        t.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                t.setText(null);
            }
        });
    }

    public void SoloNumeros(final JTextField t) {
        t.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char t = e.getKeyChar();
                if (Character.isLetter(t)) {
                    e.consume();
                    getToolkit().beep();
                }
            }
        });
    }

    public void Enter(final JTextField u, final JTextField d) {
        u.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 10) {
                    d.grabFocus();
                }
            }
        });
    }

    public void BordeGris(final JTextField t) {
        t.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                t.setBorder(new LineBorder(Color.gray));
            }
        });
    }

    public void SoloNumerosYComa(final JTextField t) {
        t.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char t = e.getKeyChar();
                if (Character.isLetter(t)) {
                    e.consume();
                    getToolkit().beep();
                }
                if (Character.toString(t).equalsIgnoreCase(".")) {
                    JOptionPane.showMessageDialog(null, "Para ingresar decimales digite la coma(,)");
                    e.consume();
                    getToolkit().beep();
                }
            }
        });
    }

    public boolean EstaVacio(JTextField t) {
        return t.getText().isEmpty();
    }

    public void Limpiar() {
        cjcliente.setText("");
        cjnoprotocolo.setText("");
        cjnoempresa.setText("");
        cjserieno.setText("");
        cjtensionserieuno.setText("");
        cjtensionseriedos.setText("");
        cjnivelbasicodeaislamientouno.setText("");
        cjpotencia.setText("");
        cjtensionprimaria.setText("");
        cjtensionsecundaria.setText("");
        cjcorrienteprimaria.setText("");
        cjcorrientesecundaria.setText("");
        cjderivacionsecundaria.setText("");
        cjcorrienteprimariadespacho.setText("");
        cjcorrientesecundariadespacho.setText("");
        cjATcontraBT.setText("");
        cjATcontraTierra.setText("");
        cjBTcontraTierra.setText("");
        cjrelacionuno.setText("");
        cjrelaciondos.setText("");
        cjtemperaturadeprueba.setText("");
        cjresistenciafaseu.setText("");
        cjresistenciafasev.setText("");
        cjresistenciafasew.setText("");
        cjpromedioresistenciaprimaria.setText("");
        cjresistenciaxy.setText("");
        cjresistenciayz.setText("");
        cjresistenciazx.setText("");
        cjpromedioresistenciasecundaria.setText("");
        cjtensionBT.setText("");
        cjtiempodeprueba.setText("");
        cjvoltajeBT.setText("");
        cjcorrientefaseIu.setText("");
        cjcorrientefaseIv.setText("");
        cjcorrientefaseIw.setText("");
        cjpromediocorrienteI.setText("");
        cjiogarantizado.setText("");
        cjpomedido.setText("");
        cjpogarantizado.setText("");
        cjperdidasdecobremedidas.setText("");
        cjvcc.setText("");
        cjpcureferidoa85.setText("");
        cjpccgarantizado.setText("");
        cjI2Ra85.setText("");
        cji2r.setText("");
        cjz85.setText("");
        cjuzgarantizado.setText("");
        cjmasatotal.setText("");
        cjvolliquido.setText("");
        cjlargodimension.setText("");
        cjanchodimension.setText("");
        cjaltodimension.setText("");
        cjcolor.setText("GRIS");
        cjespesor.setText("110");
        cjnumerodelementos.setText("");
        cjlargorefrigeracion.setText("");
        cjaltorefrigeracion.setText("");
        cjobservaciones.setText("");
        cjtemperaturadeprueba.setText("30");
        cjnumerodelote.setText("");
        cjfechafabricacion.setText("");
        for (int i = 0; i < tablauno.getRowCount(); i++) {
            for (int j = 1; j < tablauno.getColumnCount(); j++) {
                tablauno.setValueAt("", i, j);
            }
        }
        for (int i = 0; i < tablados.getRowCount(); i++) {
            for (int j = 0; j < tablados.getColumnCount(); j++) {
                tablados.setValueAt("", i, j);
            }
        }
        garantia.setSelected(false);
    }

    public int getIdMarca(String nombre) {
        int id = 0;
        String sql = "SELECT * FROM marcatransformador WHERE marca_marcatransformador='" + nombre + "' ";
        conexion.conectar();
        ResultSet rs = conexion.CONSULTAR(sql);
        try {
            if (rs.next()) {
                id = rs.getInt("id_marcatransformador");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
            M("ERROR AL OBTENER EL ID DE LAS MARCAS\n" + ex, mal);
        }finally{
            conexion.CERRAR();
        }
        return id;
    }

    public int getIdCliente(String nombreCliente) {
        int id = 0;
        String sql = "SELECT * FROM cliente WHERE nombre_cliente='" + nombreCliente + "' ";
        conexion.conectar();
        ResultSet rs = conexion.CONSULTAR(sql);
        try {
            if (rs.next()) {
                id = rs.getInt("id_cliente");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
            M("ERROR AL OBTENER EL ID DEL CLIENTE\n" + ex, mal);
        }finally{
            conexion.CERRAR();
        }
        return id;
    }

    public String getNombreMarca(int id) {
        String marca = null;
        String sql = "SELECT * FROM marcatransformador WHERE id_marcatransformador=" + id + " ";
        conexion.conectar();
        ResultSet rs = conexion.CONSULTAR(sql);
        try {
            if (rs.next()) {
                marca = rs.getString("marca_marcatransformador");
            }
        } catch (SQLException e) {
            M("Error al trar la marca del transformador", mal);
        }finally{
            conexion.CERRAR();
        }
        return marca;
    }

    public void Actualizar() {
        if (datos.getSelectedRow() != -1) {
            String sql = null;
            try {
                String idp[] = datos.getValueAt(datos.getSelectedRow(), 0).toString().split("-");
                int id = Integer.parseInt(idp[1]);
                sql = "SELECT * FROM protocolo WHERE id_protocolo=" + id + " ";
            } catch (java.lang.NumberFormatException e) {
                M("ERROR AL OBTENER EL VALOR ID DEL PROTOCOLO PARA SER MOSTRADO", mal);
            }
            conexion.conectar();
            ResultSet rs = conexion.CONSULTAR(sql);
            try {
                if (rs.next()) {
                    estado = rs.getString("estadoservicio");
                    HayQueActualizar = true;
                    ESTA_ACTUALIZANDO = true;
                    cjnoprotocolo.setText("A-" + rs.getInt("id_protocolo") + "-" + rs.getString("anocreacion"));
                    try {
                        cjcliente.setText((rs.getString("cliente")));
                    } catch (java.awt.IllegalComponentStateException exe) {
                        //M("NO SE PUDO MOSTRAR EL NOMBRE DEL CLIENTE A QUIEN PERTENECE EL PROTOCOLO. INGRESELO MANUALMENTE.",mal);
                    }
                    comboservicio.setSelectedItem(rs.getString("servicio"));
                    cjnoempresa.setText(rs.getString("noempresa"));
                    try {
                        cjserieno.setText(rs.getString("serieno"));
                    } catch (java.awt.IllegalComponentStateException e) {
                    }
                    cjmarca.setText(datos.getValueAt(datos.getSelectedRow(), 4).toString());
                    cjpotencia.setText(datos.getValueAt(datos.getSelectedRow(), 5).toString().replace(",", "."));
                    combofasestransformador.setSelectedItem(datos.getValueAt(datos.getSelectedRow(), 6));
                    cjfrecuencia1.setValue(rs.getString("frecuencia"));
                    comborefrigeracion.setSelectedItem(rs.getString("refrigeracion"));
                    try {
                        String[] tensionserie = rs.getString("tensionserie").split("/");
                        cjtensionserieuno.setText(tensionserie[0]);
                        cjtensionseriedos.setText(tensionserie[1]);
                    } catch (java.lang.ArrayIndexOutOfBoundsException ext) {
                    }
                    cjnivelbasicodeaislamientouno.setText(rs.getString("nba"));
                    cjnivelbasicodeaislamientodos.setText(rs.getString("nbados"));
                    cjcalentamientodevanado.setText(rs.getString("calentamientodevanado"));
                    comboclasedeaislamiento.addItem(rs.getString("claseaislamiento"));
                    cjalturadiseno.setText(rs.getString("altdiseno"));
                    cjfechafabricacion.setText(rs.getString("fechafabricacion"));
                    cjtensionprimaria.setText(rs.getString("tensionprimaria"));
                    cjtensionsecundaria.setText(rs.getString("tensionsecundaria"));
                    comboderivacionprimaria.setSelectedItem(rs.getString("derivacionprimaria"));
                    cjderivacionsecundaria.setText(rs.getString("derivacionsecundaria"));
                    cjcorrienteprimaria.setText(rs.getString("corrienteprimaria"));
                    cjcorrientesecundaria.setText(rs.getString("corrientesecundaria"));
                    cjcorrienteprimariadespacho.setText(cjcorrienteprimaria.getText());
                    cjcorrientesecundariadespacho.setText(cjcorrientesecundaria.getText());
                    comboliquidoaislante.setSelectedItem(rs.getString("liquidoaislante"));
                    cjtemperaturadeprueba.setText(rs.getString("temperaturadeensayo"));
                    cjposicionconmutador.setValue(rs.getInt("posiciondeconmutador"));
                    comboreferenciaaceite.setSelectedItem(rs.getString("referenciadeaceite"));
                    cjruptura.setText(rs.getString("tensionderuptura"));
                    cjmetodo.setText(rs.getString("metodo"));
                    cjratl.setValue(Integer.parseInt(rs.getString("ratl")));
                    cjtensiondeprueba.setValue(rs.getString("tensiondeprueba"));
                    cjATcontraBT.setText(rs.getString("ATcontraBT"));
                    cjATcontraTierra.setText(rs.getString("ATcontraTierra"));
                    cjBTcontraTierra.setText(rs.getString("BTcontraTierra"));
                    cjrelacionuno.setText(cjtensionprimaria.getText());
                    cjrelaciondos.setText(cjtensionsecundaria.getText());
                    combogrupodeconexion.setSelectedItem(rs.getString("grupodeconexion"));
                    combopolaridad.setSelectedItem(rs.getString("polaridad"));
                    try {
                        CargarTablaUno(Integer.parseInt(cjtensionprimaria.getText()), (int) cjposicionconmutador.getValue(), combofasestransformador);
                    } catch (java.lang.NumberFormatException t) {
                    }
                    String fase = null;
                    String pos = null;
                    for (int i = 0; i < 5; i++) {
                        if (i == 0) {
                            pos = "uno";
                        } else if (i == 1) {
                            pos = "dos";
                        } else if (i == 2) {
                            pos = "tres";
                        } else if (i == 3) {
                            pos = "cuatro";
                        } else if (i == 4) {
                            pos = "cinco";
                        }
                        for (int j = 2; j < 5; j++) {
                            if (j == 2) {
                                fase = "u";
                            } else if (j == 3) {
                                fase = "v";
                            } else if (j == 4) {
                                fase = "w";
                            }
                            tablauno.setValueAt(rs.getString("p" + pos + fase), i, j);
                        }
                    }
                    MostrarNominalTablaDos();
//                    tablauno.setDefaultRenderer(Object.class, new ColorTablaFases(valores, Integer.parseInt(combofasestransformador.getSelectedItem().toString())));
                    cjresistenciafaseu.setText(rs.getString("resuv"));
                    cjresistenciafasev.setText(rs.getString("resuw"));
                    cjresistenciafasew.setText(rs.getString("resvw"));
                    cjpromedioresistenciaprimaria.setText(rs.getString("proresuno"));
                    cjresistenciaxy.setText(rs.getString("resxy"));
                    cjresistenciayz.setText(rs.getString("resyz"));
                    cjresistenciazx.setText(rs.getString("reszx"));
                    cjpromedioresistenciasecundaria.setText(rs.getString("proresdos"));
                    if (rs.getString("materialconductor").split("-").length == 2) {
                        combomaterialconductor.setSelectedItem(rs.getString("materialconductor").split("-")[0]);
                        combomaterialconductordos.setSelectedItem(rs.getString("materialconductor").split("-")[1]);
                    } else {
                        combomaterialconductor.setSelectedItem(rs.getString("materialconductor"));
                        combomaterialconductordos.setSelectedItem(rs.getString("materialconductor"));
                    }
                    cjcorrientefaseIu.setText(rs.getString("iu"));
                    cjcorrientefaseIv.setText(rs.getString("iv"));
                    cjcorrientefaseIw.setText(rs.getString("iw"));
                    cjpromediocorrienteI.setText(rs.getString("promediocorriente"));
                    cjiogarantizado.setText(rs.getString("iogarantizado"));
                    cjpomedido.setText(rs.getString("pomedido"));
                    cjpogarantizado.setText(rs.getString("pogarantizado"));
                    cjperdidasdecobremedidas.setText(rs.getString("perdidasdecobre"));
                    cji2r.setText(rs.getString("i2r"));
                    cjvcc.setText(rs.getString("vcc"));
                    cjpcureferidoa85.setText(rs.getString("pcua85"));
                    cjI2Ra85.setText(rs.getString("i2r85"));
                    cjz85.setText(rs.getString("impedancia85"));
                    cjpccgarantizado.setText(rs.getString("pcugarantizado"));
                    cjuzgarantizado.setText(rs.getString("impedanciagarantizado"));
                    cjreg.setText(rs.getString("reg"));
                    cjef.setText(rs.getString("ef"));
                    cjmasatotal.setText(rs.getString("masatotal"));
                    cjvolliquido.setText(rs.getString("volumendeliquido"));
                    cjlargodimension.setText(rs.getString("largotanque"));
                    cjanchodimension.setText(rs.getString("anchotanque"));
                    cjaltodimension.setText(rs.getString("altotanque"));
                    cjcolor.setText(rs.getString("color"));
                    cjespesor.setText(rs.getString("espesor"));
                    cjnumerodelementos.setText(rs.getString("numeroelementos"));
                    cjlargorefrigeracion.setText(rs.getString("largorefrigeracion"));
                    cjaltorefrigeracion.setText(rs.getString("altorefrigeracion"));
                    cjobservaciones.setText(rs.getString("observaciones"));
//                Date fechaexpedido = rs.getDate("fechaexpedicion");
                    fechaexpedicionparaactualizar = new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate("fechaexpedicion"));
                    fechaexpedicionparaactualizarBD = new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("fechaexpedicion"));
                    ultimosdosanosaactualizar = rs.getString("anocreacion");
                    cjnumerodelote.setText(rs.getString("lote"));
                    id_protocoloencontrado = rs.getInt("id_protocolo");
                    CONTENEDOR.setSelectedComponent(TODO);
                    TODO.setSelectedComponent(PANELTODO);
                    cjtiempodeprueba.setText("" + FORMULAS.HallarTiempodePrueba(cjfrecuencia));
                    cjtensionBT.setText("" + FORMULAS.HallarTensionBT(cjtensionsecundaria));
                    cjvoltajeBT.setText(cjtensionsecundaria.getText());
                    cjfechadesalida.setText(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("fechasalida")));
                    TODO.setSelectedComponent(INTERNOPANELCARACTERISTICAS);
                    cjfechadesalida.grabFocus();
                    cjz.setText("" + FORMULAS.QuitarDecimales(getZ(cjvcc, cjtensionprimaria), 3));
                    repaint();
                    validate();
                }
            } catch (SQLException e) {
                M("ERROR AL REALIZAR LA CONSULTA PARA MOSTRAR LOS DATOS DEL PROTOCOLO\n" + e, mal);
            }finally{
                conexion.CERRAR();
            }
        } else {
            M("PRIMERO DEBE SELECCIONAR UNA FILA", mal);
        }
    }
    
    public DefaultListModel buscarPDF(String parametro) {
        DefaultListModel defaultListModel = new DefaultListModel();
        File directorio = new File(RutaPDF());
        // Si es un directorio valido
        if (directorio.isDirectory()) {
            // obtenemos su contenido
            File[] ficheros = directorio.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".pdf");
            }
        });
                //y lo llenamos en un DefaultListModel
                for (File fichero : ficheros) {
                    if (fichero.getName().toUpperCase().contains(parametro.toUpperCase())) {
                        defaultListModel.addElement(fichero.getName());

                    }
                }
            }
            return defaultListModel;
        }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PROTOCOLO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

//                JFrame.setDefaultLookAndFeelDecorated(true);
//                try {
//                    UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
//                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
//                    Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
//                }
                new PROTOCOLO().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane CONTENEDOR;
    private javax.swing.JMenuItem EliminarProtocolo;
    private javax.swing.JPanel INTERNOPANELCARACTERISTICAS;
    private javax.swing.JTabbedPane LISTAPROTOCOLOS;
    private javax.swing.JPanel PANELTODO;
    private javax.swing.JTabbedPane PDFPROTOCOLOS;
    private javax.swing.JPanel PanelLiquidoAislante;
    private javax.swing.JMenuItem SubMenuAbrir;
    private javax.swing.JPopupMenu SubMenuAgregarCliente;
    private javax.swing.JMenuItem SubMenuExcelCompleto;
    private javax.swing.JMenuItem SubMenuExcelControl;
    private javax.swing.JPopupMenu SubMenuJList;
    private javax.swing.JMenuItem SubMenuNuevoCliente;
    private javax.swing.JPopupMenu SubMenuVerProtocolo;
    private javax.swing.JPopupMenu SubMenuVerifica;
    private javax.swing.JMenuItem SubMenuVerificarErrores;
    private javax.swing.JTabbedPane TODO;
    private javax.swing.JMenuItem VerProtocolo;
    private javax.swing.JProgressBar barra;
    private javax.swing.JButton btnbuscarpdf;
    private javax.swing.JCheckBoxMenuItem checkMostrarReporte;
    private javax.swing.JTextField cjATcontraBT;
    private javax.swing.JTextField cjATcontraBTytierra;
    private javax.swing.JTextField cjATcontraTierra;
    private javax.swing.JTextField cjBTcontraATytierra;
    private javax.swing.JTextField cjBTcontraTierra;
    private com.toedter.calendar.JDateChooser cjFechaDesde;
    private com.toedter.calendar.JDateChooser cjFechaHasta;
    private javax.swing.JTextField cjI2Ra85;
    private javax.swing.JTextField cjaltodimension;
    private javax.swing.JTextField cjaltorefrigeracion;
    private javax.swing.JFormattedTextField cjalturadiseno;
    private javax.swing.JTextField cjanchodimension;
    private javax.swing.JTextField cjbuscarpdf;
    private javax.swing.JTextField cjbuscarplaca;
    private javax.swing.JTextField cjbuscarporcliente;
    private javax.swing.JTextField cjbuscarpormarca;
    private javax.swing.JTextField cjcalentamientodevanado;
    public static javax.swing.JTextField cjcliente;
    public static javax.swing.JTextField cjcolor;
    private javax.swing.JTextField cjcorrientefaseIu;
    private javax.swing.JTextField cjcorrientefaseIv;
    private javax.swing.JTextField cjcorrientefaseIw;
    private javax.swing.JTextField cjcorrienteprimaria;
    private javax.swing.JTextField cjcorrienteprimariadespacho;
    private javax.swing.JTextField cjcorrientesecundaria;
    private javax.swing.JTextField cjcorrientesecundariadespacho;
    private javax.swing.JTextField cjderivacionsecundaria;
    private javax.swing.JTextField cjdiseno;
    private javax.swing.JTextField cjef;
    public static javax.swing.JTextField cjespesor;
    private javax.swing.JFormattedTextField cjfechadesalida;
    public static javax.swing.JFormattedTextField cjfechafabricacion;
    private javax.swing.JTextField cjfrecuencia;
    private javax.swing.JSpinner cjfrecuencia1;
    private javax.swing.JTextField cji2r;
    private javax.swing.JTextField cjiogarantizado;
    private javax.swing.JTextField cjlargodimension;
    private javax.swing.JTextField cjlargorefrigeracion;
    public static javax.swing.JTextField cjmarca;
    public static javax.swing.JTextField cjmasatotal;
    private javax.swing.JTextField cjmetodo;
    private javax.swing.JTextField cjnivelbasicodeaislamientodos;
    private javax.swing.JTextField cjnivelbasicodeaislamientouno;
    public static javax.swing.JTextField cjnoempresa;
    private javax.swing.JTextField cjnoprotocolo;
    private javax.swing.JTextField cjnumerodelementos;
    public static javax.swing.JTextField cjnumerodelote;
    private javax.swing.JTextArea cjobservaciones;
    private javax.swing.JTextField cjpccgarantizado;
    private javax.swing.JTextField cjpcureferidoa85;
    private javax.swing.JTextField cjperdidasdecobremedidas;
    private javax.swing.JTextField cjpogarantizado;
    private javax.swing.JTextField cjpomedido;
    private javax.swing.JSpinner cjposicionconmutador;
    public static javax.swing.JTextField cjpotencia;
    private javax.swing.JTextField cjpromediocorrienteI;
    private javax.swing.JTextField cjpromedioresistenciaprimaria;
    private javax.swing.JTextField cjpromedioresistenciasecundaria;
    private javax.swing.JSpinner cjratl;
    private javax.swing.JTextField cjreg;
    private javax.swing.JTextField cjrelaciondos;
    private javax.swing.JTextField cjrelacionuno;
    private javax.swing.JTextField cjresistenciafaseu;
    private javax.swing.JTextField cjresistenciafasev;
    private javax.swing.JTextField cjresistenciafasew;
    private javax.swing.JTextField cjresistenciaxy;
    private javax.swing.JTextField cjresistenciayz;
    private javax.swing.JTextField cjresistenciazx;
    private javax.swing.JTextField cjruptura;
    private javax.swing.JTextField cjserieno;
    private javax.swing.JTextField cjtemperaturadeprueba;
    private javax.swing.JTextField cjtensionBT;
    private javax.swing.JSpinner cjtensiondeprueba;
    public static javax.swing.JTextField cjtensionprimaria;
    public static javax.swing.JTextField cjtensionsecundaria;
    private javax.swing.JTextField cjtensionseriedos;
    private javax.swing.JTextField cjtensionserieuno;
    private javax.swing.JTextField cjtiempodeprueba;
    private javax.swing.JTextField cjtiempoestandar;
    private javax.swing.JTextField cjuzgarantizado;
    private javax.swing.JTextField cjvcc;
    public static javax.swing.JTextField cjvolliquido;
    private javax.swing.JTextField cjvoltajeBT;
    private javax.swing.JTextField cjz;
    private javax.swing.JTextField cjz85;
    private javax.swing.JComboBox<String> comboBuscarPorLote;
    public static javax.swing.JComboBox comboclasedeaislamiento;
    public static javax.swing.JComboBox comboderivacionprimaria;
    public static javax.swing.JComboBox combofasestransformador;
    public static javax.swing.JComboBox combogrupodeconexion;
    public static javax.swing.JComboBox comboliquidoaislante;
    public static javax.swing.JComboBox combomaterialconductor;
    public static javax.swing.JComboBox combomaterialconductordos;
    public static javax.swing.JComboBox combopolaridad;
    public static javax.swing.JComboBox comboreferenciaaceite;
    public static javax.swing.JComboBox comborefrigeracion;
    public static javax.swing.JComboBox comboservicio;
    private javax.swing.JTable datos;
    private javax.swing.JCheckBox garantia;
    private javax.swing.JCheckBoxMenuItem inteligente;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblIv;
    private javax.swing.JLabel lblIw;
    private javax.swing.JLabel lblmostrarpcu;
    private javax.swing.JLabel lblresfasev;
    private javax.swing.JLabel lblresfasew;
    private javax.swing.JLabel lblyz;
    private javax.swing.JLabel lblzx;
    private javax.swing.JMenuItem subMenuItemCaracteristicasTrafo;
    private javax.swing.JMenuItem subMenuItemResultadosDeEnsayo;
    private javax.swing.JMenuItem submenuProtocoloExcel;
    private javax.swing.JTable tablados;
    private javax.swing.JTable tablauno;
    // End of variables declaration//GEN-END:variables
        
    private String RutaPDF(){
        return System.getProperties().getProperty("user.dir")+"\\PROTOCOLOS PDF\\";
    }
    
    private String RutaPlantillasExcel(){
        return System.getProperties().getProperty("user.dir")+"\\PLANTILLAS EXCEL\\";
    }
    
    private String RutaExcelGenerados(){
        return System.getProperties().getProperty("user.dir")+"\\REPORTES GENERADOS\\";
    }
    
    public boolean work = true;

    private class Tempo implements Runnable {

        @Override
        public void run() {
            while (true) {
                if (work){
                    if (inteligente.isSelected()) {
                        if(valores[0][0] > 0){
                            for (int row = 0; row < tablauno.getRowCount(); row++){
                                for (int col = 2; col < tablauno.getRowCount(); col++){
                                    try {
                                        double fase = Double.parseDouble(String.valueOf(tablauno.getValueAt(row, col)));
                                        if (fase > 0) {
                                            double minima = valores[row][0];
                                            double maxima = valores[row][1];
                                            String m = "";
                                            boolean si = false;
                                            if (fase < minima || fase > maxima) {
                                                si = true;
                                                m = "El valor de " + fase + " en la " + tablauno.getColumnName(col) + " de la " + tablauno.getValueAt(row, 0) + " se encuentra FUERA DE RANGO\n\nSI DESEA DESACTIVAR LA BUSQUEDA DE ERRORES AUTOMATICA,\nDESACTEVIE LA OPCION 'MODO INTELIGENTE' OPCIONES O PRESIONE F5";
                                            }
                                            if (si) {
                                                try {
                                                    sonido = AudioSystem.getClip();
                                                    sonido.open(AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/recursos/audio/error.wav")));
                                                    sonido.start();
                                                } catch (Exception ex) {
                                                    Logger.getLogger(PROTOCOLO.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                                int confirmar = JOptionPane.showConfirmDialog(null, m + "\nDesea cambiar el valor ?", "FUERA DE RANGO!", JOptionPane.YES_NO_OPTION);
                                                if (confirmar == JOptionPane.YES_OPTION) {
                                                    while (true) {
                                                        try {
                                                            try {
                                                                double nuevo = Double.parseDouble(JOptionPane.showInputDialog(drmt, "Ingrese el nuevo valor a remplazar"));
                                                                tablauno.setValueAt(nuevo, row, col);
                                                                break;
                                                            } catch (NullPointerException e) {
                                                                break;
                                                            }
                                                        } catch (NumberFormatException e) {
                                                            M("Ingrese un numero valido", mal);
                                                        }
                                                    }
                                                } else {
                                                    work = false;
                                                    break;
                                                }
                                            }
                                        }
                                    } catch (NumberFormatException e) {
                                    }
                                }
                            }
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            JOptionPane.showMessageDialog(null, "Error en el hilo dell JProgressBar\n" + e);
                        }
                    }
                }
            }
        }
    }

}
