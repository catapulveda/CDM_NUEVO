package view;

import JTableAutoResizeColumn.TableColumnAdjuster;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import modelo.ConexionBD;
import modelo.CustomTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jfree.data.category.DefaultCategoryDataset;

public class InformesDeProduccion extends javax.swing.JFrame{

    CustomTableModel modeloTabla;
    TableColumnAdjuster ajustarColumna;
    ConexionBD conexion = new ConexionBD();
    
    List<RowFilter<TableModel, Object>> filtros = new ArrayList<>();
    TableRowSorter rowSorter;
    RowFilter<TableModel, Object>  compoundRowFilter;
    
    DefaultCategoryDataset dataSetUnidades = new DefaultCategoryDataset();
    DefaultCategoryDataset dataSetKva = new DefaultCategoryDataset();
    DefaultCategoryDataset dataSetServicios = new DefaultCategoryDataset();
    DefaultCategoryDataset dataSetFases = new DefaultCategoryDataset();
    
    public InformesDeProduccion(){
        initComponents();
        
        panelCantidades.setLayout(new java.awt.BorderLayout());
        panelFases.setLayout(new java.awt.BorderLayout());
        panelKva.setLayout(new java.awt.BorderLayout());
        panelServicios.setLayout(new java.awt.BorderLayout());
        
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
        cargarInterfazTabla();
        
        if(cjfechainicio.getDate()==null||cjfechafin.getDate()==null){
            return;
        }
        
        tablaDatos.setRowSorter(null);
        conexion.conectar();
        ResultSet rs = conexion.CONSULTAR("SELECT e.op, t.numeroserie, e.fecharecepcion, p.fechalaboratorio, c.nombrecliente, \n" +
                                        "t.fase, t.kvasalida, t.serviciosalida, e.lote FROM protocolos p\n" +
                                        "INNER JOIN transformador t USING(idtransformador)\n" +
                                        "INNER JOIN entrada e USING(identrada)\n" +
                                        "INNER JOIN cliente c USING(idcliente)\n" +
                                        "WHERE p.fechalaboratorio::date BETWEEN '"+cjfechainicio.getDate()+"' AND '"+cjfechafin.getDate()+"' \n" +
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
        }
                
        rs = conexion.CONSULTAR("SELECT count(*), extract(year from fechalaboratorio) AS ano, extract(month from fechalaboratorio), to_char(fechalaboratorio, 'TMMonth') AS mes \n" +
                                "FROM protocolos WHERE fechalaboratorio BETWEEN '"+cjfechainicio.getDate()+"' AND '"+cjfechafin.getDate()+"' \n" +
                                "GROUP BY extract(year from fechalaboratorio), extract(month from fechalaboratorio), to_char(fechalaboratorio, 'TMMonth')\n" +
                                "ORDER BY extract(month from fechalaboratorio) ASC");
        try {
            dataSetUnidades.clear();
            while(rs.next()){
                dataSetUnidades.addValue(rs.getInt("count"), "AÑO: "+rs.getString("ano"), rs.getString("mes"));
            }            
            modelo.Metodos.generarGrafica(dataSetUnidades, "UNIDADES REPARADAS", "MES", "CANTIDAD", panelCantidades);
            validate();
        } catch (Exception ex) {
            modelo.Metodos.ERROR(ex, "ERROR AL CARGAR LA GRAFICA DE CANTIDADES.");
            Logger.getLogger(InformesDeProduccion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        rs = conexion.CONSULTAR("SELECT sum(t.kvasalida), extract(year from fechalaboratorio) AS ano, extract(month from fechalaboratorio), to_char(fechalaboratorio, 'TMMonth') AS mes \n" +
                                "FROM protocolos p INNER JOIN transformador t USING(idtransformador) WHERE fechalaboratorio BETWEEN '"+cjfechainicio.getDate()+"' AND '"+cjfechafin.getDate()+"' \n" +
                                "GROUP BY extract(year from fechalaboratorio), extract(month from fechalaboratorio), to_char(fechalaboratorio, 'TMMonth')\n" +
                                "ORDER BY extract(month from fechalaboratorio) ASC");
        try {
            dataSetKva.clear();
            while(rs.next()){
                dataSetKva.addValue(rs.getInt("sum"), "AÑO: "+rs.getString("ano"), rs.getString("mes"));
            }
            modelo.Metodos.generarGrafica(dataSetKva, "KVA PRODUCIDOS", "MES", "KVA", panelKva);
            validate();
        } catch (Exception ex) {
            modelo.Metodos.ERROR(ex, "ERROR AL CARGAR LA GRAFICA DE CANTIDADES.");
            Logger.getLogger(InformesDeProduccion.class.getName()).log(Level.SEVERE, null, ex);
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
        barraProgreso = new javax.swing.JProgressBar();
        jToolBar2 = new javax.swing.JToolBar();
        btnImprimir = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnExcel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        panelCantidades = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        panelKva = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        panelServicios = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        panelFases = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        cjfechainicio = new com.toedter.calendar.JDateChooser();
        cjfechafin = new com.toedter.calendar.JDateChooser();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        comboMes = new com.toedter.calendar.JMonthChooser();
        jButton1 = new javax.swing.JButton();
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

        barraProgreso.setStringPainted(true);

        jToolBar2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar2.setFloatable(false);
        jToolBar2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/imprimir.png"))); // NOI18N
        btnImprimir.setFocusable(false);
        btnImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });
        jToolBar2.add(btnImprimir);
        jToolBar2.add(jSeparator3);

        btnExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/excel.png"))); // NOI18N
        btnExcel.setFocusable(false);
        btnExcel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExcel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelActionPerformed(evt);
            }
        });
        jToolBar2.add(btnExcel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(barraProgreso, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barraProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("REPORTES", jPanel1);

        javax.swing.GroupLayout panelCantidadesLayout = new javax.swing.GroupLayout(panelCantidades);
        panelCantidades.setLayout(panelCantidadesLayout);
        panelCantidadesLayout.setHorizontalGroup(
            panelCantidadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 355, Short.MAX_VALUE)
        );
        panelCantidadesLayout.setVerticalGroup(
            panelCantidadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 229, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelCantidades, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelCantidades, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("CANTIDADES", jPanel2);

        javax.swing.GroupLayout panelKvaLayout = new javax.swing.GroupLayout(panelKva);
        panelKva.setLayout(panelKvaLayout);
        panelKvaLayout.setHorizontalGroup(
            panelKvaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 355, Short.MAX_VALUE)
        );
        panelKvaLayout.setVerticalGroup(
            panelKvaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 229, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelKva, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelKva, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("KVA", jPanel3);

        javax.swing.GroupLayout panelServiciosLayout = new javax.swing.GroupLayout(panelServicios);
        panelServicios.setLayout(panelServiciosLayout);
        panelServiciosLayout.setHorizontalGroup(
            panelServiciosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 355, Short.MAX_VALUE)
        );
        panelServiciosLayout.setVerticalGroup(
            panelServiciosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 229, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelServicios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelServicios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("SERVICIOS", jPanel4);

        javax.swing.GroupLayout panelFasesLayout = new javax.swing.GroupLayout(panelFases);
        panelFases.setLayout(panelFasesLayout);
        panelFasesLayout.setHorizontalGroup(
            panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 355, Short.MAX_VALUE)
        );
        panelFasesLayout.setVerticalGroup(
            panelFasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 229, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFases, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFases, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("FASES", jPanel5);

        jToolBar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToolBar1.setFloatable(false);

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
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
        jToolBar1.add(comboMes);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/buscar.png"))); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
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

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        (new Thread(){
            @Override
            public void run(){
                try{
                    btnImprimir.setEnabled(false);
                    btnImprimir.setIcon(new ImageIcon(getClass().getResource("/recursos/images/gif.gif")));
                    JasperReport reporte = (JasperReport) JRLoader.loadObject(new URL(this.getClass().getResource("/REPORTES/INFORME_ANUAL.jasper").toString()));
                    Map<String, Object> p = new HashMap<String, Object>();
                    p.put("INICIO", cjfechainicio.getDate());
                    p.put("FIN", cjfechafin.getDate());
                    JasperPrint jasperprint = JasperFillManager.fillReport(reporte, p, conexion.conectar());                        
                    JasperViewer.viewReport(jasperprint, false);                        
                    JasperViewer visor = new JasperViewer(jasperprint, false);                        
                }catch(JRException | MalformedURLException ex){
                    Logger.getLogger(EntradaDeTrafos.class.getName()).log(Level.SEVERE, null, ex);
                }finally{
                    btnImprimir.setIcon(new ImageIcon(getClass().getResource("/recursos/images/imprimir.png")));
                    btnImprimir.setEnabled(true);
                }
            }
        }).start();
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, comboMes.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        cjfechainicio.setDate(cal.getTime());

        cal.set(Calendar.MONTH, comboMes.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        cjfechafin.setDate(cal.getTime());
        
        cargarDatosTabla();
    }//GEN-LAST:event_jButton1ActionPerformed

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
    private javax.swing.JButton btnImprimir;
    private com.toedter.calendar.JDateChooser cjfechafin;
    private com.toedter.calendar.JDateChooser cjfechainicio;
    private com.toedter.calendar.JMonthChooser comboMes;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JPopupMenu menuTabla;
    private javax.swing.JPanel panelCantidades;
    private javax.swing.JPanel panelFases;
    private javax.swing.JPanel panelKva;
    private javax.swing.JPanel panelServicios;
    private javax.swing.JMenuItem subMenuFiltrar;
    private javax.swing.JTable tablaDatos;
    // End of variables declaration//GEN-END:variables
}
