package view;

import JTableAutoResizeColumn.TableColumnAdjuster;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import modelo.ConexionBD;
import modelo.CustomTableModel;

public class InformesDeProduccion extends javax.swing.JFrame{

    CustomTableModel modeloTabla;
    TableColumnAdjuster ajustarColumna;
    ConexionBD conexion = new ConexionBD();
    
    List<RowFilter<TableModel, Object>> filtros = new ArrayList<>();
    TableRowSorter rowSorter;
    RowFilter<TableModel, Object>  compoundRowFilter;
    
    public InformesDeProduccion(){
        initComponents();
        ajustarColumna = new TableColumnAdjuster(tablaDatos);
        cargarInterfazTabla();    
        cargarDatosTabla();
    }
    
    void cargarInterfazTabla(){
        modeloTabla = new CustomTableModel(
                new Object[][]{}, 
                new String[]{"ITEM","ORDEN","Nº SERIE","FECHA RECEPCION","FECHA ENTREGADO","CLIENTE","FASE","KVA","SERVICIO","LOTE"}, 
                tablaDatos, 
                new Class[]{Integer.class,String.class,String.class,String.class,String.class,String.class,Integer.class,Double.class,String.class,String.class}, 
                new Boolean[]{true,false,false,false,false,false,false,false,false,false});
        tablaDatos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tablaDatos.setCellSelectionEnabled(true);
        tablaDatos.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectNextColumnCell");
        tablaDatos.getColumnModel().getColumn(0).setCellRenderer(new JButtonIntoJTable.BotonEnColumna());
    }
    
    void cargarDatosTabla(){
        conexion.conectar();
        ResultSet rs = conexion.CONSULTAR("SELECT e.op, t.numeroserie, e.fecharecepcion, p.fechalaboratorio, c.nombrecliente, \n" +
                                        "t.fase, t.kvasalida, t.serviciosalida, e.lote FROM protocolos p\n" +
                                        "INNER JOIN transformador t USING(idtransformador)\n" +
                                        "INNER JOIN entrada e USING(identrada)\n" +
                                        "INNER JOIN cliente c USING(idcliente)\n" +
                                        "WHERE p.fechalaboratorio::date BETWEEN 'Tue Jan 03 11:05:37 COT 2017' AND 'Fri Feb 03 11:05:40 COT 2017' \n" +
                                        "ORDER BY p.fechalaboratorio, t.numeroserie ASC");
        try {
            while(rs.next()){
                modeloTabla.addRow(new Object[]{
                    rs.getRow(),
                    rs.getString("op"),
                    rs.getString("numeroserie"),
                    new SimpleDateFormat("dd-MMM-yy").format(rs.getDate("fecharecepcion")),
                    new SimpleDateFormat("dd-MMM-yy").format(rs.getDate("fechalaboratorio")),
                    rs.getString("nombrecliente"),
                    rs.getInt("fase"),
                    rs.getString("kvasalida"),
                    rs.getString("serviciosalida"),
                    rs.getString("lote")
                });
            }
            rowSorter = new TableRowSorter(modeloTabla);
            tablaDatos.setRowSorter(rowSorter);
            ajustarColumna.adjustColumns();
        } catch (Exception ex) {
            modelo.Metodos.ERROR(ex, "Error al cargar la tabla.");
            Logger.getLogger(InformesDeProduccion.class.getName()).log(Level.SEVERE, null, ex);
            modelo.Metodos.escribirFichero(ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuTabla = new javax.swing.JPopupMenu();
        subMenuFiltrar = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDatos = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        cjfechainicio = new com.toedter.calendar.JDateChooser();
        cjfechafin = new com.toedter.calendar.JDateChooser();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnExcel = new javax.swing.JButton();
        barraProgreso = new javax.swing.JProgressBar();
        jPanel2 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        subMenuFiltrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/buscar.png"))); // NOI18N
        subMenuFiltrar.setText("jMenuItem1");
        subMenuFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subMenuFiltrarActionPerformed(evt);
            }
        });
        menuTabla.add(subMenuFiltrar);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tablaDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablaDatos.setGridColor(new java.awt.Color(227, 227, 227));
        tablaDatos.setRowHeight(25);
        tablaDatos.getTableHeader().setReorderingAllowed(false);
        tablaDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaDatosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaDatos);

        jToolBar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToolBar1.setFloatable(false);

        jLabel1.setText("Periodo:");
        jToolBar1.add(jLabel1);

        cjfechainicio.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                cjfechainicioPropertyChange(evt);
            }
        });
        jToolBar1.add(cjfechainicio);

        cjfechafin.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                cjfechafinPropertyChange(evt);
            }
        });
        jToolBar1.add(cjfechafin);
        jToolBar1.add(jSeparator1);

        btnExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/excel.png"))); // NOI18N
        btnExcel.setFocusable(false);
        btnExcel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExcel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelActionPerformed(evt);
            }
        });
        jToolBar1.add(btnExcel);

        barraProgreso.setStringPainted(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(barraProgreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barraProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("TRAFOS TERMINADOS", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 395, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 307, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab2", jPanel2);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Editar");

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/basura.png"))); // NOI18N
        jMenuItem1.setText("Remover filtros");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

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
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cjfechainicioPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cjfechainicioPropertyChange
        if(cjfechainicio.getDate()!=null&&cjfechafin.getDate()!=null){
            cargarDatosTabla();
        }
    }//GEN-LAST:event_cjfechainicioPropertyChange

    private void cjfechafinPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cjfechafinPropertyChange
        if(cjfechainicio.getDate()!=null&&cjfechafin.getDate()!=null){
            cargarDatosTabla();
        }
    }//GEN-LAST:event_cjfechafinPropertyChange

    private void tablaDatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDatosMouseClicked
        if(SwingUtilities.isRightMouseButton(evt)){
            int col = tablaDatos.columnAtPoint(evt.getPoint());
            int row = tablaDatos.rowAtPoint(evt.getPoint());            
            tablaDatos.setColumnSelectionInterval(col, col);
            tablaDatos.setRowSelectionInterval(row, row);
            subMenuFiltrar.setText("Filtrar por: "+tablaDatos.getValueAt(row, col));
            menuTabla.show(tablaDatos, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tablaDatosMouseClicked

    private void subMenuFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subMenuFiltrarActionPerformed
        filtros.add(RowFilter.regexFilter(tablaDatos.getValueAt(tablaDatos.getSelectedRow(), tablaDatos.getSelectedColumn()).toString(), tablaDatos.getSelectedColumn()));
        compoundRowFilter = RowFilter.andFilter(filtros);
        rowSorter.setRowFilter(compoundRowFilter);
    }//GEN-LAST:event_subMenuFiltrarActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        filtros.clear();
        compoundRowFilter = RowFilter.andFilter(filtros);
        rowSorter.setRowFilter(compoundRowFilter);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelActionPerformed
        modelo.Metodos.generarExcel(tablaDatos, barraProgreso, btnExcel);
    }//GEN-LAST:event_btnExcelActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InformesDeProduccion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InformesDeProduccion().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barraProgreso;
    private javax.swing.JButton btnExcel;
    private com.toedter.calendar.JDateChooser cjfechafin;
    private com.toedter.calendar.JDateChooser cjfechainicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPopupMenu menuTabla;
    private javax.swing.JMenuItem subMenuFiltrar;
    private javax.swing.JTable tablaDatos;
    // End of variables declaration//GEN-END:variables
}
