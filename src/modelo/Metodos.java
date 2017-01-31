package modelo;
 
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author AUXPLANTA
 */
public class Metodos {    
    
    public static void ERROR(Exception e, String mensaje){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        JOptionPane.showMessageDialog(null, mensaje+"\n"+sw.toString(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void generarExcel(JTable table, JProgressBar barra, JButton btn){
        (new Thread(){
            @Override
            public void run(){                
                try {
                    btn.setEnabled(false);
                    btn.setIcon(new ImageIcon(getClass().getResource("/recursos/images/gif.gif")));
                    XSSFWorkbook wb = new XSSFWorkbook();
                    FileOutputStream fileOut = null;            
                    fileOut = new FileOutputStream(table.getName()+".xlsx");
                    XSSFSheet hoja = wb.createSheet(table.getName()+".xlsx");
                    XSSFRow fila;
                    barra.setMaximum(table.getRowCount());
                    for(int i=0; i<=table.getRowCount(); i++){
                        
                        if(i<table.getRowCount()){
                            table.setRowSelectionInterval(i, i);
                            table.scrollRectToVisible(table.getCellRect(i, 0, true));
                        }
                        
                        barra.setValue(i);
                        fila = hoja.createRow(i);
                        
                        for(int j = 0; j < table.getColumnCount(); j++){
                            if(i==0){
                                fila.createCell(j).setCellValue(table.getColumnName(j));
                            }else{
                                fila.createCell(j).setCellValue((table.getValueAt(i-1, j)==null)?"0":table.getValueAt(i-1, j).toString());
                            }                            
                        }
                    }
                    for(int j = 0; j < table.getColumnCount(); j++) {
                        wb.getSheetAt(0).autoSizeColumn(j);
                    }
                    wb.write(fileOut);
                    fileOut.close();
                    Desktop.getDesktop().open(new File(table.getName()+".xlsx"));
                } catch (Exception ex) {
                    ERROR(ex, "ERROR AL EXPORTAR EL ARCHIVO EXCEL.");
                    Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, ex);
                }finally{
                    btn.setEnabled(true);
                    btn.setIcon(new ImageIcon(getClass().getResource("/recursos/images/excel.png")));
                    barra.setValue(0);
                }
            }
        }).start();        
    }
    
    public static void M(String m, String i) {
        JOptionPane.showMessageDialog(null, m, "Aviso", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(Metodos.class.getClass().getResource("/recursos/images/"+i)));
    }
    
    public static int getConsecutivoRemision(String empresa, boolean aumentar){
        int ID_REMISION = 0;
        ConexionBD conexion = new ConexionBD();
        try {
            conexion.conectar();
            if(aumentar){
                ResultSet rs = conexion.CONSULTAR("SELECT nextval('"+empresa+"') ");
                rs.next();
                ID_REMISION = rs.getInt("nextval");
            }else{
                ResultSet rs = conexion.CONSULTAR(" SELECT last_value FROM "+empresa);
                rs.next();
                ID_REMISION = rs.getInt("last_value");
            }
        }catch(SQLException ex){
            Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            conexion.CERRAR();
        }
        return ID_REMISION;
    }
    
    public static int getUltimoID(String tabla, String col){
        ConexionBD conexion = new ConexionBD();
        try {            
            conexion.conectar();
            ResultSet rs = conexion.CONSULTAR("SELECT MAX("+col+") FROM "+tabla+" ");
            rs.next();
            return rs.getInt("max");
        } catch (SQLException ex) {
            Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            conexion.CERRAR();
        }
        return -1;
    }
    
    public static void cargarCombo(String sql, JComboBox combo){
        ConexionBD conexion = new ConexionBD();
        conexion.conectar();
        ResultSet rs = conexion.CONSULTAR(sql);
        try {
            while(rs.next()){
                if(rs.getString(1) != null && !rs.getString(1).isEmpty()){
                    combo.addItem(rs.getString(1));
                }              
            }
        } catch (SQLException ex) {
            Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static BufferedImage byteToBufferedImage(byte[] bytes){
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException ex) {
            Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static byte[] BufferedImageToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream os =  new  ByteArrayOutputStream (); 
        ImageIO.write(image,  "png" , os ); 
        os.flush();
        return os.toByteArray();
    }
    
    public static BufferedImage imageToBufferedImage(Image img){
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null),
        BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();    
        return bufferedImage;
    }
    
    public static ArrayList getFirma(String cargo){
        ConexionBD con = new ConexionBD();
        con.conectar();
        ArrayList list = new ArrayList();
        ResultSet rs = con.CONSULTAR("SELECT firma, nombre FROM personal WHERE cargo='"+cargo+"' ");
        try {
            rs.next();        
            list.add(0, rs.getBytes("firma"));
            list.add(1, rs.getString("nombre"));
        } catch (SQLException ex) {
            Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            con.CERRAR();
        }
        return list;
    }
    
    public static Icon getIcon(String icono){
        return new ImageIcon(Metodos.class.getResource("/recursos/images/"+icono));
    }
    
}
