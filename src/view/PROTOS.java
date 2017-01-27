package view;

import Dialogos.DialogoTrafosRepetidos;
import java.awt.HeadlessException;
import java.awt.event.ItemEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import modelo.ConexionBD;

public class PROTOS extends javax.swing.JFrame {

    private String ESTADO_TRAFO = null;
    ConexionBD conex = new ConexionBD();
    private final int IDTRAFO = -1;
    TableModelListener listenerTablaUno;
    Hilofases alertas;
    
    public PROTOS() {
        initComponents();
        
        habilitarCampos((comboFase.getSelectedIndex()==0));
    }
    
    void HallarTensionSerie(){
        String tensionserie = "", nba = "";
        tensionserie = (7000 <= cjvp.getInt() && cjvp.getInt() <= 15000)?"15":(16000 <= cjvp.getInt() && cjvp.getInt() <= 25000)?"25":(26000 <= cjvp.getInt() && cjvp.getInt() <= 38000)?"38":(39000 <= cjvp.getInt() && cjvp.getInt() <= 52000)?"52":(cjvp.getInt() <= 1200)?"1.2":"0";
        nba = (7000 <= cjvp.getInt() && cjvp.getInt() <= 15000)?"95":(16000 <= cjvp.getInt() && cjvp.getInt() <= 25000)?"125":(26000 <= cjvp.getInt() && cjvp.getInt() <= 38000)?"200":(39000 <= cjvp.getInt() && cjvp.getInt() <= 52000)?"250":(cjvp.getInt() <= 1200)?"30":"0";
        tensionserie += (cjvs.getInt()>1200)?"/15":"/1.2";
        nba += (cjvs.getInt()<=1200)?"/30":(7000 <= cjvs.getInt() && cjvs.getInt() <= 15000)?"/95":"0";
        cjtensionSerie.setText(tensionserie);
        cjnba.setText(nba);
    }
    
    public void HallarConexionYPolaridad() {
        if (comboFase.getSelectedIndex() == 0) {
            comboGrupoConexion .setSelectedItem((cjvp.getInt()<=8000)?"Ii6":"Ii0");
            comboPolaridad.setSelectedIndex((cjvp.getInt()<=8000)?1:0);
        } else if (comboFase.getSelectedIndex() == 1) {
            comboGrupoConexion .setSelectedItem("DYn5");
            comboPolaridad.setSelectedIndex(0);
        }
    }
    
    void HallarCorrientes(){
        try{
            cji1.setText(String.valueOf(QD(((cjkva.getDouble() * 1000) / ((comboFase.getSelectedIndex()==0)?1:Math.sqrt(3)) ) / cjvp.getInt(), 2)));
            cji2.setText(String.valueOf(QD(((cjkva.getDouble() * 1000) / ((comboFase.getSelectedIndex()==0)?1:Math.sqrt(3)) ) / cjvs.getInt(), 2)));
        }catch(Exception e){
            Logger.getLogger(PROTOS.class.getName()).log(Level.SEVERE, null, e);
        }        
    }
    
    void HallarPromedioResistencias(){
        cjproresalta.setText(""+QD((cjuv.getDouble()+cjwu.getDouble()+cjvw.getDouble())/((comboFase.getSelectedIndex()==1)?3:1), 2));
        cjproresbaja.setText(""+QD((cjxy.getDouble()+cjyz.getDouble()+cjzx.getDouble())/((comboFase.getSelectedIndex()==1)?3:1), 2));
    }
    
    void HallarPromedioCorrientes(){
        if(comboFase.getSelectedIndex()==0){
            cjpromedioi.setText(String.valueOf(QD((cjiu.getDouble() / cji2.getDouble()) * 100, 2)));
        }else{
            cjpromedioi.setText(String.valueOf(QD((((cjiu.getDouble() + cjiv.getDouble() + cjiw.getDouble()) / 3) / cji2.getDouble()) * 100, 2)));
        }        
    }
    
    double I2R(){
        if(comboFase.getSelectedIndex()==0){
            cji2r.setText(""+QD(((Math.pow(cji1.getDouble(), 2) * cjproresalta.getDouble()) + (Math.pow(cji2.getDouble(), 2) * (cjproresbaja.getDouble() / 1000))), 2));
            return ((Math.pow(cji1.getDouble(), 2) * cjproresalta.getDouble()) + (Math.pow(cji2.getDouble(), 2) * (cjproresbaja.getDouble() / 1000)));
        }else{
            cji2r.setText(""+QD(1.5 * ((Math.pow(cji1.getDouble(), 2) * cjproresalta.getDouble()) + (Math.pow(cji2.getDouble(), 2) * (cjproresbaja.getDouble() / 1000))), 2));
            return 1.5 * ((Math.pow(cji1.getDouble(), 2) * cjproresalta.getDouble()) + (Math.pow(cji2.getDouble(), 2) * (cjproresbaja.getDouble() / 1000)));
        }                
    }
    
    double I2R85(){
        cji2ra85.setText(String.valueOf(QD(I2R() * K(), 1)));
        return I2R() * K();
    }
    
    double R() {
        return cjpcumedido.getDouble() / (10 * cjkva.getDouble());
    }
    
    double R85() {
        return R() * K();
    }
    
    double Z() {
        cjimpedancia.setText(""+QD((cjvcc.getDouble() / cjvp.getDouble()) * 100, 3));
        return (cjvcc.getDouble() / cjvp.getDouble()) * 100;
    }
    
    public double Z85() {
        double Z85 = Math.sqrt((Math.pow(R85(), 2)) + (Math.pow(X(), 2)));
        cjimpedancia85.setText(String.valueOf(QD(Z85, 2)));
        return Z85;
    }
    
    double X() {
        return Math.sqrt((Math.pow(Z(), 2)) - (Math.pow(R(), 2)));        
    }
    
    double getkc(){
        return (comboMaterialAlta.getSelectedItem().toString().equalsIgnoreCase("COBRE")&& comboMaterialBaja.getSelectedItem().equals("COBRE"))?234.5:(comboMaterialAlta.getSelectedItem().toString().equalsIgnoreCase("ALUMINIO")&& comboMaterialBaja.getSelectedItem().equals("ALUMINIO"))?225:229;        
    }
    
    double K(){
        double K = 0;
        if(comboRefrigeracion.getSelectedIndex() < 2){
            K = (getkc() + 85) / (getkc() + Double.parseDouble(cjtemperatura.getText()));
        }else if (comboRefrigeracion.getSelectedIndex() == 2){
            switch (comboClaseAislamiento.getSelectedIndex()){
                case 1:
                    K = (getkc() + 75) / (getkc() + Double.parseDouble(cjtemperatura.getText()));
                    break;
                case 2:
                    K = (getkc() + 85) / (getkc() + Double.parseDouble(cjtemperatura.getText()));
                    break;
                case 3:
                    K = (getkc() + 100) / (getkc() + Double.parseDouble(cjtemperatura.getText()));
                    break;
                case 4:
                    K = (getkc() + 120) / (getkc() + Double.parseDouble(cjtemperatura.getText()));
                    break;
                case 5:
                    K = (getkc() + 145) / (getkc() + Double.parseDouble(cjtemperatura.getText()));
                    break;
            }
        }
        return K;
    }
    
