package Dialogos;

import JButtonIntoJTable.BotonEnColumna;
import JTableAutoResizeColumn.TableColumnAdjuster;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import modelo.ConexionBD;

/**
 *
 * @author AUXPLANTA
 */
public class DialogoRegistrarHerramienta extends javax.swing.JDialog {

    DefaultTableModel model;
    
    modelo.ConexionBD conex = new ConexionBD();
    
    TableColumnAdjuster ajustarColumna;
    
    public DialogoRegistrarHerramienta(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        ajustarColumna = new TableColumnAdjuster(tablaherramientas);
        
        Cargar();
        
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String herramienta  = cjherramienta.getText().toUpperCase().trim();
                    String tipo = cjtipo.getText().toUpperCase().trim();
                    String codigo = cjcodigo.getText().trim();
                    conex.conectar();
                    if(conex.GUARDAR("INSERT INTO herramienta_consorcio "
                            + "(nombre_herramienta,tipo_herramienta,codigo_herramienta) "
                            + "VALUES ( '"+herramienta+"' , '"+tipo+"' , '"+codigo+"' )")){
                        Cargar();
                        cjherramienta.setText(null);cjtipo.setText(null);
                        JOptionPane.showMessageDialog(null, "HERRAMIENTA GUARDA CON EXITO");
                        conex.CERRAR();
                    }
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "ERROR AL GUARDAR\n"+e);
                }
            }
        });
        
        //EVENTO MIENTRAS SE ESCRIBE EN EL CAMPO DE TEXTO PARA BUSCAR LAS HERRAMIENTAS
        cjbuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt){
                Cargar();
            }
        });
        
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               try{
                   int fila = tablaherramientas.getSelectedRow();                   
                   if(fila>=0){
                       int id = Integer.parseInt(tablaherramientas.getValueAt(fila, 1).toString());
                       conex.conectar();
                       if(conex.GUARDAR("DELETE FROM herramienta_consorcio WHERE idherramienta='"+id+"' ")){
                           Cargar();
                       }
                   }
               }catch(Exception ex){
                   JOptionPane.showMessageDialog(rootPane, "ERROR\n"+ex);
                   Logger.getLogger(DialogoRegistrarHerramienta.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
        });
        
        tablaherramientas.setDefaultRenderer(JButton.class, new BotonEnColumna());
        
    }
    
    void Cargar(){
        try {
            String data[][]={};
            String col[]={"ITEM","ID","HERRAMIENTA","TIPO","CODIGO"};
            model = new DefaultTableModel(data, col){
                @Override
                public boolean isCellEditable(int row, int col){                
                    if(col==2||col==3||col==4)
                        return true;
                    return false;
                }
            };
            tablaherramientas.setModel(model);
            tablaherramientas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            int row = 0;
            conex.conectar();
            ResultSet rs = conex.CONSULTAR("SELECT * FROM herramienta_consorcio WHERE nombre_herramienta ILIKE '%"+cjbuscar.getText()+"%' ORDER BY nombre_herramienta ");
            while(rs.next()){
                model.insertRow(row, new Object[]{});
                model.setValueAt(rs.getRow(), row, 0);
                model.setValueAt(rs.getInt("idherramienta"), row, 1);
                model.setValueAt(rs.getString("nombre_herramienta"), row, 2);
                model.setValueAt(rs.getString("tipo_herramienta"), row, 3);
                model.setValueAt(rs.getString("codigo_herramienta"), row, 4);
                row++;
            }
            ajustarColumna.adjustColumns();
            
            model.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    if(e.getType() == TableModelEvent.UPDATE){
                        int id = Integer.parseInt(tablaherramientas.getValueAt(e.getFirstRow(), 1).toString());
                        String nombre = tablaherramientas.getValueAt(e.getFirstRow(), 2).toString();
                        String tipo = tablaherramientas.getValueAt(e.getFirstRow(), 3).toString();
                        String codigo = tablaherramientas.getValueAt(e.getFirstRow(), 4).toString();
                        conex.conectar();
                        switch(e.getColumn()){
                            case 2:
                                if(conex.GUARDAR("UPDATE herramienta_consorcio SET nombre_herramienta='"+nombre+"' WHERE idherramienta='"+id+"' ")){
                                    
                                }
                                break;
                            case 3:
                                if(conex.GUARDAR("UPDATE herramienta_consorcio SET tipo_herramienta='"+tipo+"' WHERE idherramienta='"+id+"' ")){
                                    
                                }
                                break;
                            case 4:
                                if(conex.GUARDAR("UPDATE herramienta_consorcio SET codigo_herramienta='"+codigo+"' WHERE idherramienta='"+id+"' ")){                                    
                                }
                                break;
                            default: break;
                        }
                        conex.CERRAR();
                    }
                }
            });
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LA LISTA DE HERRAMIENTAS\n"+ex);
            Logger.getLogger(DialogoRegistrarHerramienta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cjherramienta = new CompuChiqui.JTextFieldPopup();
        cjtipo = new CompuChiqui.JTextFieldPopup();
        jLabel2 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaherramientas = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        cjbuscar = new CompuChiqui.JTextFieldPopup();
        btnEliminar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cjcodigo = new CompuChiqui.JTextFieldPopup();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel1.setText("Herramienta:");

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel2.setText("Tipo:");

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/RECURSOS/X16/Guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");

        tablaherramientas.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        tablaherramientas.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tablaherramientas);

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel3.setText("Buscar:");

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/RECURSOS/X16/Borrar.png"))); // NOI18N
        btnEliminar.setText("Eliminar");

        jLabel4.setFont(new java.awt.Font("Enter Sansman", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("HERRAMIENTAS CONSORCIO CDM");

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel5.setText("Codigo:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cjbuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminar)
                        .addGap(2, 2, 2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cjherramienta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cjcodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnGuardar)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(cjtipo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cjherramienta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cjtipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cjcodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cjbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogoRegistrarHerramienta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogoRegistrarHerramienta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogoRegistrarHerramienta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogoRegistrarHerramienta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogoRegistrarHerramienta dialog = new DialogoRegistrarHerramienta(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private CompuChiqui.JTextFieldPopup cjbuscar;
    private CompuChiqui.JTextFieldPopup cjcodigo;
    private CompuChiqui.JTextFieldPopup cjherramienta;
    private CompuChiqui.JTextFieldPopup cjtipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaherramientas;
    // End of variables declaration//GEN-END:variables
}