    public void CargarTablas() {
        try {
            tablaUno.getModel().removeTableModelListener(listenerTablaUno);
            for (int i = 0; i < 5; i++){
                tablaUno.setValueAt("Posic: "+(i+1), i, 0);
                tablaUno.setValueAt(0, i, 2);
                tablaUno.setValueAt(0, i, 3);
                tablaUno.setValueAt(0, i, 4);
            }
            tablaUno.setValueAt((conmutador.getSelectedIndex()==1)?Math.round(cjvp.getInt() * 1.025):Math.round(cjvp.getInt() * 1.05), 0, 1);
            tablaUno.setValueAt((conmutador.getSelectedIndex()==1)?Math.round(cjvp.getInt() * 1.00):Math.round(cjvp.getInt() * 1.025), 1, 1);
            tablaUno.setValueAt((conmutador.getSelectedIndex()==1)?Math.round(cjvp.getInt() * 0.975):Math.round(cjvp.getInt() * 1.0), 2, 1);
            tablaUno.setValueAt((conmutador.getSelectedIndex()==1)?Math.round(cjvp.getInt() * 0.95):Math.round(cjvp.getInt() * 0.975), 3, 1);
            tablaUno.setValueAt((conmutador.getSelectedIndex()==1)?Math.round(cjvp.getInt() * 0.925):Math.round(cjvp.getInt() * 0.95), 4, 1);            
            tablaUno.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            tablaUno.setCellSelectionEnabled(true);

            for (int i = 0; i < tablaUno.getRowCount(); i++) {
                tablaDos.setValueAt(QD((Integer.parseInt(tablaUno.getValueAt(i, 1).toString()) * ((comboFase.getSelectedIndex()==0)?1:Math.sqrt(3)) ) / cjvs.getInt(), 3), i, 0);
                tablaDos.setValueAt(QD((Integer.parseInt(tablaUno.getValueAt(i, 1).toString()) * ((comboFase.getSelectedIndex()==0)?1:Math.sqrt(3)) ) / cjvs.getInt(), 3) * 0.995, i, 1);
                tablaDos.setValueAt(QD((Integer.parseInt(tablaUno.getValueAt(i, 1).toString()) * ((comboFase.getSelectedIndex()==0)?1:Math.sqrt(3)) ) / cjvs.getInt(), 3) * 1.005, i, 2);
            }
            tablaDos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            tablaDos.setCellSelectionEnabled(true);
            listenerTablaUno = new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    if(e.getType() == TableModelEvent.UPDATE){
                        if(e.getColumn()>1){
                            if(alertas==null || alertas.getState()==java.lang.Thread.State.TERMINATED){
                                alertas = new Hilofases(e.getFirstRow(), e.getColumn());
                                alertas.start();
                            }
                        }
                    }
                }
            };
            tablaUno.getModel().addTableModelListener(listenerTablaUno);
        } catch (Exception e) {
            Logger.getLogger(PROTOS.class.getName()).log(Level.SEVERE, null, e);
        }      
    }
    
    double QD(double n, double d){
        return Math.round(n * Math.pow(10, d)) / Math.pow(10, d);
    }
    
    void habilitarCampos(boolean ver){
        cjwu.setEnabled(ver);
        cjvw.setEnabled(ver);
        cjyz.setEnabled(ver);
        cjzx.setEnabled(ver);
        cjiv.setEnabled(ver);
        cjiw.setEnabled(ver);
    }
    
    double PCU85() {
        cjpcua85.setText(String.valueOf(QD( ((cjpcumedido.getDouble() - I2R()) / K()) + I2R85(), 1)));
        return QD( ((cjpcumedido.getDouble() - I2R()) / K()) + I2R85(), 1);
    }
    
    public void HallarReg() {                                   
        double REG = QD(Math.sqrt(R85() + Math.pow(X(), 2) + (200 * R85() * 0.8) + (200 * X() * 0.6) + 10000) - 100, 2);
        REG = Math.pow(R85(), 2) + Math.pow(X(), 2) + 200 * R85() * 0.8 + 200 * X() * 0.6 + 10000;
        REG = Math.sqrt(REG);
        REG = REG - 100;
        REG = QD(REG, 2);
        cjreg.setText(String.valueOf(REG));       
    }
    
    void HallarEf(){
        cjef.setText(String.valueOf(QD((0.8 * cjkva.getDouble() * Math.pow(10, 5)) / (0.8 * cjkva.getDouble() * Math.pow(10, 3) + cjpomedido.getDouble() + PCU85()), 2)));
    }
    
    void cargarValores(){
        String servicio = comboServicio.getSelectedItem().toString(), tabla = null;
        int ano = cjano.getInt();
        int vp = cjvp.getInt();
        int fase = Integer.parseInt(comboFase.getSelectedItem().toString());
        if("NUEVO".equals(servicio)||"RECONSTRUIDO".equals(servicio) || cjcliente.getText().equals("EMPRESAS PUBLICAS DE MEDELLIN S.A E.S.P")){
            tabla = (vp<=15000)?(fase==1)?"monofasiconuevo":"trifasiconuevo":(vp > 15000 && vp <= 35000)?(fase==1)?"monofasiconuevoserie35":"trifasiconuevoserie35":null;
        }else if(servicio.equals("REPARACION")){
            if(vp <= 15000){
                tabla = (fase==1)?(ano < 1996)?"monofasicoantesde1996":"monofasicodespuesde1996":(ano < 1996)?"trifasicoantesde1996":"trifasicodespuesde1996";
            }else if (vp > 15000 && vp <= 35000){
                tabla = (fase==1)?(ano >= 1996)?"monofasicoantesde1996serie35":"monofasicodespuesde1996serie35":(ano < 1996)?"trifasicoantesde1996serie35":"trifasicodespuesde1996serie46";
            }else if(vp > 35000 && vp <= 46000 && fase==3 ){
                tabla = "trifasicodespuesde1996serie46";
            }
        }else if(servicio.equals("MANTENIMIENTO")){
            tabla = ESTADO_TRAFO.equals("REPARADO")?(ano < 1996)?(fase == 1)?"monofasicoantesde1996":"trifasicoantesde1996":(fase == 1)?"monofasicodespuesde1996":"trifasicodespuesde1996":(fase == 1)?"monofasiconuevo":"trifasiconuevo";
        }
        if(comboRefrigeracion.getSelectedIndex() == 2){
            tabla = (fase==3&&(vp > 1200 && vp <= 15000) )?"trifasicosecoserie1512":"trifasicosecoserie1212";
        }        
        double kva = getKva(tabla, cjkva.getDouble());
        String sql = "SELECT * FROM " + tabla + " WHERE kva=" + kva;
        conex.conectar();
        ResultSet rs = conex.CONSULTAR(sql);
        try{
            if(rs.next()){
                cjiogarantizado.setText(""+rs.getDouble("io"));
                cjpogarantizado.setText(""+rs.getDouble("po"));
                cjpcugarantizado.setText(""+rs.getInt("pc"));
                cjimpedanciagarantizado.setText(""+rs.getDouble("uz"));
            }
        }catch(SQLException ex){
            Logger.getLogger(PROTOS.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            conex.CERRAR();
        }
    }
    
    public static double getKva(String tabla, double KVAdigitada){
        double kva = 0;
        String sql = "select * from " + tabla + " ORDER BY kva ASC";
        ConexionBD conex = new ConexionBD();
        conex.conectar();
        ResultSet rs = conex.CONSULTAR(sql);
        boolean esta = false;
        try {
            double BD, auxiliar = 0;
            while (rs.next()) {
                System.out.println(rs.getDouble(1));
                BD = rs.getDouble("kva");
                if (BD == KVAdigitada) {
                    kva = BD;
                    esta = true;
                    break;
                } else if (BD < KVAdigitada) {
                    auxiliar = BD;
                } else if(KVAdigitada < ((auxiliar + BD) / 2)) {
                    kva = auxiliar;
                    esta = true;
                    break;
                } else {
                    kva = BD;
                    esta = true;
                    break;
                }
            }
            if (!esta) {
                JOptionPane.showMessageDialog(null, "NO SE ENCONTRO LA POTENCIA DE " + KVAdigitada + " EN LA TABLA " + tabla);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PROTOS.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error al buscar el kva mas adecuado\n" + ex);
        } finally {
            conex.CERRAR();
        }
        return kva;
    }
    
    void cargarMedidas() {
        double kva = cjkva.getDouble();        
        cjancho.setText((comboFase.getSelectedIndex()==0)?((kva==3)?"250":(kva==5)?"280":(kva==10)?"320":(kva==15)?"350":(kva==25)?"380":(kva==37.5)?"420":(kva==50)?"450":(kva==75)?"480":"0"):((kva==15)?"350":(kva==30)?"500":(kva==45)?"600":(kva==75)?"620":(kva==112.5)?"700":(kva==150)?"750":(kva==225)?"800":"0"));
        cjlargo.setText((comboFase.getSelectedIndex()==0)?((kva==3)?"250":(kva==5)?"280":(kva==10)?"320":(kva==15)?"350":(kva==25)?"380":(kva==37.5)?"420":(kva==50)?"450":(kva==75)?"480":"0"):((kva==15)?"280":(kva==30)?"320":(kva==45)?"350":(kva==75)?"380":(kva==112.5)?"430":(kva==150)?"480":(kva==225)?"520":"0"));
        cjalto.setText((comboFase.getSelectedIndex()==0)?((kva==3)?"450":(kva==5)?"500":(kva==10)?"550":(kva==15)?"550":(kva==25)?"550":(kva==37.5)?"600":(kva==50)?"650":(kva==75)?"700":"0"):((kva==15)?"500":(kva==30)?"500":(kva==45)?"550":(kva==75)?"600":(kva==112.5)?"650":(kva==150)?"700":(kva==225)?"750":"0"));
        cjelementos.setText((comboFase.getSelectedIndex()==0)?((kva==50)?"6":(kva==75)?"8":"0"):((kva==75)?"6":(kva==112.5)?"10":(kva==150)?"14":(kva==225)?"18":"0"));
        cjlargoelemento.setText((comboFase.getSelectedIndex()==0)?((kva==50||kva==75)?"300":"0"):((kva==75)?"300":(kva==112.5)?"380":(kva==150)?"300":(kva==225)?"300":"0"));
        cjanchoelemento.setText((comboFase.getSelectedIndex()==0)?((kva==50)?"480":(kva==75)?"480":"0"):((kva==75)?"480":(kva==112.5)?"380":(kva==150)?"480":(kva==225)?"480":"0"));
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cjserie = new CompuChiqui.JTextFieldPopup();
        jLabel4 = new javax.swing.JLabel();
        cjempresa = new CompuChiqui.JTextFieldPopup();
        jLabel5 = new javax.swing.JLabel();
        cjmarca = new CompuChiqui.JTextFieldPopup();
        jLabel6 = new javax.swing.JLabel();
        cjkva = new CompuChiqui.JTextFieldPopup();
        jLabel8 = new javax.swing.JLabel();
        comboFase = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        cjano = new CompuChiqui.JTextFieldPopup();
        jLabel9 = new javax.swing.JLabel();
        cjvp = new CompuChiqui.JTextFieldPopup();
        jLabel10 = new javax.swing.JLabel();
        cjvs = new CompuChiqui.JTextFieldPopup();
        jLabel1 = new javax.swing.JLabel();
        comboServicio = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        comboFrecuencia = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        comboRefrigeracion = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        cjtensionSerie = new CompuChiqui.JTextFieldPopup();
        jLabel13 = new javax.swing.JLabel();
        cjnba = new CompuChiqui.JTextFieldPopup();
        jLabel14 = new javax.swing.JLabel();
        cjcalentamientodevanado = new CompuChiqui.JTextFieldPopup();
        jLabel15 = new javax.swing.JLabel();
        comboClaseAislamiento = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        cjaltdiseno = new CompuChiqui.JTextFieldPopup();
        jLabel17 = new javax.swing.JLabel();
        cji1 = new CompuChiqui.JTextFieldPopup();
        jLabel18 = new javax.swing.JLabel();
        cji2 = new CompuChiqui.JTextFieldPopup();
        jLabel19 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel29 = new javax.swing.JLabel();
        cjtemperatura = new CompuChiqui.JTextFieldPopup();
        jLabel30 = new javax.swing.JLabel();
        conmutador = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        comboAceite = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        comboReferenciaAceite = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        cjRuptura = new CompuChiqui.JTextFieldPopup();
        jLabel23 = new javax.swing.JLabel();
        cjMetodo = new CompuChiqui.JTextFieldPopup();
        jPanel4 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        cjtiemporalt = new CompuChiqui.JTextFieldPopup();
        jLabel25 = new javax.swing.JLabel();
        comboTensionPrueba = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        cjATcontraBT = new CompuChiqui.JTextFieldPopup();
        jLabel27 = new javax.swing.JLabel();
        cjATcontraTierra = new CompuChiqui.JTextFieldPopup();
        jLabel28 = new javax.swing.JLabel();
        cjBTcontraTierra = new CompuChiqui.JTextFieldPopup();
        jPanel5 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        comboGrupoConexion = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        comboPolaridad = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        cjuv = new CompuChiqui.JTextFieldPopup();
        jLabel36 = new javax.swing.JLabel();
        cjxy = new CompuChiqui.JTextFieldPopup();
        jLabel37 = new javax.swing.JLabel();
        cjwu = new CompuChiqui.JTextFieldPopup();
        jLabel38 = new javax.swing.JLabel();
        cjyz = new CompuChiqui.JTextFieldPopup();
        jLabel39 = new javax.swing.JLabel();
        cjvw = new CompuChiqui.JTextFieldPopup();
        jLabel40 = new javax.swing.JLabel();
        cjzx = new CompuChiqui.JTextFieldPopup();
        jLabel41 = new javax.swing.JLabel();
        cjproresalta = new CompuChiqui.JTextFieldPopup();
        jLabel42 = new javax.swing.JLabel();
        cjproresbaja = new CompuChiqui.JTextFieldPopup();
        jLabel43 = new javax.swing.JLabel();
        comboMaterialAlta = new javax.swing.JComboBox<>();
        jLabel44 = new javax.swing.JLabel();
        comboMaterialBaja = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        cjBTcontraATyTierra = new CompuChiqui.JTextFieldPopup();
        jLabel46 = new javax.swing.JLabel();
        cjATcontraBTyTierra = new CompuChiqui.JTextFieldPopup();
        jLabel47 = new javax.swing.JLabel();
        cjtiempoaplicado = new CompuChiqui.JTextFieldPopup();
        jLabel48 = new javax.swing.JLabel();
        cjtensionBT = new CompuChiqui.JTextFieldPopup();
        jLabel49 = new javax.swing.JLabel();
        cjFrecuenciaInducida = new CompuChiqui.JTextFieldPopup();
        jLabel50 = new javax.swing.JLabel();
        cjtiempoInducido = new CompuChiqui.JTextFieldPopup();
        jPanel8 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        cjTensionBT2 = new CompuChiqui.JTextFieldPopup();
        jLabel52 = new javax.swing.JLabel();
        cjiu = new CompuChiqui.JTextFieldPopup();
        jLabel53 = new javax.swing.JLabel();
        cjiv = new CompuChiqui.JTextFieldPopup();
        jLabel54 = new javax.swing.JLabel();
        cjiw = new CompuChiqui.JTextFieldPopup();
        jLabel55 = new javax.swing.JLabel();
        cjpromedioi = new CompuChiqui.JTextFieldPopup();
        jLabel56 = new javax.swing.JLabel();
        cjiogarantizado = new CompuChiqui.JTextFieldPopup();
        jLabel57 = new javax.swing.JLabel();
        cjpomedido = new CompuChiqui.JTextFieldPopup();
        jLabel58 = new javax.swing.JLabel();
        cjpogarantizado = new CompuChiqui.JTextFieldPopup();
        jPanel9 = new javax.swing.JPanel();
        jLabel59 = new javax.swing.JLabel();
        cjvcc = new CompuChiqui.JTextFieldPopup();
        jLabel60 = new javax.swing.JLabel();
        cjpcumedido = new CompuChiqui.JTextFieldPopup();
        jLabel61 = new javax.swing.JLabel();
        cjpcua85 = new CompuChiqui.JTextFieldPopup();
        jLabel62 = new javax.swing.JLabel();
        cjpcugarantizado = new CompuChiqui.JTextFieldPopup();
        jLabel63 = new javax.swing.JLabel();
        cji2r = new CompuChiqui.JTextFieldPopup();
        jLabel64 = new javax.swing.JLabel();
        cji2ra85 = new CompuChiqui.JTextFieldPopup();
        jLabel65 = new javax.swing.JLabel();
        cjimpedancia = new CompuChiqui.JTextFieldPopup();
        jLabel66 = new javax.swing.JLabel();
        cjimpedancia85 = new CompuChiqui.JTextFieldPopup();
        jLabel67 = new javax.swing.JLabel();
        cjimpedanciagarantizado = new CompuChiqui.JTextFieldPopup();
        jPanel10 = new javax.swing.JPanel();
        jLabel78 = new javax.swing.JLabel();
        cjreg = new CompuChiqui.JTextFieldPopup();
        jLabel79 = new javax.swing.JLabel();
        cjef = new CompuChiqui.JTextFieldPopup();
        jPanel12 = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        cjmasa = new CompuChiqui.JTextFieldPopup();
        jLabel69 = new javax.swing.JLabel();
        cjaceite = new CompuChiqui.JTextFieldPopup();
        jLabel70 = new javax.swing.JLabel();
        cjlargo = new CompuChiqui.JTextFieldPopup();
        jLabel71 = new javax.swing.JLabel();
        cjancho = new CompuChiqui.JTextFieldPopup();
        jLabel72 = new javax.swing.JLabel();
        cjalto = new CompuChiqui.JTextFieldPopup();
        jLabel73 = new javax.swing.JLabel();
        cjcolor = new CompuChiqui.JTextFieldPopup();
        jLabel74 = new javax.swing.JLabel();
        cjespesor = new CompuChiqui.JTextFieldPopup();
        jLabel75 = new javax.swing.JLabel();
        cjelementos = new CompuChiqui.JTextFieldPopup();
        jLabel76 = new javax.swing.JLabel();
        cjlargoelemento = new CompuChiqui.JTextFieldPopup();
        jLabel77 = new javax.swing.JLabel();
        cjanchoelemento = new CompuChiqui.JTextFieldPopup();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaUno = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaDos = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        cjobservaciones = new CompuChiqui.JCTextArea();
        cjcliente = new CompuChiqui.JTextFieldPopup();
        cjlote = new CompuChiqui.JTextFieldPopup();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        cjfechasalida = new com.toedter.calendar.JDateChooser();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        subMenuItemRecalcular = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Informacion General", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Enter Sansman", 0, 10))); // NOI18N
        jPanel2.setLayout(new java.awt.GridLayout(21, 2, 0, 2));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Serie N°:");
        jPanel2.add(jLabel3);

        cjserie.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjserie.setText("2014110679");
        cjserie.setCampodetexto(cjATcontraBT);
        cjserie.setPreferredSize(new java.awt.Dimension(100, 20));
        cjserie.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cjserieKeyPressed(evt);
            }
        });
        jPanel2.add(cjserie);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("N° Empresa:");
        jPanel2.add(jLabel4);

        cjempresa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjempresa.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel2.add(cjempresa);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Marca:");
        jPanel2.add(jLabel5);

        cjmarca.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjmarca.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel2.add(cjmarca);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("KVA:");
        jPanel2.add(jLabel6);

        cjkva.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjkva.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel2.add(cjkva);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Fases:");
        jPanel2.add(jLabel8);

        comboFase.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "3" }));
        comboFase.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboFaseItemStateChanged(evt);
            }
        });
        jPanel2.add(comboFase);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Año:");
        jPanel2.add(jLabel7);

        cjano.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjano.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel2.add(cjano);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Volt. Primario:");
        jPanel2.add(jLabel9);

        cjvp.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjvp.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel2.add(cjvp);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Volt. Secund.:");
        jPanel2.add(jLabel10);

        cjvs.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjvs.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel2.add(cjvs);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Servicio:");
        jPanel2.add(jLabel1);

        comboServicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NUEVO", "RECONSTRUIDO", "REPARACION", "MANTENIMIENTO" }));
        comboServicio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboServicioItemStateChanged(evt);
            }
        });
        jPanel2.add(comboServicio);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Frecuencia:");
        jPanel2.add(jLabel2);

        comboFrecuencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "60", "50" }));
        jPanel2.add(comboFrecuencia);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Refrigeracion:");
        jPanel2.add(jLabel11);

        comboRefrigeracion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ONAN", "ONAF", "AN" }));
        jPanel2.add(comboRefrigeracion);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Tension Serie:");
        jPanel2.add(jLabel12);

        cjtensionSerie.setEditable(false);
        cjtensionSerie.setBackground(new java.awt.Color(255, 255, 255));
        cjtensionSerie.setForeground(new java.awt.Color(0, 102, 255));
        cjtensionSerie.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(cjtensionSerie);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("NBA AT/BT:");
        jPanel2.add(jLabel13);

        cjnba.setEditable(false);
        cjnba.setBackground(new java.awt.Color(255, 255, 255));
        cjnba.setForeground(new java.awt.Color(0, 102, 255));
        cjnba.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(cjnba);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Calent. Devn:");
        jPanel2.add(jLabel14);

        cjcalentamientodevanado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjcalentamientodevanado.setText("65");
        jPanel2.add(cjcalentamientodevanado);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Clase Aislam.:");
        jPanel2.add(jLabel15);

        comboClaseAislamiento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ao", "A:(75°C)", "A:(85°C)", "A:(100°C)", "A:(120°C)", "A:(145°C)" }));
        jPanel2.add(comboClaseAislamiento);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Alt Diseño:");
        jPanel2.add(jLabel16);

        cjaltdiseno.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjaltdiseno.setText("1000 MSNM");
        jPanel2.add(cjaltdiseno);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("I1:");
        jPanel2.add(jLabel17);

        cji1.setEditable(false);
        cji1.setBackground(new java.awt.Color(255, 255, 255));
        cji1.setForeground(new java.awt.Color(0, 102, 255));
        cji1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(cji1);

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("I2:");
        jPanel2.add(jLabel18);

        cji2.setEditable(false);
        cji2.setBackground(new java.awt.Color(255, 255, 255));
        cji2.setForeground(new java.awt.Color(0, 102, 255));
        cji2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(cji2);

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Deriv. Prim:");
        jPanel2.add(jLabel19);

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "(-4)*2.5%", "(+1-3)*2.5%", "(+2-2)*2.5%", "(+3-1)*2.5%", "(+4)*2.5%" }));
        jPanel2.add(jComboBox6);

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Temperatura:");
        jPanel2.add(jLabel29);

        cjtemperatura.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjtemperatura.setText("30");
        cjtemperatura.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel2.add(cjtemperatura);

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Conmutador:");
        jPanel2.add(jLabel30);

        conmutador.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5" }));
        conmutador.setSelectedIndex(1);
        jPanel2.add(conmutador);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "1) Liquido Aislante", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Enter Sansman", 0, 10))); // NOI18N
        jPanel3.setToolTipText("Liquido Aislante");
        jPanel3.setLayout(new java.awt.GridLayout(4, 2, 0, 2));

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Aceite:");
        jPanel3.add(jLabel20);

        comboAceite.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MINERAL", "VEGETAL\t" }));
        jPanel3.add(comboAceite);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Referencia:");
        jPanel3.add(jLabel21);

        comboReferenciaAceite.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "HYVOLT", "LUB TROIL TIPO II", "FR3" }));
        jPanel3.add(comboReferenciaAceite);

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Ruptura(kv):");
        jPanel3.add(jLabel22);

        cjRuptura.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjRuptura.setText("40");
        cjRuptura.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel3.add(cjRuptura);

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("Metodo:");
        jPanel3.add(jLabel23);

        cjMetodo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjMetodo.setText("ASTM 877");
        cjMetodo.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel3.add(cjMetodo);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "2) Resist. Aislamiento:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Enter Sansman", 0, 10))); // NOI18N
        jPanel4.setToolTipText("Resistencia de Aislamiento");
        jPanel4.setLayout(new java.awt.GridLayout(5, 2, 0, 2));

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Tiempo:");
        jPanel4.add(jLabel24);

        cjtiemporalt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjtiemporalt.setText("60");
        cjtiemporalt.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel4.add(cjtiemporalt);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("Tens. Prueba:");
        jPanel4.add(jLabel25);

        comboTensionPrueba.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "5000/500", "5000/5000", "500/500" }));
        jPanel4.add(comboTensionPrueba);

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("AT contra BT:");
        jPanel4.add(jLabel26);

        cjATcontraBT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjATcontraBT.setAbajo(cjATcontraBTyTierra);
        cjATcontraBT.setArriba(comboTensionPrueba);
        cjATcontraBT.setCampodetexto(cjATcontraTierra);
        cjATcontraBT.setPreferredSize(new java.awt.Dimension(100, 20));
        cjATcontraBT.setValidar(true);
        jPanel4.add(cjATcontraBT);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("AT contra Tierra:");
        jPanel4.add(jLabel27);

        cjATcontraTierra.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjATcontraTierra.setAbajo(null);
        cjATcontraTierra.setArriba(cjATcontraBT);
        cjATcontraTierra.setCampodetexto(cjBTcontraTierra);
        cjATcontraTierra.setPreferredSize(new java.awt.Dimension(100, 20));
        cjATcontraTierra.setValidar(true);
        jPanel4.add(cjATcontraTierra);

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("BT contra Tierra:");
        jPanel4.add(jLabel28);

        cjBTcontraTierra.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjBTcontraTierra.setCampodetexto(null);
        cjBTcontraTierra.setPreferredSize(new java.awt.Dimension(100, 20));
        cjBTcontraTierra.setValidar(true);
        cjBTcontraTierra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cjBTcontraTierraKeyPressed(evt);
            }
        });
        jPanel4.add(cjBTcontraTierra);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "3) Relacion de Transformacion", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Enter Sansman", 0, 10))); // NOI18N
        jPanel5.setToolTipText("Relacion de Transformacion");
        jPanel5.setLayout(new java.awt.GridLayout(2, 2, 0, 2));

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Grupo de Conexion:");
        jPanel5.add(jLabel31);

        comboGrupoConexion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ii6", "Ii0", "DYn5", "DYn11", "Yy0", "Yy6" }));
        jPanel5.add(comboGrupoConexion);

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("Polaridad:");
        jPanel5.add(jLabel32);

        comboPolaridad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SUSTRACTIVA", "ADITIVA", "NO APLICA" }));
        jPanel5.add(comboPolaridad);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "4) Resistencia Entre Terminales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Enter Sansman", 0, 10))); // NOI18N
        jPanel6.setToolTipText("Resistencia Entre Terminales");
        jPanel6.setLayout(new java.awt.GridLayout(5, 4, 0, 2));

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("U-V OHM:");
        jPanel6.add(jLabel35);

        cjuv.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjuv.setAbajo(cjwu);
        cjuv.setCampodetexto(null);
        cjuv.setPreferredSize(new java.awt.Dimension(100, 20));
        cjuv.setValidar(true);
        cjuv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cjuvKeyPressed(evt);
            }
        });
        jPanel6.add(cjuv);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("X-Y:");
        jPanel6.add(jLabel36);

        cjxy.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjxy.setAbajo(cjyz);
        cjxy.setCampodetexto(null);
        cjxy.setPreferredSize(new java.awt.Dimension(100, 20));
        cjxy.setValidar(true);
        cjxy.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cjxyKeyReleased(evt);
            }
        });
        jPanel6.add(cjxy);

        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel37.setText("W-U OHM:");
        jPanel6.add(jLabel37);

        cjwu.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjwu.setAbajo(cjvw);
        cjwu.setArriba(cjuv);
        cjwu.setCampodetexto(cjvw);
        cjwu.setPreferredSize(new java.awt.Dimension(100, 20));
        cjwu.setValidar(true);
        jPanel6.add(cjwu);

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel38.setText("Y-Z:");
        jPanel6.add(jLabel38);

        cjyz.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjyz.setAbajo(cjvcc);
        cjyz.setArriba(cjxy);
        cjyz.setCampodetexto(cjzx);
        cjyz.setPreferredSize(new java.awt.Dimension(100, 20));
        cjyz.setValidar(true);
        jPanel6.add(cjyz);

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("V-W OHM:");
        jPanel6.add(jLabel39);

        cjvw.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjvw.setArriba(cjwu);
        cjvw.setCampodetexto(cjxy);
        cjvw.setPreferredSize(new java.awt.Dimension(100, 20));
        cjvw.setValidar(true);
        jPanel6.add(cjvw);

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("Z-X:");
        jPanel6.add(jLabel40);

        cjzx.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjzx.setArriba(cjyz);
        cjzx.setCampodetexto(cjiu);
        cjzx.setPreferredSize(new java.awt.Dimension(100, 20));
        cjzx.setValidar(true);
        jPanel6.add(cjzx);

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Promedio:");
        jPanel6.add(jLabel41);

        cjproresalta.setForeground(new java.awt.Color(0, 102, 255));
        cjproresalta.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjproresalta.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel6.add(cjproresalta);

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText("Promedio:");
        jPanel6.add(jLabel42);

        cjproresbaja.setForeground(new java.awt.Color(0, 102, 255));
        cjproresbaja.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjproresbaja.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel6.add(cjproresbaja);

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("Material:");
        jPanel6.add(jLabel43);

        comboMaterialAlta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "COBRE", "ALUMINIO" }));
        jPanel6.add(comboMaterialAlta);

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("Material:");
        jPanel6.add(jLabel44);

        comboMaterialBaja.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "COBRE", "ALUMINIO" }));
        jPanel6.add(comboMaterialBaja);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "5) Ensayo de Aislamiento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Enter Sansman", 0, 10))); // NOI18N
        jPanel7.setToolTipText("Resistencia Entre Terminales");
        jPanel7.setLayout(new java.awt.GridLayout(6, 2, 0, 2));

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel45.setText("BT contra AT y Tierra:");
        jPanel7.add(jLabel45);

        cjBTcontraATyTierra.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjBTcontraATyTierra.setText("10");
        cjBTcontraATyTierra.setCampodetexto(cjATcontraBTyTierra);
        cjBTcontraATyTierra.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel7.add(cjBTcontraATyTierra);

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel46.setText("AT contra BT y TierraKv:");
        jPanel7.add(jLabel46);

        cjATcontraBTyTierra.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjATcontraBTyTierra.setText("34.5");
        cjATcontraBTyTierra.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel7.add(cjATcontraBTyTierra);

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setText("Tiempo de Prueba(s):");
        jPanel7.add(jLabel47);

        cjtiempoaplicado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjtiempoaplicado.setText("60");
        cjtiempoaplicado.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel7.add(cjtiempoaplicado);

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel48.setText("Tension BT:");
        jPanel7.add(jLabel48);

        cjtensionBT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjtensionBT.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel7.add(cjtensionBT);

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel49.setText("Frecuencia (Hz):");
        jPanel7.add(jLabel49);

        cjFrecuenciaInducida.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjFrecuenciaInducida.setText("414");
        cjFrecuenciaInducida.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel7.add(cjFrecuenciaInducida);

        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel50.setText("Timpo de Prueba:");
        jPanel7.add(jLabel50);

        cjtiempoInducido.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjtiempoInducido.setText("17");
        cjtiempoInducido.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel7.add(cjtiempoInducido);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "6) Ensayo Sin Carga", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Enter Sansman", 0, 10))); // NOI18N
        jPanel8.setToolTipText("Resistencia Entre Terminales");
        jPanel8.setLayout(new java.awt.GridLayout(8, 2, 0, 2));

        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel51.setText("Tension BT:");
        jPanel8.add(jLabel51);

        cjTensionBT2.setForeground(new java.awt.Color(0, 102, 255));
        cjTensionBT2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjTensionBT2.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel8.add(cjTensionBT2);

        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel52.setText("Iu:");
        jPanel8.add(jLabel52);

        cjiu.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjiu.setAbajo(cjiv);
        cjiu.setCampodetexto(null);
        cjiu.setPreferredSize(new java.awt.Dimension(100, 20));
        cjiu.setValidar(true);
        cjiu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cjiuKeyPressed(evt);
            }
        });
        jPanel8.add(cjiu);

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel53.setText("Iv:");
        jPanel8.add(jLabel53);

        cjiv.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjiv.setAbajo(cji2ra85);
        cjiv.setArriba(cjiu);
        cjiv.setCampodetexto(cjiw);
        cjiv.setPreferredSize(new java.awt.Dimension(100, 20));
        cjiv.setValidar(true);
        jPanel8.add(cjiv);

        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel54.setText("Iw:");
        jPanel8.add(jLabel54);

        cjiw.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjiw.setAbajo(cjpomedido);
        cjiw.setArriba(cjiv);
        cjiw.setCampodetexto(cjpomedido);
        cjiw.setPreferredSize(new java.awt.Dimension(100, 20));
        cjiw.setValidar(true);
        jPanel8.add(cjiw);

        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel55.setText("Promedio I(%):");
        jPanel8.add(jLabel55);

        cjpromedioi.setForeground(new java.awt.Color(0, 102, 255));
        cjpromedioi.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjpromedioi.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel8.add(cjpromedioi);

        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel56.setText("Io Garantizado:");
        jPanel8.add(jLabel56);

        cjiogarantizado.setForeground(new java.awt.Color(0, 102, 255));
        cjiogarantizado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjiogarantizado.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel8.add(cjiogarantizado);

        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel57.setText("Po Medido:");
        jPanel8.add(jLabel57);

        cjpomedido.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjpomedido.setCampodetexto(cjvcc);
        cjpomedido.setPreferredSize(new java.awt.Dimension(100, 20));
        cjpomedido.setValidar(true);
        jPanel8.add(cjpomedido);

        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel58.setText("Po Garantizado:");
        jPanel8.add(jLabel58);

        cjpogarantizado.setForeground(new java.awt.Color(0, 102, 255));
        cjpogarantizado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjpogarantizado.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel8.add(cjpogarantizado);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "7) Ensayo De Corto Circuito", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Enter Sansman", 0, 10))); // NOI18N
        jPanel9.setToolTipText("Resistencia Entre Terminales");
        jPanel9.setLayout(new java.awt.GridLayout(9, 2, 0, 2));

        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel59.setText("Vcc:");
        jPanel9.add(jLabel59);

        cjvcc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjvcc.setCampodetexto(cjpcumedido);
        cjvcc.setPreferredSize(new java.awt.Dimension(100, 20));
        cjvcc.setValidar(true);
        jPanel9.add(cjvcc);

        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel60.setText("Pcu Medido:");
        jPanel9.add(jLabel60);

        cjpcumedido.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjpcumedido.setCampodetexto(cjobservaciones);
        cjpcumedido.setPreferredSize(new java.awt.Dimension(100, 20));
        cjpcumedido.setValidar(true);
        cjpcumedido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cjpcumedidoKeyPressed(evt);
            }
        });
        jPanel9.add(cjpcumedido);

        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel61.setText("Pcu a 85°:");
        jPanel9.add(jLabel61);

        cjpcua85.setForeground(new java.awt.Color(0, 102, 255));
        cjpcua85.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjpcua85.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel9.add(cjpcua85);

        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel62.setText("Pcu Garantizado:");
        jPanel9.add(jLabel62);

        cjpcugarantizado.setForeground(new java.awt.Color(0, 102, 255));
        cjpcugarantizado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjpcugarantizado.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel9.add(cjpcugarantizado);

        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel63.setText("I2r:");
        jPanel9.add(jLabel63);

        cji2r.setForeground(new java.awt.Color(0, 102, 255));
        cji2r.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cji2r.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel9.add(cji2r);

        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel64.setText("I2r a 85°:");
        jPanel9.add(jLabel64);

        cji2ra85.setForeground(new java.awt.Color(0, 102, 255));
        cji2ra85.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cji2ra85.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel9.add(cji2ra85);

        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel65.setText("Impedancia Z (%):");
        jPanel9.add(jLabel65);

        cjimpedancia.setForeground(new java.awt.Color(0, 102, 255));
        cjimpedancia.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjimpedancia.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel9.add(cjimpedancia);

        jLabel66.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel66.setText("Impedancia 85° Z (%):");
        jPanel9.add(jLabel66);

        cjimpedancia85.setForeground(new java.awt.Color(0, 102, 255));
        cjimpedancia85.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjimpedancia85.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel9.add(cjimpedancia85);

        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel67.setText("Impendancia Garantizado:");
        jPanel9.add(jLabel67);

        cjimpedanciagarantizado.setForeground(new java.awt.Color(0, 102, 255));
        cjimpedanciagarantizado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjimpedanciagarantizado.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel9.add(cjimpedanciagarantizado);

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel10.setToolTipText("Resistencia Entre Terminales");
        jPanel10.setLayout(new java.awt.GridLayout(1, 4, 0, 2));

        jLabel78.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel78.setText("8) Regulacion:");
        jPanel10.add(jLabel78);

        cjreg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjreg.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel10.add(cjreg);

        jLabel79.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel79.setText("9) Eficiencia:");
        jPanel10.add(jLabel79);

        cjef.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjef.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel10.add(cjef);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "10) Caracteristicas Mecanicas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Enter Sansman", 0, 10))); // NOI18N
        jPanel12.setToolTipText("Resistencia Entre Terminales");
        jPanel12.setLayout(new java.awt.GridLayout(10, 2, 0, 2));

        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel68.setText("Masa(Kg):");
        jPanel12.add(jLabel68);

        cjmasa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjmasa.setCampodetexto(cjaceite);
        cjmasa.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel12.add(cjmasa);

        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel69.setText("Aceite(L):");
        jPanel12.add(jLabel69);

        cjaceite.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjaceite.setCampodetexto(cjlargo);
        cjaceite.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel12.add(cjaceite);

        jLabel70.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel70.setText("Largo:");
        jPanel12.add(jLabel70);

        cjlargo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjlargo.setCampodetexto(cjancho);
        cjlargo.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel12.add(cjlargo);

        jLabel71.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel71.setText("Ancho:");
        jPanel12.add(jLabel71);

        cjancho.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjancho.setCampodetexto(cjalto);
        cjancho.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel12.add(cjancho);

        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel72.setText("Alto:");
        jPanel12.add(jLabel72);

        cjalto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjalto.setCampodetexto(cjcolor);
        cjalto.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel12.add(cjalto);

        jLabel73.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel73.setText("Color:");
        jPanel12.add(jLabel73);

        cjcolor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjcolor.setCampodetexto(cjespesor);
        cjcolor.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel12.add(cjcolor);

        jLabel74.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel74.setText("Espesor:");
        jPanel12.add(jLabel74);

        cjespesor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjespesor.setCampodetexto(cjelementos);
        cjespesor.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel12.add(cjespesor);

        jLabel75.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel75.setText("N° Elementos:");
        jPanel12.add(jLabel75);

        cjelementos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjelementos.setCampodetexto(cjlargoelemento);
        cjelementos.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel12.add(cjelementos);

        jLabel76.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel76.setText("Largo Elemento:");
        jPanel12.add(jLabel76);

        cjlargoelemento.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjlargoelemento.setCampodetexto(cjanchoelemento);
        cjlargoelemento.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel12.add(cjlargoelemento);

        jLabel77.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel77.setText("Ancho Elemento:");
        jPanel12.add(jLabel77);

        cjanchoelemento.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjanchoelemento.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel12.add(cjanchoelemento);

        tablaUno.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "POSICION", "TENSION", "FASE U", "FASE V", "FASE W"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablaUno.setGridColor(new java.awt.Color(227, 227, 227));
        tablaUno.setRowHeight(20);
        tablaUno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tablaUnoKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(tablaUno);

        tablaDos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "NOMINAL", "MINIMA", "MAXIMA"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaDos.setGridColor(new java.awt.Color(227, 227, 227));
        tablaDos.setRowHeight(20);
        jScrollPane2.setViewportView(tablaDos);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Observaciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Enter Sansman", 0, 10))); // NOI18N
        jPanel11.setToolTipText("Resistencia Entre Terminales");

        cjobservaciones.setColumns(20);
        cjobservaciones.setRows(5);
        jScrollPane3.setViewportView(cjobservaciones);

        cjcliente.setEditable(false);
        cjcliente.setBackground(new java.awt.Color(255, 255, 255));
        cjcliente.setForeground(new java.awt.Color(0, 102, 255));
        cjcliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjcliente.setPreferredSize(new java.awt.Dimension(100, 20));

        cjlote.setEditable(false);
        cjlote.setBackground(new java.awt.Color(255, 255, 255));
        cjlote.setForeground(new java.awt.Color(0, 102, 255));
        cjlote.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cjlote.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabel33.setText("Cliente:");

        jLabel34.setText("Lote:");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjcliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjlote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cjcliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cjlote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addContainerGap(60, Short.MAX_VALUE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Fecha Salida Laboratorio", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Enter Sansman", 0, 10))); // NOI18N
        jPanel13.setToolTipText("Resistencia Entre Terminales");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cjfechasalida, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cjfechasalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(6, 6, 6)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Resultados y Ensayos", jPanel1);

        jMenu1.setText("Archivo");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Editar");

        subMenuItemRecalcular.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, java.awt.event.InputEvent.CTRL_MASK));
        subMenuItemRecalcular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/calculadora.png"))); // NOI18N
        subMenuItemRecalcular.setText("Recalcular");
        subMenuItemRecalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subMenuItemRecalcularActionPerformed(evt);
            }
        });
        jMenu2.add(subMenuItemRecalcular);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cjserieKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjserieKeyPressed
        if(evt.getKeyCode()==10){
            try{
                boolean mostrar = false;
                ResultSet rs = null;
                conex.conectar();
                rs = conex.CONSULTAR("SELECT numeroserie, count(*) FROM transformador t WHERE t.numeroserie='"+cjserie.getText()+"' GROUP BY numeroserie ");
                int total = 0;
                if(rs.next()){
                    total = rs.getInt("count");
                }else{
                    modelo.Metodos.M("NO SE ENCONTRÒ EL NUMERO DE SERIE DIGITADO", "advertencia.png");
                    return;
                }
                rs = conex.CONSULTAR("SELECT * FROM entrada e INNER JOIN transformador t USING(identrada) INNER JOIN cliente c USING (idcliente) WHERE t.numeroserie='"+cjserie.getText()+"'");
                if(total == 1){                    
                    mostrar = rs.next();
                }else if(total > 1){
                    DialogoTrafosRepetidos trafos = new DialogoTrafosRepetidos(this, rootPaneCheckingEnabled);
                    trafos.CargarDatos(rs);
                    trafos.setVisible(true);
                    rs = conex.CONSULTAR("SELECT * FROM entrada e INNER JOIN transformador t USING(identrada) INNER JOIN cliente c USING (idcliente) WHERE t.idtransformador='"+trafos.getIDTRAFO()+"'");
                    mostrar = rs.next();
                }
                if(mostrar){
                    cjcliente.setText(rs.getString("nombrecliente"));
                    cjlote.setText(rs.getString("lote"));
                    cjempresa.setText(rs.getString("numeroempresa"));
                    cjmarca.setText(rs.getString("marca"));
                    cjkva.setText(rs.getString("kvasalida"));
                    comboFase.setSelectedItem(rs.getString("fase"));
                    cjano.setText(rs.getString("ano"));
                    cjvp.setText(rs.getString("tps"));
                    cjvs.setText(rs.getString("tss"));
                    cjtensionBT.setText(String.valueOf(rs.getInt("tss")*2));
                    cjTensionBT2.setText(rs.getString("tss"));
                    comboServicio.setSelectedItem(rs.getString("serviciosalida"));
                    cjmasa.setText(rs.getString("peso"));
                    cjaceite.setText(rs.getString("aceite"));
                    CargarTablas();
                    habilitarCampos(rs.getString("fase").equals("3"));
                    subMenuItemRecalcular.doClick();
                }
            }catch(SQLException ex){
                modelo.Metodos.M("ERROR AL BUSCAR EL NUMERO DE SERIE\n"+ex, "error.png");
                Logger.getLogger(PROTOS.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                conex.CERRAR();
            }
        }
    }//GEN-LAST:event_cjserieKeyPressed

    private void subMenuItemRecalcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subMenuItemRecalcularActionPerformed
        cargarValores();
        HallarTensionSerie();
        HallarConexionYPolaridad();
        HallarCorrientes();
//        CargarTablas();
        HallarPromedioCorrientes();
        HallarPromedioResistencias();
        I2R();
        I2R85();
        Z85();
        HallarReg();
        HallarEf();        
        cargarMedidas();
    }//GEN-LAST:event_subMenuItemRecalcularActionPerformed

    private void tablaUnoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaUnoKeyTyped
        if(evt.getKeyChar()==10){
            if(comboFase.getSelectedIndex()==0){
                if(tablaUno.getSelectedRow()==0&&tablaUno.getSelectedColumn()==3){
                    cjuv.grabFocus();
                }
            }else{
                if(tablaUno.getSelectedRow()==0&&tablaUno.getSelectedColumn()==0){
                    cjuv.grabFocus();
                }
            }
        }
    }//GEN-LAST:event_tablaUnoKeyTyped

    private void cjuvKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjuvKeyPressed
        if(evt.getKeyCode()==10 && !cjuv.getText().isEmpty()){
            if(comboFase.getSelectedIndex()==0){
                cjxy.grabFocus();
            }else{
                cjwu.grabFocus();
            }
        }
    }//GEN-LAST:event_cjuvKeyPressed

    private void cjxyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjxyKeyReleased
        if(evt.getKeyCode()==10 && !cjxy.getText().isEmpty()){
            if(comboFase.getSelectedIndex()==0){
                cjiu.grabFocus();
            }else{
                cjyz.grabFocus();
            }
        }
    }//GEN-LAST:event_cjxyKeyReleased

    private void comboFaseItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboFaseItemStateChanged
        if(evt.getStateChange()==ItemEvent.DESELECTED){
            habilitarCampos((comboFase.getSelectedIndex()==1));
            subMenuItemRecalcular.doClick();
        }
    }//GEN-LAST:event_comboFaseItemStateChanged

    private void cjBTcontraTierraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjBTcontraTierraKeyPressed
        if(evt.getKeyCode()==10){
            tablaUno.setRowSelectionInterval(0, 0);
            tablaUno.setColumnSelectionInterval(2, 2);
            tablaUno.grabFocus();
        }
    }//GEN-LAST:event_cjBTcontraTierraKeyPressed

    private void cjiuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjiuKeyPressed
        if(evt.getKeyCode()==10){
            if(comboFase.getSelectedIndex()==0){
                cjpomedido.grabFocus();
            }else{
                cjiv.grabFocus();
            }
        }
    }//GEN-LAST:event_cjiuKeyPressed

    private void cjpcumedidoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cjpcumedidoKeyPressed
        if(evt.getKeyCode()==10){
            subMenuItemRecalcular.doClick();
        }
    }//GEN-LAST:event_cjpcumedidoKeyPressed

    private void comboServicioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboServicioItemStateChanged
        if(evt.getStateChange() == ItemEvent.DESELECTED){
            if("MANTENIMIENTO".equals(comboServicio.getSelectedItem().toString())){
                while(true){
                    int n = JOptionPane.showOptionDialog(this, "SELECCIONE EL ESTADO DEL TRANSFORMADOR", "Seleccione una opcion", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, modelo.Metodos.getIcon("advertencia.png"), new Object[]{"ORIGINAL","REPARADO"}, "ORIGINAL");
                    if(n>=0){
                        ESTADO_TRAFO = (n==0)?"ORIGINAL":"REPARADO";
                        break;
                    }
                }
            }
        }
    }//GEN-LAST:event_comboServicioItemStateChanged

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
            java.util.logging.Logger.getLogger(PROTOS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PROTOS().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private CompuChiqui.JTextFieldPopup cjATcontraBT;
    private CompuChiqui.JTextFieldPopup cjATcontraBTyTierra;
    private CompuChiqui.JTextFieldPopup cjATcontraTierra;
    private CompuChiqui.JTextFieldPopup cjBTcontraATyTierra;
    private CompuChiqui.JTextFieldPopup cjBTcontraTierra;
    private CompuChiqui.JTextFieldPopup cjFrecuenciaInducida;
    private CompuChiqui.JTextFieldPopup cjMetodo;
    private CompuChiqui.JTextFieldPopup cjRuptura;
    private CompuChiqui.JTextFieldPopup cjTensionBT2;
    private CompuChiqui.JTextFieldPopup cjaceite;
    private CompuChiqui.JTextFieldPopup cjaltdiseno;
    private CompuChiqui.JTextFieldPopup cjalto;
    private CompuChiqui.JTextFieldPopup cjancho;
    private CompuChiqui.JTextFieldPopup cjanchoelemento;
    private CompuChiqui.JTextFieldPopup cjano;
    private CompuChiqui.JTextFieldPopup cjcalentamientodevanado;
    private CompuChiqui.JTextFieldPopup cjcliente;
    private CompuChiqui.JTextFieldPopup cjcolor;
    private CompuChiqui.JTextFieldPopup cjef;
    private CompuChiqui.JTextFieldPopup cjelementos;
    private CompuChiqui.JTextFieldPopup cjempresa;
    private CompuChiqui.JTextFieldPopup cjespesor;
    private com.toedter.calendar.JDateChooser cjfechasalida;
    private CompuChiqui.JTextFieldPopup cji1;
    private CompuChiqui.JTextFieldPopup cji2;
    private CompuChiqui.JTextFieldPopup cji2r;
    private CompuChiqui.JTextFieldPopup cji2ra85;
    private CompuChiqui.JTextFieldPopup cjimpedancia;
    private CompuChiqui.JTextFieldPopup cjimpedancia85;
    private CompuChiqui.JTextFieldPopup cjimpedanciagarantizado;
    private CompuChiqui.JTextFieldPopup cjiogarantizado;
    private CompuChiqui.JTextFieldPopup cjiu;
    private CompuChiqui.JTextFieldPopup cjiv;
    private CompuChiqui.JTextFieldPopup cjiw;
    private CompuChiqui.JTextFieldPopup cjkva;
    private CompuChiqui.JTextFieldPopup cjlargo;
    private CompuChiqui.JTextFieldPopup cjlargoelemento;
    private CompuChiqui.JTextFieldPopup cjlote;
    private CompuChiqui.JTextFieldPopup cjmarca;
    private CompuChiqui.JTextFieldPopup cjmasa;
    private CompuChiqui.JTextFieldPopup cjnba;
    private CompuChiqui.JCTextArea cjobservaciones;
    private CompuChiqui.JTextFieldPopup cjpcua85;
    private CompuChiqui.JTextFieldPopup cjpcugarantizado;
    private CompuChiqui.JTextFieldPopup cjpcumedido;
    private CompuChiqui.JTextFieldPopup cjpogarantizado;
    private CompuChiqui.JTextFieldPopup cjpomedido;
    private CompuChiqui.JTextFieldPopup cjpromedioi;
    private CompuChiqui.JTextFieldPopup cjproresalta;
    private CompuChiqui.JTextFieldPopup cjproresbaja;
    private CompuChiqui.JTextFieldPopup cjreg;
    private CompuChiqui.JTextFieldPopup cjserie;
    private CompuChiqui.JTextFieldPopup cjtemperatura;
    private CompuChiqui.JTextFieldPopup cjtensionBT;
    private CompuChiqui.JTextFieldPopup cjtensionSerie;
    private CompuChiqui.JTextFieldPopup cjtiempoInducido;
    private CompuChiqui.JTextFieldPopup cjtiempoaplicado;
    private CompuChiqui.JTextFieldPopup cjtiemporalt;
    private CompuChiqui.JTextFieldPopup cjuv;
    private CompuChiqui.JTextFieldPopup cjvcc;
    private CompuChiqui.JTextFieldPopup cjvp;
    private CompuChiqui.JTextFieldPopup cjvs;
    private CompuChiqui.JTextFieldPopup cjvw;
    private CompuChiqui.JTextFieldPopup cjwu;
    private CompuChiqui.JTextFieldPopup cjxy;
    private CompuChiqui.JTextFieldPopup cjyz;
    private CompuChiqui.JTextFieldPopup cjzx;
    private javax.swing.JComboBox<String> comboAceite;
    private javax.swing.JComboBox<String> comboClaseAislamiento;
    private javax.swing.JComboBox<String> comboFase;
    private javax.swing.JComboBox<String> comboFrecuencia;
    private javax.swing.JComboBox<String> comboGrupoConexion;
    private javax.swing.JComboBox<String> comboMaterialAlta;
    private javax.swing.JComboBox<String> comboMaterialBaja;
    private javax.swing.JComboBox<String> comboPolaridad;
    private javax.swing.JComboBox<String> comboReferenciaAceite;
    private javax.swing.JComboBox<String> comboRefrigeracion;
    private javax.swing.JComboBox<String> comboServicio;
    private javax.swing.JComboBox<String> comboTensionPrueba;
    private javax.swing.JComboBox<String> conmutador;
    private javax.swing.JComboBox<String> jComboBox6;
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
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
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
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenuItem subMenuItemRecalcular;
    private javax.swing.JTable tablaDos;
    private javax.swing.JTable tablaUno;
    // End of variables declaration//GEN-END:variables

    private class Hilofases extends Thread{
        
        private int fila = -1;
        private int col = -1;
        
        public Hilofases(int row, int col) {
            this.fila = row;
            this.col = col;
        }
        
        @Override
        public void run(){
            while(true){
                System.out.println("CORRIENDO HILO");
                double fase = Double.parseDouble(tablaUno.getValueAt(getFila(), getCol()).toString());
                double minimo = Double.parseDouble(tablaDos.getValueAt(getFila(), 1).toString());
                double maximo = Double.parseDouble(tablaDos.getValueAt(getFila(), 2).toString());
                if(fase < minimo || fase > maximo){
                    try {
                        JTextField fileName = new JTextField(String.valueOf(fase));
                        Object[] message = {"INGRESE UN NUEVO VALOR.\n(MIN = "+minimo+" MAX = "+maximo+")", fileName};
                        int n =JOptionPane.showOptionDialog(rootPane, message, "VALOR FUERA DE RANGO", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,modelo.Metodos.getIcon("advertencia.png"), new Object[]{"ACEPTAR","OMITIR ERROR"}, "ACEPTAR");
                        if(n==1){
                            break;
                        }else if(n==0){
                            if(Double.parseDouble(fileName.getText()) >= minimo && Double.parseDouble(fileName.getText()) <= maximo){
                                tablaUno.setValueAt(Double.parseDouble(fileName.getText()), getFila(), getCol());
                                break;
                            }
                        }
                    } catch (HeadlessException | NumberFormatException ex){
                        modelo.Metodos.M("ERROR\n"+ex, "error.png");
                    }                                
                }else{
                    break;
                }
            }
        }       

        public int getFila() {
            return fila;
        }

        public void setFila(int fila) {
            this.fila = fila;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }
        
    }

}
